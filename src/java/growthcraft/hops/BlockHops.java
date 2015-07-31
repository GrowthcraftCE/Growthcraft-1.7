package growthcraft.hops;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.block.IBlockRope;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHops extends Block implements IBlockRope, IPlantable
{
	//Constants
	private final float growth = GrowthCraftHops.hopVine_growth;
	private final float growth2 = GrowthCraftHops.hopVine_growth2;

	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;
	public static Boolean graphicFlag;

	public BlockHops()
	{
		super(Material.plants);
		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.hopVine");
		this.setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		// 0 - Bine
		// 1 - Small Leaves
		// 2 - Big
		// 3 - Ready

		if (!this.canBlockStay(world, x, y, z))
		{
			world.setBlock(x, y, z, GrowthCraftCore.ropeBlock);
		}
		else
		{
			int meta = world.getBlockMetadata(x, y, z);
			float f = this.getGrowthRateLoop(world, x, y, z);

			if (meta < 2)
			{
				if (random.nextInt((int)(this.growth / f) + 1) == 0)
				{
					++meta;
					world.setBlockMetadataWithNotify(x, y, z, meta, 3);
				}
			}
			else if ((meta == 2 || meta == 3) && world.getBlock(x, y + 1, z) == GrowthCraftCore.ropeBlock && this.canBlockStay(world, x, y + 1, z))
			{
				if (random.nextInt((int)(this.growth / f) + 1) == 0)
				{
					world.setBlock(x, y + 1, z, this, 2, 3);
				}
			}
			else if (meta == 2)
			{
				if (random.nextInt((int)(this.growth2 / f) + 1) == 0)
				{
					++meta;
					world.setBlockMetadataWithNotify(x, y, z, meta, 3);
				}
			}
		}
	}

	private float getGrowthRateLoop(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y - 1, z) == Blocks.farmland)
		{
			return getGrowthRate(world, x, y, z);
		}
		else
		{
			int loop = 1;

			while (loop < 5)
			{
				if (world.getBlock(x, y - loop, z) != this)
				{
					return getGrowthRate(world, x, y, z);
				}

				if (isVineRoot(world, x, y - loop, z))
				{
					return getGrowthRate(world, x, y - loop, z);
				}
				loop++;
			}

			return getGrowthRate(world, x, y, z);
		}
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		Block l = world.getBlock(x, y, z - 1);
		Block i1 = world.getBlock(x, y, z + 1);
		Block j1 = world.getBlock(x - 1, y, z);
		Block k1 = world.getBlock(x + 1, y, z);
		Block l1 = world.getBlock(x - 1, y, z - 1);
		Block i2 = world.getBlock(x + 1, y, z - 1);
		Block j2 = world.getBlock(x + 1, y, z + 1);
		Block k2 = world.getBlock(x - 1, y, z + 1);
		boolean flag = j1 == this || k1 == this;
		boolean flag1 = l == this || i1 == this;
		boolean flag2 = l1 == this || i2 == this || j2 == this || k2 == this;

		for (int l2 = x - 1; l2 <= x + 1; ++l2)
		{
			for (int i3 = z - 1; i3 <= z + 1; ++i3)
			{
				Block block = world.getBlock(l2, y - 1, i3);
				float f1 = 0.0F;

				if (block != null && block == Blocks.farmland)
				{
					f1 = 1.0F;

					if (block.isFertile(world, l2, y - 1, i3))
					{
						f1 = 3.0F;
					}
				}

				if (l2 != x || i3 != z)
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if (flag2 || flag && flag1)
		{
			f /= 2.0F;
		}

		return f;
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dir, float par7, float par8, float par9)
	{
		if (world.getBlockMetadata(x, y, z) == 3)
		{
			if (!world.isRemote)
			{
				world.setBlockMetadataWithNotify(x, y, z, 2, 3);
				this.dropBlockAsItem(world, x, y, z, new ItemStack(GrowthCraftHops.hops, world.rand.nextInt(8) + 1));

			}
			return true;
		}
		else
		{
			return false;
		}
	}

	/*@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			world.setBlock(x, y, z, GrowthCraftCore.ropeBlock.blockID, 0, 3);
		}
	}*/

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y - 1, z) == Blocks.farmland)
		{
			return true;
		}
		else
		{
			int loop = 1;

			while (loop < 5)
			{
				if (world.getBlock(x, y - loop, z) != this)
				{
					return false;
				}

				if (isVineRoot(world, x, y - loop, z))
				{
					return true;
				}
				loop++;
			}

			return false;
		}
	}

	private boolean isVineRoot(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == this && world.getBlock(x, y - 1, z) == Blocks.farmland && (world.getBlockMetadata(x, y, z) == 2 || world.getBlockMetadata(x, y, z) == 3);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return meta < 3 ? GrowthCraftHops.hopSeeds : GrowthCraftHops.hops;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return false;
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) instanceof IBlockRope)
		{
			return true;
		}
		return false;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(GrowthCraftCore.rope));
		if (world.getBlockMetadata(x, y, z) == 3)
		{
			ret.add(new ItemStack(GrowthCraftHops.hops, world.rand.nextInt(8) + 1));
		}
		return ret;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[7];

		tex[0] = reg.registerIcon("grchops:leaves");
		tex[1] = reg.registerIcon("grchops:leaves_opaque");
		tex[2] = reg.registerIcon("grchops:bine");
		tex[3] = reg.registerIcon("grchops:leaves_hops");
		tex[4] = reg.registerIcon("grccore:rope_1");
		tex[5] = reg.registerIcon("grchops:leaves_x");
		tex[6] = reg.registerIcon("grchops:leaves_opaque_x");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta != 0)
		{
			this.graphicFlag = !((BlockLeaves)Blocks.leaves).isOpaqueCube();
			if (meta == 3)
			{
				return this.graphicFlag ? this.tex[5] : this.tex[6];
			}
			else
			{
				return this.graphicFlag ? this.tex[0] : this.tex[1];
			}
		}
		else
		{
			return this.tex[2];
		}
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderHops.id;
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

	/************
	 * COLORS
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		double d0 = 0.5D;
		double d1 = 1.0D;
		return ColorizerFoliage.getFoliageColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		return ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);

		int r = 0;
		int g = 0;
		int b = 0;

		for (int l1 = -1; l1 <= 1; ++l1)
		{
			for (int i2 = -1; i2 <= 1; ++i2)
			{
				int j2 = world.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
				r += (j2 & 16711680) >> 16;
			g += (j2 & 65280) >> 8;
		b += j2 & 255;
			}
		}

		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
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
		int meta = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		switch (meta)
		{
		case 0:
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 5*f, 10*f);
			break;
		case 1:
			this.setBlockBounds(4*f, 0.0F, 4*f, 12*f, 8*f, 12*f);
			break;
		default:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		}
	}

	/************
	 * IPLANTABLE
	 ************/
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}
}
