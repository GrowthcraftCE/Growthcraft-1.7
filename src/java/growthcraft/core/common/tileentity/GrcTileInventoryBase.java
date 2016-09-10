/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.core.common.tileentity;

import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.IInventoryWatcher;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.tileentity.event.TileEventHandler;
import growthcraft.core.common.tileentity.feature.ICustomDisplayName;
import growthcraft.core.util.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Extend this base class if you want a Tile with an `Inventory`
 */
public abstract class GrcTileInventoryBase extends GrcTileBase implements ISidedInventory, ICustomDisplayName, IInventoryWatcher
{
	protected static final int[] NO_SLOTS = new int[]{};

	protected String inventoryName;
	protected GrcInternalInventory inventory;

	public GrcTileInventoryBase()
	{
		super();
		this.inventory = createInventory();
	}

	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 0);
	}

	public String getDefaultInventoryName()
	{
		return "grc.inventory.name";
	}

	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		markDirty();
	}

	@Override
	public void onItemDiscarded(IInventory inv, ItemStack stack, int index, int discardedAmount)
	{
		final ItemStack discarded = stack.copy();
		discarded.stackSize = discardedAmount;
		ItemUtils.spawnItemStack(worldObj, xCoord, yCoord, zCoord, discarded, worldObj.rand);
	}

	@Override
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? inventoryName : getDefaultInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return inventoryName != null && inventoryName.length() > 0;
	}

	@Override
	public void setGuiDisplayName(String string)
	{
		this.inventoryName = string;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}

	public ItemStack tryMergeItemIntoSlot(ItemStack itemstack, int index)
	{
		final ItemStack result = ItemUtils.mergeStacksBang(getStackInSlot(index), itemstack);
		if (result != null)
		{
			inventory.setInventorySlotContents(index, result);
		}
		return result;
	}

	// Attempts to merge the given itemstack into the main slot
	public ItemStack tryMergeItemIntoMainSlot(ItemStack itemstack)
	{
		return tryMergeItemIntoSlot(itemstack, 0);
	}

	@Override
	public ItemStack decrStackSize(int index, int par2)
	{
		return inventory.decrStackSize(index, par2);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return inventory.getStackInSlotOnClosing(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		inventory.setInventorySlotContents(index, itemstack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}
		return player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return inventory.isItemValidForSlot(slot, itemstack);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		return InventoryProcessor.instance().canInsertItem(this, stack, slot);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		return InventoryProcessor.instance().canExtractItem(this, stack, slot);
	}

	public int[] getAccessibleSlotsFromSide(int side)
	{
		return NO_SLOTS;
	}

	private void readInventoryFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("items"))
		{
			inventory.readFromNBT(nbt, "items");
		}
		else if (nbt.hasKey("inventory"))
		{
			inventory.readFromNBT(nbt, "inventory");
		}
	}

	private void readInventoryNameFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("name"))
		{
			this.inventoryName = nbt.getString("name");
		}
		else if (nbt.hasKey("inventory_name"))
		{
			this.inventoryName = nbt.getString("inventory_name");
		}
	}

	@Override
	public void readFromNBTForItem(NBTTagCompound nbt)
	{
		super.readFromNBTForItem(nbt);
		readInventoryFromNBT(nbt);
		// Do not reload the inventory name from NBT, allow the ItemStack to do that
		//readInventoryNameFromNBT(nbt);
	}

	@TileEventHandler(event=TileEventHandler.EventType.NBT_READ)
	public void readFromNBT_Inventory(NBTTagCompound nbt)
	{
		readInventoryFromNBT(nbt);
		readInventoryNameFromNBT(nbt);
	}

	private void writeInventoryToNBT(NBTTagCompound nbt)
	{
		inventory.writeToNBT(nbt, "inventory");
		// NAME
		if (hasCustomInventoryName())
		{
			nbt.setString("inventory_name", inventoryName);
		}
		nbt.setInteger("inventory_tile_version", 3);
	}

	@Override
	public void writeToNBTForItem(NBTTagCompound nbt)
	{
		super.writeToNBTForItem(nbt);
		writeInventoryToNBT(nbt);
	}

	@TileEventHandler(event=TileEventHandler.EventType.NBT_WRITE)
	public void writeToNBT_Inventory(NBTTagCompound nbt)
	{
		writeInventoryToNBT(nbt);
	}
}
