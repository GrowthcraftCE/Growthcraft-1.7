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
package growthcraft.core.common.inventory;

import growthcraft.api.core.nbt.NBTHelper;
import growthcraft.core.util.ItemUtils;
import growthcraft.api.core.nbt.INBTSerializableContext;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;

public class GrcInternalInventory implements IInventory, INBTSerializableContext
{
	public static final int WILDCARD_SLOT = -1;

	protected ItemStack[] items;
	protected int maxSize;
	protected int maxStackSize;
	protected Object parent;

	public GrcInternalInventory(Object par, int size, int maxStack)
	{
		this.parent = par;
		this.maxSize = size;
		this.maxStackSize = maxStack;
		this.items = new ItemStack[maxSize];
	}

	public GrcInternalInventory(Object par, int size)
	{
		this(par, size, 64);
	}

	public int getMaxSize()
	{
		return maxSize;
	}

	protected void onSlotChanged(int index)
	{
		if (parent instanceof IInventoryWatcher)
		{
			((IInventoryWatcher)parent).onInventoryChanged(this, index);
		}
		else if (parent instanceof IInventory)
		{
			((IInventory)parent).markDirty();
		}
	}

	@Override
	public void markDirty()
	{
		onSlotChanged(WILDCARD_SLOT);
	}

	public void clearInventory()
	{
		for (int i = 0; i < getMaxSize(); ++i)
		{
			items[i] = null;
		}
		onSlotChanged(WILDCARD_SLOT);
	}

	protected void readFromNBT(NBTTagList data)
	{
		this.items = ItemUtils.clearInventorySlots(items, getSizeInventory());
		NBTHelper.readInventorySlotsFromNBT(items, data);
		onSlotChanged(WILDCARD_SLOT);
	}

	@Override
	public void readFromNBT(NBTTagCompound data, String name)
	{
		final NBTTagList list = data.getTagList(name, NBTHelper.NBTType.COMPOUND.id);
		if (list != null)
		{
			readFromNBT(list);
		}
		else
		{
			// LOG error
		}
	}

	protected void writeToNBT(NBTTagList data)
	{
		NBTHelper.writeInventorySlotsToNBT(items, data);
	}

	@Override
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagList invData = new NBTTagList();
		writeToNBT(invData);
		data.setTag(name, invData);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
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
	public boolean isUseableByPlayer(EntityPlayer _player)
	{
		return true;
	}

	@Override
	public int getSizeInventory()
	{
		return maxSize;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return maxStackSize;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public String getInventoryName()
	{
		return "grc.inventory.internal";
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return items[index];
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		final ItemStack oldStack = items[index];
		items[index] = stack;
		if (stack != null)
		{
			if (stack.stackSize > getInventoryStackLimit())
			{
				final int discarded = stack.stackSize - getInventoryStackLimit();
				items[index].stackSize = getInventoryStackLimit();
				if (discarded > 0)
				{
					if (parent instanceof IInventoryWatcher)
					{
						((IInventoryWatcher)parent).onItemDiscarded(this, stack, index, discarded);
					}
				}
			}
		}
		if (oldStack != stack)
		{
			onSlotChanged(index);
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		final ItemStack stack = items[index];
		items[index] = null;
		if (stack != null) onSlotChanged(index);
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int index, int amount)
	{
		if (items[index] != null)
		{
			ItemStack itemstack;

			if (items[index].stackSize <= amount)
			{
				itemstack = items[index];
				items[index] = null;
			}
			else
			{
				itemstack = items[index].splitStack(amount);

				if (items[index].stackSize <= 0)
				{
					items[index] = null;
				}
			}
			onSlotChanged(index);
			return itemstack;
		}
		return null;
	}
}
