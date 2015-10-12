package growthcraft.grapes.block;

import java.util.Random;

import growthcraft.core.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.utils.BlockCheck;
import growthcraft.core.utils.BlockFlags;
import growthcraft.grapes.utils.GrapeBlockCheck;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockGrapeVineBase extends Block implements IPlantable, ICropDataProvider
{
	private ItemStack itemDrop;
	private float growthRateMultiplier;

	public BlockGrapeVineBase()
	{
		super(Material.plants);
		this.itemDrop = new ItemStack((Item)null, 0);
		this.growthRateMultiplier = 1.0f;
	}

	public void setItemDrop(ItemStack itemstack)
	{
		this.itemDrop = itemstack;
	}

	public ItemStack getItemDrop()
	{
		return this.itemDrop;
	}

	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return itemDrop.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return itemDrop.stackSize;
	}

	public void setGrowthRateMultiplier(float rate)
	{
		this.growthRateMultiplier = rate;
	}

	public float getGrowthRateMultiplier()
	{
		return this.growthRateMultiplier;
	}

	public int getGrowthMax()
	{
		return 1;
	}

	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta)
	{
		return (float)(meta / getGrowthMax());
	}

	protected boolean isGrapeVine(Block block)
	{
		return GrapeBlockCheck.isGrapeVine(block);
	}

	public void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		final int previousMetadata = meta;
		++meta;
		world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_CLIENT);
		AppleCore.announceGrowthTick(this, world, x, y, z, previousMetadata);
	}

	protected float getGrowthRate(World world, int x, int y, int z)
	{
		final Block l = world.getBlock(x, y, z - 1);
		final Block i1 = world.getBlock(x, y, z + 1);
		final Block j1 = world.getBlock(x - 1, y, z);
		final Block k1 = world.getBlock(x + 1, y, z);
		final Block l1 = world.getBlock(x - 1, y, z - 1);
		final Block i2 = world.getBlock(x + 1, y, z - 1);
		final Block j2 = world.getBlock(x + 1, y, z + 1);
		final Block k2 = world.getBlock(x - 1, y, z + 1);
		final boolean flag = this.isGrapeVine(j1) || this.isGrapeVine(k1);
		final boolean flag1 = this.isGrapeVine(l) || this.isGrapeVine(i1);
		final boolean flag2 = this.isGrapeVine(l1) || this.isGrapeVine(i2) || this.isGrapeVine(j2) || this.isGrapeVine(k2);
		float f = 1.0F;

		for (int l2 = x - 1; l2 <= x + 1; ++l2)
		{
			for (int i3 = z - 1; i3 <= z + 1; ++i3)
			{
				final Block block = world.getBlock(l2, y - 1, i3);
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

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return BlockCheck.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, BlockFlags.UPDATE_CLIENT);
		}
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return false;
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

	/**
	 * If all conditions have passed, do plant growth
	 *
	 * @param world - world with block
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @param meta - block metadata
	 */
	protected abstract void doGrowth(World world, int x, int y, int z, int meta);

	/**
	 * Are the conditions right for this plant to grow?
	 *
	 * @param world - world with block
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true, it can grow, false otherwise
	 */
	protected abstract boolean canUpdateGrowth(World world, int x, int y, int z);

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);

		if (canUpdateGrowth(world, x, y, z))
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, x, y, z, random);
			if (Event.Result.DENY == allowGrowthResult)
				return;

			final int meta = world.getBlockMetadata(x, y, z);
			final float f = this.getGrowthRate(world, x, y, z);

			final boolean continueGrowth = random.nextInt((int)(getGrowthRateMultiplier() / f) + 1) == 0;
			if (Event.Result.ALLOW == allowGrowthResult || continueGrowth)
			{
				doGrowth(world, x, y, z, meta);
			}
		}
	}
}
