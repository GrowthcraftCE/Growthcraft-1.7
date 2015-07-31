package growthcraft.core.block;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.render.RenderRope;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRope extends Block implements IBlockRope
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockRope()
	{
		super(Material.circuits);
		this.setHardness(0.5F);
		//this.setStepSound(soundWoodFootstep);
		this.setBlockName("grc.ropeBlock");
		this.setCreativeTab(null);
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		this.checkBlockCoordValid(world, x, y, z);
	}

	protected final void checkBlockCoordValid(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
		boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
		boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
		boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
		boolean flag4 = this.canConnectRopeTo(world, x, y - 1, z);
		boolean flag5 = this.canConnectRopeTo(world, x, y + 1, z);
		return flag || flag1 || flag2 || flag3 || flag4 || flag5;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return this.canPlaceBlockAt(world, x, y, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftCore.rope;
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) instanceof IBlockRope;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftCore.rope;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("grccore:rope_1");
		tex[1] = reg.registerIcon("grccore:rope");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[1];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderRope.id;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int par5)
	{
		return true;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
	{
		boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
		boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
		boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
		boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
		boolean flag4 = this.canConnectRopeTo(world, x, y - 1, z);
		boolean flag5 = this.canConnectRopeTo(world, x, y + 1, z);
		float f = 0.4375F;
		float f1 = 0.5625F;
		float f2 = 0.4375F;
		float f3 = 0.5625F;
		float f4 = 0.4375F;
		float f5 = 0.5625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag || flag1)
		{
			this.setBlockBounds(f, f4, f2, f1, f5, f3);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		f2 = 0.4375F;
		f3 = 0.5625F;

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag2 || flag3)
		{
			this.setBlockBounds(f, f4, f2, f1, f5, f3);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		f = 0.4375F;
		f1 = 0.5625F;

		if (flag4)
		{
			f4 = 0.0F;
		}

		if (flag5)
		{
			f5 = 1.0F;
		}

		if (flag4 || flag5)
		{
			this.setBlockBounds(f, f4, f2, f1, f5, f3);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		this.setBlockBoundsBasedOnState(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
		boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
		boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
		boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
		boolean flag4 = this.canConnectRopeTo(world, x, y - 1, z);
		boolean flag5 = this.canConnectRopeTo(world, x, y + 1, z);
		float f = 0.4375F;
		float f1 = 0.5625F;
		float f2 = 0.4375F;
		float f3 = 0.5625F;
		float f4 = 0.4375F;
		float f5 = 0.5625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag4)
		{
			f4 = 0.0F;
		}

		if (flag5)
		{
			f5 = 1.0F;
		}

		this.setBlockBounds(f, f4, f2, f1, f5, f3);
	}

}
