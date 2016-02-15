/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

/**
 * Utility class for handling some `painful` inventory operations
 */
public class InventoryProcessor
{
	private static final InventoryProcessor	inst = new InventoryProcessor();

	private InventoryProcessor() {}

	/**
	 * Are the provided slots empty?
	 *
	 * @param inv - inventory to check
	 * @param slots - slots to check
	 * @return true, the slots are empty, false otherwise
	 */
	public boolean slotsAreEmpty(@Nonnull IInventory inv, @Nonnull int[] slots)
	{
		for (int slot : slots)
		{
			if (inv.getStackInSlot(slot) != null) return false;
		}
		return true;
	}

	/**
	 * Merges the provided item into the inventory slot
	 *
	 * @param inv - inventory to merge in
	 * @param item - item stack to merge
	 * @param slot - slot to merge in
	 * @return true, the item was merged, false otherwise
	 */
	public boolean mergeWithSlot(@Nonnull IInventory inv, @Nullable ItemStack item, int slot)
	{
		if (item == null) return false;
		if (item.stackSize <= 0) return false;

		final ItemStack existing = inv.getStackInSlot(slot);
		if (existing == null || existing.stackSize <= 0)
		{
			inv.setInventorySlotContents(slot, item.copy());
			item.stackSize = 0;
		}
		else
		{
			if (existing.isItemEqual(item))
			{
				final int newSize = MathHelper.clamp_int(existing.stackSize + item.stackSize, 0, existing.getMaxStackSize());
				if (newSize == existing.stackSize)
				{
					return false;
				}
				else
				{
					final int consumed = newSize - existing.stackSize;
					item.stackSize -= consumed;
					existing.stackSize = newSize;
					inv.setInventorySlotContents(slot, existing);
				}
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	public boolean mergeWithSlots(@Nonnull IInventory inv, @Nullable ItemStack stack, int[] slots)
	{
		if (stack == null) return false;

		boolean anythingMerged = false;
		for (int slot : slots)
		{
			if (stack.stackSize <= 0) break;
			anythingMerged |= mergeWithSlot(inv, stack, slot);
		}
		return anythingMerged;
	}

	/**
	 * Attempts to merge the given ItemStack into the inventory
	 *
	 * @param inv - inventory to merge to
	 * @param stack -
	 * @param remaining stack OR null if the item was completely expeneded
	 */
	public ItemStack mergeWithSlots(@Nonnull IInventory inv, @Nullable ItemStack stack)
	{
		if (stack == null) return null;

		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if (stack.stackSize <= 0) break;
			mergeWithSlot(inv, stack, i);
		}
		return stack.stackSize <= 0 ? null : stack;
	}

	/**
	 * Attempts to clear the inventory slots
	 *
	 * @param inv - inventory to clear from
	 * @param src - slots to clear
	 * @return true, IF anything was remove from the slots, false otherwise
	 */
	public boolean clearSlots(@Nonnull IInventory inv, int[] src)
	{
		boolean clearedAnything = false;
		for (int slot : src)
		{
			clearedAnything |= inv.getStackInSlotOnClosing(slot) != null;
		}
		return clearedAnything;
	}

	/**
	 * Removes the item from the slot and returns it
	 *
	 * @param inv - inventory to yank from
	 * @param slot - slot to yank
	 * @return stack, ItemStack removed
	 */
	public ItemStack yankSlot(@Nonnull IInventory inv, int slot)
	{
		final ItemStack stack = inv.getStackInSlot(slot);
		if (stack != null)
		{
			return inv.decrStackSize(slot, stack.stackSize);
		}
		return null;
	}

	/**
	 * Checks the given inventory and slot for an expected ItemStack
	 *
	 * @param inv - inventory to check
	 * @param expected - itemstack expected
	 *   If the stack is null, then the slot is expected to be null as well
	 *   Otherwise it is required
	 * @param src - slot to check
	 */
	public boolean checkSlot(@Nonnull IInventory inv, @Nullable ItemStack expected, int src)
	{
		final ItemStack actual = inv.getStackInSlot(src);

		if (expected == null)
		{
			// if the item is not needed, and is not available
			if (actual != null) return false;
		}
		else
		{
			if (actual == null) return false;
			if (!expected.isItemEqual(actual)) return false;
			if (actual.stackSize < expected.stackSize) return false;
		}
		return true;
	}

	/**
	 * Checks a slice of slots for the given items
	 *
	 * @param inv - inventory to check
	 * @param filter - itemstacks to look for
	 * @param from - a slice of slots to look in
	 * @return true, all the items in the filter are present in the inv, false otherwise
	 */
	public boolean checkSlots(@Nonnull IInventory inv, @Nonnull ItemStack[] filter, int[] from)
	{
		assert filter.length == from.length;

		for (int i = 0; i < filter.length; ++i)
		{
			if (!checkSlot(inv, filter[i], from[i])) return false;
		}
		return true;
	}

	/**
	 * Checks a slice of slots for the given items
	 *
	 * @param inv - inventory to check
	 * @param filter - itemstacks to look for
	 * @param from - a slice of slots to look in
	 * @return true, all the items in the filter are present in the inv, false otherwise
	 */
	public boolean checkSlots(@Nonnull IInventory inv, @Nonnull List<ItemStack> filter, int[] from)
	{
		assert filter.size() == from.length;

		for (int i = 0; i < filter.size(); ++i)
		{
			if (!checkSlot(inv, filter.get(i), from[i])) return false;
		}
		return true;
	}

	/**
	 * @return true if moved, false otherwise
	 */
	public boolean moveToSlots(@Nonnull IInventory inv, @Nonnull ItemStack[] filter, int[] from, int[] to)
	{
		assert filter.length == from.length;
		assert filter.length == to.length;

		// first ensure that each stack in the input has the item and enough of them
		if (!checkSlots(inv, filter, from)) return false;

		for (int i = 0; i < filter.length; ++i)
		{
			if (filter[i] != null)
			{
				final ItemStack stack = inv.decrStackSize(from[i], filter[i].stackSize);
				inv.setInventorySlotContents(to[i], stack);
			}
		}
		return true;
	}

	/**
	 * Moves item from slot `from` to slot `to`
	 *
	 * @param inv - inventory to move items in
	 * @param filter - item to check for
	 * @param from - slot to move from
	 * @param to - slot to move to
	 * @return true, the item was moved, false otherwise
	 */
	public boolean moveToSlot(@Nonnull IInventory inv, @Nonnull ItemStack filter, int from, int to)
	{
		// first ensure that each stack in the input has the item and enough of them
		if (!checkSlot(inv, filter, from)) return false;

		final ItemStack stack = inv.decrStackSize(from, filter.stackSize);
		inv.setInventorySlotContents(to, stack);

		return true;
	}

	/**
	 * @param inv - source inventory to search
	 * @param query - item to search for
	 * @return -1 no slot was found, slot index otherwise
	 */
	public int findItemSlot(@Nonnull IInventory inv, @Nonnull Item query)
	{
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			final ItemStack stack = inv.getStackInSlot(i);
			if (stack != null)
			{
				if (stack.getItem() == query) return i;
			}
		}
		return -1;
	}

	/**
	 * @param inv - source inventory to search
	 * @param query - itemstack to search for
	 * @return -1 no slot was found, slot index otherwise
	 */
	public int findItemSlot(@Nonnull IInventory inv, @Nonnull ItemStack query)
	{
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			final ItemStack stack = inv.getStackInSlot(i);
			if (query.isItemEqual(stack)) return i;
		}
		return -1;
	}

