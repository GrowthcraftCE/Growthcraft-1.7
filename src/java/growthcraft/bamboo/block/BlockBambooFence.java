package growthcraft.bamboo.block;

import java.util.List;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.renderer.RenderBambooFence;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBambooFence extends Block
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockBambooFence()
	{
		super(Material.wood);
		this.useNeighborBrightness = true;
		this.setStepSound(soundTypeWood);
		this.setResistance(5.0F);
		this.setHardness(2.0F);
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooFence");
	}

	/************
	 * STUFF
	 ************/
	@Override
	public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);

		if (block != this && block != Blocks.fence_gate && block != GrowthCraftBamboo.bambooFenceGate && block != GrowthCraftBamboo.bambooWall && block != GrowthCraftBamboo.bambooStalk)
		{
			return block != null && block.getMaterial().isOpaque() && block.renderAsNormalBlock() ? block.getMaterial() != Material.gourd : false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	{
		return true;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("grcbamboo:fence_top");
		tex[1] = reg.registerIcon("grcbamboo:fence");
		tex[2] = reg.registerIcon("grcbamboo:block");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side == 1 ? tex[0] : ( side == 0 ? tex[0] : tex[1]);
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBambooFence.id;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		Block idXneg = world.getBlock(x - 1, y, z);
		Block idXpos = world.getBlock(x + 1, y, z);
		Block idZneg = world.getBlock(x, y, z - 1);
		Block idZpos = world.getBlock(x, y, z + 1);

		int metaXneg = world.getBlockMetadata(x - 1, y, z);
		int metaXpos = world.getBlockMetadata(x + 1, y, z);
		int metaZneg = world.getBlockMetadata(x, y, z - 1);
		int metaZpos = world.getBlockMetadata(x, y, z + 1);

		boolean flagXneg = this.canConnectFenceTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
		boolean flagXpos = this.canConnectFenceTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
		boolean flagZneg = this.canConnectFenceTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
		boolean flagZpos = this.canConnectFenceTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);

		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flagZneg)
		{
			f2 = 0.0F;
		}

		if (flagZpos)
		{
			f3 = 1.0F;
		}

		if (flagXneg)
		{
			f = 0.0F;
		}

		if (flagXpos)
		{
			f1 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity)
	{
		Block idXneg = world.getBlock(x - 1, y, z);
		Block idXpos = world.getBlock(x + 1, y, z);
		Block idZneg = world.getBlock(x, y, z - 1);
		Block idZpos = world.getBlock(x, y, z + 1);

		int metaXneg = world.getBlockMetadata(x - 1, y, z);
		int metaXpos = world.getBlockMetadata(x + 1, y, z);
		int metaZneg = world.getBlockMetadata(x, y, z - 1);
		int metaZpos = world.getBlockMetadata(x, y, z + 1);

		boolean flagXneg = this.canConnectFenceTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
		boolean flagXpos = this.canConnectFenceTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
		boolean flagZneg = this.canConnectFenceTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
		boolean flagZpos = this.canConnectFenceTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);

		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flagZneg)
		{
			f2 = 0.0F;
		}

		if (flagZpos)
		{
			f3 = 1.0F;
		}

		if (flagZneg || flagZpos)
		{
			this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
			super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		}

		f2 = 0.375F;
		f3 = 0.625F;

		if (flagXneg)
		{
			f = 0.0F;
		}

		if (flagXpos)
		{
			f1 = 1.0F;
		}

		if (flagXneg || flagXpos || !flagZneg && !flagZpos)
		{
			this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
			super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		}

		if (flagZneg)
		{
			f2 = 0.0F;
		}

		if (flagZpos)
		{
			f3 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}
}
