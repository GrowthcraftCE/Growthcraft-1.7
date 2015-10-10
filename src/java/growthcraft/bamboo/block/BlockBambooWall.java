package growthcraft.bamboo.block;

import java.util.List;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.renderer.RenderBambooWall;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBambooWall extends Block
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockBambooWall()
	{
		super(Material.wood);
		this.useNeighborBrightness = true;
		this.setStepSound(soundTypeWood);
		this.setResistance(5.0F / 3.0F);
		this.setHardness(2.0F);
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooWall");
	}

	/************
	 * STUFF
	 ************/
	@Override
	public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	public boolean canConnectWallTo(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);

		if (block != this && block != Blocks.fence_gate && block != GrowthCraftBamboo.bambooFenceGate.getBlock() && block != GrowthCraftBamboo.bambooFence.getBlock() && block != GrowthCraftBamboo.bambooStalk.getBlock())
		{
			//Block block = Block.blocksList[id];
			//return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
			return false;
		}
		else
		{
			return true;
		}
	}

	/************
	 * ICONS
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("grcbamboo:fence_top");
		tex[1] = reg.registerIcon("grcbamboo:fence");
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
		return RenderBambooWall.id;
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
		int tm;

		Block idXneg = world.getBlock(x - 1, y, z);
		Block idXpos = world.getBlock(x + 1, y, z);
		Block idZneg = world.getBlock(x, y, z - 1);
		Block idZpos = world.getBlock(x, y, z + 1);

		int metaXneg = world.getBlockMetadata(x - 1, y, z);
		int metaXpos = world.getBlockMetadata(x + 1, y, z);
		int metaZneg = world.getBlockMetadata(x, y, z - 1);
		int metaZpos = world.getBlockMetadata(x, y, z + 1);

		boolean flagXneg = this.canConnectWallTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
		boolean flagXpos = this.canConnectWallTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
		boolean flagZneg = this.canConnectWallTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
		boolean flagZpos = this.canConnectWallTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);

		float x1 = 0.375F;
		float x2 = 0.625F;
		float z1 = 0.375F;
		float z2 = 0.625F;

		//ZNEG
		if (flagZneg)
		{
			z1 = 0.0F;
		}
		else if (idZneg instanceof BlockDoor)
		{
			if ((metaZneg & 8) > 7)
			{
				metaZneg = world.getBlockMetadata(x, y - 1, z - 1);
			}

			tm = metaZneg & 3;

			if (tm == 0 || tm == 2)
			{
				z1 = 0.0F;

				if (tm == 0)
				{
					x1 = 0.0F;
				}

				if (tm == 2)
				{
					x2 = 1.0F;
				}
			}
		}

		//ZPOS
		if (flagZpos)
		{
			z2 = 1.0F;
		}
		else if (idZpos instanceof BlockDoor)
		{
			if ((metaZpos & 8) > 7)
			{
				metaZpos = world.getBlockMetadata(x, y - 1, z + 1);
			}

			tm = metaZpos & 3;

			if (tm == 0 || tm == 2)
			{
				z2 = 1.0F;

				if (tm == 0)
				{
					x1 = 0.0F;
				}

				if (tm == 2)
				{
					x2 = 1.0F;
				}
			}
		}

		//XNEG
		if (flagXneg)
		{
			x1 = 0.0F;
		}
		else if (idXneg instanceof BlockDoor)
		{
			if ((metaXneg & 8) > 7)
			{
				metaXneg = world.getBlockMetadata(x - 1, y - 1, z);
			}

			tm = metaXneg & 3;

			if (tm == 1 || tm == 3)
			{
				x1 = 0.0F;

				if (tm == 1)
				{
					z1 = 0.0F;
				}

				if (tm == 3)
				{
					z2 = 1.0F;
				}
			}
		}

		//XPOS
		if (flagXpos)
		{
			x2 = 1.0F;
		}
		else if (idXpos instanceof BlockDoor)
		{
			if ((metaXpos & 8) > 7)
			{
				metaXpos = world.getBlockMetadata(x + 1, y - 1, z);
			}

			tm = metaXpos & 3;

			if (tm == 1 || tm == 3)
			{
				x2 = 1.0F;

				if (tm == 1)
				{
					z1 = 0.0F;
				}

				if (tm == 3)
				{
					z2 = 1.0F;
				}
			}
		}

		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity)
	{
		int tm;

		Block idXneg = world.getBlock(x - 1, y, z);
		Block idXpos = world.getBlock(x + 1, y, z);
		Block idZneg = world.getBlock(x, y, z - 1);
		Block idZpos = world.getBlock(x, y, z + 1);

		int metaXneg = world.getBlockMetadata(x - 1, y, z);
		int metaXpos = world.getBlockMetadata(x + 1, y, z);
		int metaZneg = world.getBlockMetadata(x, y, z - 1);
		int metaZpos = world.getBlockMetadata(x, y, z + 1);

		boolean flagXneg = this.canConnectWallTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
		boolean flagXpos = this.canConnectWallTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
		boolean flagZneg = this.canConnectWallTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
		boolean flagZpos = this.canConnectWallTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);

		float x1 = 0.375F;
		float x2 = 0.625F;
		float z1 = 0.375F;
		float z2 = 0.625F;

		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		//XNEG
		if (flagXneg)
		{
			x1 = 0.0F;
			x2 = 0.375F;
			z1 = 0.375F;
			z2 = 0.625F;

			this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
			super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		}
		else if (idXneg instanceof BlockDoor)
		{
			if ((metaXneg & 8) > 7)
			{
				metaXneg = world.getBlockMetadata(x - 1, y - 1, z);
			}

			tm = metaXneg & 3;

			if (tm == 1 || tm == 3)
			{
				x1 = 0.0F;
				x2 = 0.375F;
				z1 = 0.375F;
				z2 = 0.625F;

				this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

				if (tm == 1)
				{

					x1 = 0.0F;
					x2 = 0.25F;
					z1 = 0.0F;
					z2 = 0.375F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}

				if (tm == 3)
				{

					x1 = 0.0F;
					x2 = 0.25F;
					z1 = 0.625F;
					z2 = 1.0F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}
			}
		}

		//XPOS
		if (flagXpos)
		{
			x1 = 0.625F;
			x2 = 1.0F;
			z1 = 0.375F;
			z2 = 0.625F;

			this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
			super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		}
		else if (idXpos instanceof BlockDoor)
		{
			if ((metaXpos & 8) > 7)
			{
				metaXpos = world.getBlockMetadata(x + 1, y - 1, z);
			}

			tm = metaXpos & 3;

			if (tm == 1 || tm == 3)
			{
				x1 = 0.625F;
				x2 = 1.0F;
				z1 = 0.375F;
				z2 = 0.625F;

				this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

				if (tm == 1)
				{
					x1 = 0.75F;
					x2 = 1.0F;
					z1 = 0.0F;
					z2 = 0.375F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}

				if (tm == 3)
				{
					x1 = 0.75F;
					x2 = 1.0F;
					z1 = 0.625F;
					z2 = 1.0F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}
			}
		}

		//ZNEG
		if (flagZneg)
		{

			x1 = 0.375F;
			x2 = 0.625F;
			z1 = 0.0F;
			z2 = 0.375F;

			this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
			super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		}
		else if (idZneg instanceof BlockDoor)
		{
			if ((metaZneg & 8) > 7)
			{
				metaZneg = world.getBlockMetadata(x, y - 1, z - 1);
			}

			tm = metaZneg & 3;

			if (tm == 0 || tm == 2)
			{
				x1 = 0.375F;
				x2 = 0.625F;
				z1 = 0.0F;
				z2 = 0.375F;

				this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

				if (tm == 0)
				{
					x1 = 0.0F;
					x2 = 0.375F;
					z1 = 0.0F;
					z2 = 0.25F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}

				if (tm == 2)
				{
					x1 = 0.625F;
					x2 = 1.0F;
					z1 = 0.0F;
					z2 = 0.25F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}
			}
		}

		//ZPOS
		if (flagZpos)
		{
			x1 = 0.375F;
			x2 = 0.625F;
			z1 = 0.625F;
			z2 = 1.0F;

			this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
			super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		}
		else if (idZpos instanceof BlockDoor)
		{
			if ((metaZpos & 8) > 7)
			{
				metaZpos = world.getBlockMetadata(x, y - 1, z + 1);
			}

			tm = metaZpos & 3;

			if (tm == 0 || tm == 2)
			{
				x1 = 0.375F;
				x2 = 0.625F;
				z1 = 0.625F;
				z2 = 1.0F;

				this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

				if (tm == 0)
				{
					x1 = 0.0F;
					x2 = 0.375F;
					z1 = 0.75F;
					z2 = 1.0F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}

				if (tm == 2)
				{
					x1 = 0.625F;
					x2 = 1.0F;
					z1 = 0.75F;
					z2 = 1.0F;

					this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
					super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
				}
			}
		}

		this.setBlockBoundsBasedOnState(world, x, y, z);
	}
}
