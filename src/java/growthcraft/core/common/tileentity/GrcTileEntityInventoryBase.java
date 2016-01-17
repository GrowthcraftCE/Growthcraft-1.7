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

import java.util.Random;

import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.IInventoryWatcher;
import growthcraft.core.common.inventory.IInventoryFlagging;
import growthcraft.core.util.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Extend this base class if you want a Tile with an `Inventory`
 */
public abstract class GrcTileEntityInventoryBase extends GrcTileEntityBase implements ISidedInventory, ICustomDisplayName, IInventoryWatcher, IInventoryFlagging
{
	protected String name;
	protected GrcInternalInventory inventory;
	protected boolean needInventoryUpdate;
	protected Random random = new Random();

	public GrcTileEntityInventoryBase()
	{
		super();

		this.inventory = createInventory();
	}

	protected abstract GrcInternalInventory createInventory();
	public abstract String getDefaultInventoryName();

	// Call this when you modified the inventory, or your not sure what
	// kind of update you require
	@Override
	public void markForInventoryUpdate()
	{
		needInventoryUpdate = true;
	}

	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		markForInventoryUpdate();
	}

	@Override
	public void onItemDiscarded(IInventory inv, ItemStack stack, int index)
	{
		ItemUtils.spawnItemStack(worldObj, xCoord, yCoord, zCoord, stack, random);
	}

	protected void checkUpdateFlags()
	{
		if (needInventoryUpdate)
		{
			needInventoryUpdate = false;
			this.markDirty();
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		checkUpdateFlags();
	}

	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.name : getDefaultInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return this.name != null && this.name.length() > 0;
	}


	public void setGuiDisplayName(String string)
	{
		this.name = string;
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
		if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
		{
			return false;
		}
		return player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return inventory.isItemValidForSlot(index, itemstack);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return isItemValidForSlot(index, stack);
	}

	@Override
	public abstract int[] getAccessibleSlotsFromSide(int side);

	@Override
	public abstract boolean canExtractItem(int index, ItemStack stack, int side);

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		inventory.readFromNBT(nbt, "items");

		if (nbt.hasKey("name"))
		{
			this.name = nbt.getString("name");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		inventory.writeToNBT(nbt, "items");

		// NAME
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}
		nbt.setInteger("BaseInventoryTile.version", 2);
	}
}
