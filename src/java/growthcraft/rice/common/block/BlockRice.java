package growthcraft.rice.common.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.common.block.IPaddyCrop;
import growthcraft.core.integration.AppleCore;
import growthcraft.rice.client.renderer.RenderRice;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRice extends GrcBlockBase implements IPaddyCrop, ICropDataProvider, IGrowable
{
	public static class RiceStage
	{
		public static final int MATURE = 7;

		private RiceStage() {}
	}

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private final float growth = GrowthCraftRice.getConfig().riceGrowthRate;

	public BlockRice()
	{
		super(Material.plants);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(null);
		this.setBlockName("grc.riceBlock");
		this.setStepSound(soundTypeGrass);
	}

	public boolean isMature(IBlockAccess world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		return meta >= RiceStage.MATURE;
	}

	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta)
	{
		return (float)meta / (float)RiceStage.MATURE;
	}

	private void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		world.setBlockMetadataWithNotify(x, y, z, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, x, y, z, meta);
	}

	private void growRice(World world, int x, int y, int z, int meta)
	{
		incrementGrowth(world, x, y, z, meta);
		final Block paddyBlock = world.getBlock(x, y - 1, z);
		if (RiceBlockCheck.isPaddy(paddyBlock))
		{
			((BlockPaddy)paddyBlock).drainPaddy(world, x, y - 1, z);
		}
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		this.checkCropChange(world, x, y, z);

		if (world.getBlockLightValue(x, y + 1, z) >= 9 && world.getBlockMetadata(x, y - 1, z) > 0)
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, x, y, z, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final int meta = world.getBlockMetadata(x, y, z);

			if (meta < RiceStage.MATURE)
			{
				final float f = this.getGrowthRate(world, x, y, z);

				if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(this.growth / f) + 1) == 0))
				{
					growRice(world, x, y, z, meta);
				}
			}
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient)
	{
		return world.getBlockMetadata(x, y, z) < RiceStage.MATURE;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		if (meta < RiceStage.MATURE)
		{
			growRice(world, x, y, z, meta);
		}
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		final Block l = world.getBlock(x, y, z - 1);
		final Block i1 = world.getBlock(x, y, z + 1);
		final Block j1 = world.getBlock(x - 1, y, z);
		final Block k1 = world.getBlock(x + 1, y, z);
		final Block l1 = world.getBlock(x - 1, y, z - 1);
		final Block i2 = world.getBlock(x + 1, y, z - 1);
		final Block j2 = world.getBlock(x + 1, y, z + 1);
		final Block k2 = world.getBlock(x - 1, y, z + 1);
		final boolean flag = j1 == this || k1 == this;
		final boolean flag1 = l == this || i1 == this;
		final boolean flag2 = l1 == this || i2 == this || j2 == this || k2 == this;

		for (int loop_i = x - 1; loop_i <= x + 1; ++loop_i)
		{
			for (int loop_k = z - 1; loop_k <= z + 1; ++loop_k)
			{
				final Block soil = world.getBlock(loop_i, y - 1, loop_k);
				float f1 = 0.0F;

				if (soil != null && RiceBlockCheck.isPaddy(soil))
				{
					f1 = 1.0F;

					if (world.getBlockMetadata(loop_i, y - 1, loop_k) > 0)
					{
						f1 = 3.0F;
					}
				}

				if (loop_i != x || loop_k != z)
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

	protected final void checkCropChange(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		this.checkCropChange(world, x, y, z);
	}

	/************
	 * CONDITIONS
	 ************/

	/**
	 * @param block - block to place on
	 * @return can the rice be placed on this block?
	 */
	protected boolean canThisPlantGrowOnThisBlockID(Block block)
	{
		return RiceBlockCheck.isPaddy(block);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return (world.getFullBlockLightValue(x, y, z) >= 8 ||
			world.canBlockSeeTheSky(x, y, z)) &&
			this.canThisPlantGrowOnThisBlockID(world.getBlock(x, y - 1, z));
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftRice.rice.getItem();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftRice.rice.getItem();
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

		if (metadata >= 7)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				if (world.rand.nextInt(15) <= metadata)
				{
					ret.add(GrowthCraftRice.rice.asStack(1));
				}
			}
		}

		return ret;
	}

	/************
	 * TEXTURE
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[5];

		for (int i = 0; i < icons.length; ++i)
		{
			icons[i] = reg.registerIcon("grcrice:rice_" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta < 0 || meta > 7)
		{
			meta = 7;
		}

		int i = 0;
		switch (meta)
		{
			case 0: case 1: i = 0; break;
			case 2: case 3: i = 1; break;
			case 4: case 5: i = 2; break;
			case 6: case 7: i = 3; break;
			default: i = 2;
		}

		return icons[i];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderRice.id;
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

	/************
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
}
