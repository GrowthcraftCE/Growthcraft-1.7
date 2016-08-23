/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;

import growthcraft.api.core.nbt.INBTItemSerializable;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.item.IItemTileBlock;
import growthcraft.core.common.tileentity.feature.ICustomDisplayName;
import growthcraft.core.common.tileentity.feature.IItemHandler;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Base class for machines and the like
 */
public abstract class GrcBlockContainer extends GrcBlockBase implements IDroppableBlock, IRotatableBlock, IWrenchable, ITileEntityProvider
{
	protected Random rand = new Random();
	protected Class<? extends TileEntity> tileEntityType;

	public GrcBlockContainer(@Nonnull Material material)
	{
		super(material);
		this.isBlockContainer = true;
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int code, int value)
	{
		super.onBlockEventReceived(world, x, y, z, code, value);
		final TileEntity te = getTileEntity(world, x, y, z);
		return te != null ? te.receiveClientEvent(code, value) : false;
	}

	protected void setTileEntityType(Class<? extends TileEntity> klass)
	{
		this.tileEntityType = klass;
	}

	/* IRotatableBlock */
	@Override
	public boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return false;
	}

	public void doRotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final ForgeDirection current = ForgeDirection.getOrientation(meta);
		ForgeDirection newDirection = current;
		if (current == side)
		{
			switch (current)
			{
				case UP:
					newDirection = ForgeDirection.NORTH;
					break;
				case DOWN:
					newDirection = ForgeDirection.SOUTH;
					break;
				case NORTH:
				case EAST:
					newDirection = ForgeDirection.UP;
					break;
				case SOUTH:
				case WEST:
					newDirection = ForgeDirection.DOWN;
					break;
				default:
					// some invalid state
					break;
			}
		}
		else
		{
			switch (current)
			{
				case UP:
					newDirection = ForgeDirection.DOWN;
					break;
				case DOWN:
					newDirection = ForgeDirection.UP;
					break;
				case WEST:
					newDirection = ForgeDirection.SOUTH;
					break;
				case EAST:
					newDirection = ForgeDirection.NORTH;
					break;
				case NORTH:
					newDirection = ForgeDirection.WEST;
					break;
				case SOUTH:
					newDirection = ForgeDirection.EAST;
					break;
				default:
					// yet another invalid state
					break;
			}
		}
		if (newDirection != current)
		{
			world.setBlockMetadataWithNotify(x, y, z, newDirection.ordinal(), BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		if (isRotatable(world, x, y, z, side))
		{
			doRotateBlock(world, x, y, z, side);
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		return false;
	}

	protected void fellBlockFromWrench(World world, int x, int y, int z)
	{
		final int metadata = world.getBlockMetadata(x, y, z);
		final List<ItemStack> drops = new ArrayList<ItemStack>();
		if (shouldDropTileStack(world, x, y, z, metadata, 0))
		{
			GrowthCraftCore.getLogger().info("Dropping Tile As ItemStack");
			getTileItemStackDrops(drops, world, x, y, z, metadata, 0);
			for (ItemStack stack : drops)
			{
				ItemUtils.spawnItemStack(world, x, y, z, stack, world.rand);
			}
			final TileEntity te = getTileEntity(world, x, y, z);
			if (te instanceof IInventory)
			{
				GrowthCraftCore.getLogger().info("Clearing Inventory");
				InventoryProcessor.instance().clearSlots((IInventory)te);
			}
			GrowthCraftCore.getLogger().info("Setting Block To Air");
			world.setBlockToAir(x, y, z);
		}
		else
		{
			fellBlockAsItem(world, x, y, z);
		}
	}

	@Override
	public boolean wrenchBlock(World world, int x, int y, int z, EntityPlayer player, ItemStack wrench)
	{
		if (player == null) return false;
		if (!ItemUtils.canWrench(wrench, player, x, y, z)) return false;
		if (!player.isSneaking()) return false;
		if (!world.isRemote)
		{
			fellBlockFromWrench(world, x, y, z);
			ItemUtils.wrenchUsed(wrench, player, x, y, z);
		}
		return true;
	}

	public boolean tryWrenchItem(EntityPlayer player, World world, int x, int y, int z)
	{
		if (player == null) return false;
		final ItemStack is = player.inventory.getCurrentItem();
		return wrenchBlock(world, x, y, z, player, is);
	}

	protected void placeBlockByEntityDirection(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (isRotatable(world, x, y, z, ForgeDirection.UNKNOWN))
		{
			final int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

			int meta = 2;

			if (l == 0) meta = 1;
			else if (l == 1) meta = 2;
			else if (l == 2) meta = 0;
			else if (l == 3) meta = 3;
			world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.SYNC);
		}
	}

	protected void setupCustomDisplayName(World world, int x, int y, int z, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			final TileEntity te = getTileEntity(world, x, y, z);
			if (te instanceof ICustomDisplayName)
			{
				((ICustomDisplayName)te).setGuiDisplayName(stack.getDisplayName());
			}
		}
	}

	protected NBTTagCompound getTileTagCompound(World world, int x, int y, int z, ItemStack stack)
	{
		final Item item = stack.getItem();
		if (item instanceof IItemTileBlock)
		{
			final IItemTileBlock itb = (IItemTileBlock)item;
			return itb.getTileTagCompound(stack);
		}
		else
		{
			GrowthCraftCore.getLogger().error("Cannot get tile tag compound for a non IItemTileBlock: stack=%s block=%s", stack, this);
		}
		return null;
	}

	protected void setTileTagCompound(World world, int x, int y, int z, ItemStack stack, NBTTagCompound tag)
	{
		final Item item = stack.getItem();
		if (item instanceof IItemTileBlock)
		{
			final IItemTileBlock itb = (IItemTileBlock)item;
			itb.setTileTagCompound(stack, tag);
		}
		else
		{
			GrowthCraftCore.getLogger().error("Cannot set tile tag compound for a non IItemTileBlock: stack=%s block=%s", stack, this);
		}
	}

	protected boolean shouldRestoreBlockState(World world, int x, int y, int z, ItemStack stack)
	{
		return false;
	}

	protected void restoreBlockStateFromStack(World world, int x, int y, int z, ItemStack stack)
	{
		if (shouldRestoreBlockState(world, x, y, z, stack))
		{
			final TileEntity te = getTileEntity(world, x, y, z);
			if (te instanceof INBTItemSerializable)
			{
				final NBTTagCompound tag = getTileTagCompound(world, x, y, z, stack);
				if (tag != null)
				{
					((INBTItemSerializable)te).readFromNBTForItem(tag);
				}
			}
			else
			{
				GrowthCraftCore.getLogger().error("Cannot restore tile from stack, the TileEntity does not support INBTItemSerializable: stack=%s block=%s tile=%s", stack, this, te);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		restoreBlockStateFromStack(world, x, y, z, stack);
		setupCustomDisplayName(world, x, y, z, stack);
	}

	protected void scatterInventory(World world, int x, int y, int z, Block block)
	{
		final TileEntity te = getTileEntity(world, x, y, z);
		if (te instanceof IInventory)
		{
			final IInventory inventory = (IInventory)te;
			if (inventory != null)
			{
				for (int index = 0; index < inventory.getSizeInventory(); ++index)
				{
					final ItemStack stack = inventory.getStackInSlot(index);
					if (stack != null)
					{
						ItemUtils.spawnItemStack(world, x, y, z, stack, rand);
					}
					inventory.setInventorySlotContents(index, (ItemStack)null);
				}
				world.func_147453_f(x, y, z, block);
			}
		}
	}

	protected boolean shouldScatterInventoryOnBreak(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		if (shouldScatterInventoryOnBreak(world, x, y, z))
			scatterInventory(world, x, y, z, block);
		world.removeTileEntity(x, y, z);
	}

	protected ItemStack createHarvestedBlockItemStack(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		return createStackedBlock(meta);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
		player.addExhaustion(0.025F);

		if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player))
		{
			final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			final ItemStack itemstack = createHarvestedBlockItemStack(world, player, x, y, z, meta);

			if (itemstack != null)
			{
				items.add(itemstack);
			}

			ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
			for (ItemStack is : items)
			{
				dropBlockAsItem(world, x, y, z, is);
			}
		}
		else
		{
			harvesters.set(player);
			final int fortune = EnchantmentHelper.getFortuneModifier(player);
			dropBlockAsItem(world, x, y, z, meta, fortune);
			harvesters.set(null);
		}
	}

	protected boolean shouldDropTileStack(World world, int x, int y, int z, int metadata, int fortune)
	{
		return false;
	}

	private void getDefaultDrops(List<ItemStack> ret, World world, int x, int y, int z, int metadata, int fortune)
	{
		final int count = quantityDropped(metadata, fortune, world.rand);
		for (int i = 0; i < count; ++i)
		{
			final Item item = getItemDropped(metadata, world.rand, fortune);
			if (item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(metadata)));
			}
		}
	}

	protected void getTileItemStackDrops(List<ItemStack> ret, World world, int x, int y, int z, int metadata, int fortune)
	{
		final TileEntity te = getTileEntity(world, x, y, z);
		if (te instanceof INBTItemSerializable)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			((INBTItemSerializable)te).writeToNBTForItem(tag);
			final ItemStack stack = new ItemStack(this, 1, metadata);
			setTileTagCompound(world, x, y, z, stack, tag);
			ret.add(stack);
		}
		else
		{
			getDefaultDrops(ret, world, x, y, z, metadata, fortune);
		}
	}

	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if (shouldDropTileStack(world, x, y, z, metadata, fortune))
		{
			getTileItemStackDrops(ret, world, x, y, z, metadata, fortune);
		}
		else
		{
			getDefaultDrops(ret, world, x, y, z, metadata, fortune);
		}
		return ret;
	}

	protected boolean playerFillTank(World world, int x, int y, int z, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		return Utils.playerFillTank(world, x, y, z, fh, is, player);
	}

	protected boolean playerDrainTank(World world, int x, int y, int z, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		final FluidStack fs = Utils.playerDrainTank(world, x, y, z, fh, is, player);
		return fs != null && fs.amount > 0;
	}

	private boolean handleIFluidHandler(World world, int x, int y, int z, EntityPlayer player, int meta)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFluidHandler)
		{
			if (world.isRemote)
			{
				return true;
			}
			else
			{
				final IFluidHandler fh = (IFluidHandler)te;
				final ItemStack is = player.inventory.getCurrentItem();

				boolean needUpdate = false;

				if (!player.isSneaking())
				{
					// While not sneaking, draining is given priority
					if (playerDrainTank(world, x, y, z, fh, is, player) ||
						playerFillTank(world, x, y, z, fh, is, player)) needUpdate = true;
				}
				else
				{
					// Otherwise filling is given priority
					if (playerFillTank(world, x, y, z, fh, is, player) ||
						playerDrainTank(world, x, y, z, fh, is, player)) needUpdate = true;
				}
				if (needUpdate)
				{
					world.markBlockForUpdate(x, y, z);
					return true;
				}
			}
		}
		return false;
	}

	private boolean handleOnUseItem(World world, int x, int y, int z, EntityPlayer player, int meta)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IItemHandler)
		{
			if (world.isRemote)
			{
				return true;
			}
			else
			{
				final IItemHandler ih = (IItemHandler)te;
				final ItemStack is = player.inventory.getCurrentItem();

				boolean needUpdate = false;
				if (ih.tryPlaceItem(player, is))
				{
					needUpdate = true;
				}
				else if (ih.tryTakeItem(player, is))
				{
					needUpdate = true;
				}

				if (needUpdate)
				{
					world.markBlockForUpdate(x, y, z);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (tryWrenchItem(player, world, x, y, z)) return true;
		if (handleIFluidHandler(world, x, y, z, player, meta)) return true;
		if (handleOnUseItem(world, x, y, z, player, meta)) return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntity(IBlockAccess world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null)
		{
			if (tileEntityType.isInstance(te))
			{
				return (T)te;
			}
			else
			{
				// warn
			}
		}
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int unused)
	{
		if (tileEntityType != null)
		{
			try
			{
				return tileEntityType.newInstance();
			}
			catch (InstantiationException e)
			{
				throw new IllegalStateException("Failed to create a new instance of an illegal class " + this.tileEntityType, e);
			}
			catch (IllegalAccessException e)
			{
				throw new IllegalStateException("Failed to create a new instance of " + this.tileEntityType + ", because lack of permissions", e);
			}
		}
		return null;
	}
}