	/**
	 * Search the inventory for all the items given, this method will skip
	 * slots that have been used before.
	 *
	 * @param inv - inventory to search
	 * @param stacks - item stacks to search for
	 * @return slot ids
	 */
	public int[] findItemSlots(@Nonnull IInventory inv, @Nonnull List<ItemStack> expected)
	{
		final boolean[] usedSlots = new boolean[inv.getSizeInventory()];
		final int[] slots = new int[expected.size()];
		int i = 0;
		for (ItemStack expectedStack : expected)
		{
			slots[i] = -1;
			for (int slotIndex = 0; slotIndex < inv.getSizeInventory(); ++slotIndex)
			{
				final ItemStack stack = inv.getStackInSlot(slotIndex);
				if (checkSlot(inv, expectedStack, slotIndex))
				{
					if (usedSlots[slotIndex]) continue;
					usedSlots[slotIndex] = true;
					slots[i] = findItemSlot(inv, stack);
					break;
				}
			}
			i++;
		}
		return slots;
	}

	/**
	 * Finds the next empty slot starting from the beginning
	 *
	 * @param inv - source inventory to search
	 * @return -1 no slot was found, slot index otherwise
	 */
	public int findNextEmpty(@Nonnull IInventory inv)
	{
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if (inv.getStackInSlot(i) == null) return i;
		}
		return -1;
	}

	/**
	 * Finds the next empty slot starting from the end of the inventory
	 *
	 * @param inv - source inventory to search
	 * @return -1 no slot was found, slot index otherwise
	 */
	public int findNextEmptyFromEnd(@Nonnull IInventory inv)
	{
		for (int i = inv.getSizeInventory() - 1; i >= 0; --i)
		{
			if (inv.getStackInSlot(i) == null) return i;
		}
		return -1;
	}

	/**
	 * Finds the next slot with an item present, starting from the beginning
	 *
	 * @param inv - source inventory to search
	 * @return -1 no slot was found, slot index otherwise
	 */
	public int findNextPresent(@Nonnull IInventory inv)
	{
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if (inv.getStackInSlot(i) != null) return i;
		}
		return -1;
	}

	/**
	 * Finds the next slot with an item present starting from the end of the inventory
	 *
	 * @param inv - source inventory to search
	 * @return -1 no slot was found, slot index otherwise
	 */
	public int findNextPresentFromEnd(@Nonnull IInventory inv)
	{
		for (int i = inv.getSizeInventory() - 1; i >= 0; --i)
		{
			if (inv.getStackInSlot(i) != null) return i;
		}
		return -1;
	}

	/**
	 * Consume items in the inventory.
	 *
	 * @param inv - source inventory to consume items from
	 * @param expected - items to consume in inventory
	 * @param slots - slots to consume from
	 * @return true, items where consumed, false otherwise
	 */
	public boolean consumeItemsInSlots(@Nonnull IInventory inv, @Nonnull List<ItemStack> expected, @Nonnull int[] slots)
	{
		for (int i = 0; i < slots.length; ++i)
		{
			final int slot = slots[i];
			final ItemStack expectedStack = expected.get(i);
			if (expectedStack != null)
			{
				inv.decrStackSize(slot, expectedStack.stackSize);
			}
		}
		return true;
	}

	/**
	 * Consume items in the inventory, this method will search for the slots.
	 *
	 * @param inv - source inventory to consume items from
	 * @param expected - items to consume in inventory
	 * @return true, items where consumed, false otherwise
	 */
	public boolean consumeItems(@Nonnull IInventory inv, @Nonnull List<ItemStack> expected)
	{
		final int[] slots = findItemSlots(inv, expected);
		if (!checkSlots(inv, expected, slots)) return false;
		return consumeItemsInSlots(inv, expected, slots);
	}

	public static InventoryProcessor instance()
	{
		return inst;
	}
}
