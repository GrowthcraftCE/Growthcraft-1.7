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
package growthcraft.core.common.tileentity.device;

import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.util.ItemUtils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DeviceInventorySlot
{
	public final int index;
	private IInventory inventory;

	public DeviceInventorySlot(IInventory inv, int idx)
	{
		this.inventory = inv;
		this.index = idx;
	}

	/**
	 * Returns the current item in the slot, if any
	 *
	 * @return stack
	 */
	public ItemStack get()
	{
		return inventory.getStackInSlot(index);
	}

	/**
	 * The size of the ItemStack in the slot, if none is present the size is 0
	 *
	 * @return size
	 */
	public int getSize()
	{
		final ItemStack stack = get();
		if (stack == null) return 0;
		return stack.stackSize;
	}

	/**
	 * Returns the total capacity of the slot.
	 * If an item occupies the slot then the stack's maxStackSize is returned
	 *
	 * @return capacity
	 */
	public int getCapacity()
	{
		final ItemStack stack = get();
		if (stack != null)
		{
			return stack.getMaxStackSize();
		}
		return inventory.getInventoryStackLimit();
	}

	/**
	 * Returns the current available capacity, that is the maxCapactity - current size
	 *
	 * @return available capacity
	 */
	public int getAvailableCapacity()
	{
		return getCapacity() - getSize();
	}

	/**
	 * Set the stack for this slot
	 *
	 * @param newStack the stack to set
	 */
	public void set(ItemStack newStack)
	{
		inventory.setInventorySlotContents(index, newStack);
	}

	/**
	 * Is there any VALID item in this slot, where valid is an item with
	 * a stackSize greater than 0
	 *
	 * @return true there is content, false otherwise
	 */
	public boolean hasContent()
	{
		return getSize() > 0;
	}

	/**
	 * Does the slot not have an item?
	 *
	 * @return true, there is no item, or no valid item in the slot
	 */
	public boolean isEmpty()
	{
		return !hasContent();
	}

	/**
	 * Is this slot full?
	 *
	 * @return true, the slot is full, false otherwise
	 */
	public boolean isFull()
	{
		return getAvailableCapacity() <= 0;
	}

	/**
	 * Does the provided stack match the one in the slot?
	 *
	 * @param stack - item stack to test
	 * @return true, it has the same item, false otherwise
	 */
	public boolean hasMatching(ItemStack stack)
	{
		final ItemStack s = get();
		if (stack == null)
		{
			return s == null;
		}
		else if (stack != null && s != null)
		{
			return stack.isItemEqual(s);
		}
		return false;
	}

	/**
	 * Does the provided multi stack match the one in the slot?
	 *
	 * @param stack - multi item stack to test
	 * @return true, it contains the an item from the stack, false otherwise
	 */
	public boolean hasMatching(IMultiItemStacks stack)
	{
		final ItemStack s = get();
		if (stack == null || stack.isEmpty())
		{
			return s == null;
		}
		else if (stack != null && s != null)
		{
			return stack.containsItemStack(s);
		}
		return false;
	}

	/**
	 * Does the slot have the same item, and the capacity to hold the stack?
	 *
	 * @param stack - item stack to test
	 * @return true, it has the same item and has capacity, false otherwise
	 */
	public boolean hasMatchingWithCapacity(ItemStack stack)
	{
		if (stack == null) return true;
		if (!isEmpty())
		{
			if (!hasMatching(stack)) return false;
		}
		return getAvailableCapacity() >= stack.stackSize;
	}

	/**
	 * This is a variation of hasMatchingWithCapacity, this version will accept
	 * the stack if the internal stack is null unlike the former.
	 *
	 * @param stack - item stack to test
	 * @return true, the slot has capacity for the provided stack
	 */
	public boolean hasCapacityFor(ItemStack stack)
	{
		if (stack == null) return true;
		if (hasContent())
		{
			if (!hasMatching(stack)) return false;
		}
		return getAvailableCapacity() >= stack.stackSize;
	}

	/**
	 * @param stack - item stack to test
	 * @return true, the item matches and has enough in slot
	 */
	public boolean hasEnough(ItemStack stack)
	{
		if (hasMatching(stack))
		{
			if (stack != null)
			{
				final ItemStack s = get();
				if (s.stackSize >= stack.stackSize)
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param stack - item stack to test
	 * @return true, the item matches and has enough in slot
	 */
	public boolean hasEnough(IMultiItemStacks stack)
	{
		if (hasMatching(stack))
		{
			if (stack != null)
			{
				final ItemStack s = get();
				if (s.stackSize >= stack.getStackSize())
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param count - how many items to consume in the slot?
	 * @param result stack
	 */
	public ItemStack consume(int count)
	{
		return inventory.decrStackSize(index, count);
	}

	public ItemStack consume(ItemStack stack)
	{
		if (hasEnough(stack)) return consume(stack.stackSize);
		return null;
	}

	public ItemStack consume(IMultiItemStacks stack)
	{
		if (hasEnough(stack)) return consume(stack.getStackSize());
		return null;
	}

	public ItemStack increaseStack(ItemStack stack)
	{
		final ItemStack result = ItemUtils.mergeStacks(get(), stack);
		if (result != null)
		{
			set(result);
			return result;
		}
		return null;
	}

	/**
	 * Removes and returns the item in the slot
	 *
	 * @return stack if any
	 */
	public ItemStack yank()
	{
		return InventoryProcessor.instance().yankSlot(inventory, index);
	}
}
