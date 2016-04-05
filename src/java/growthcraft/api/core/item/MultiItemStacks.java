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
package growthcraft.api.core.item;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.definition.IMultiItemStacks;

import net.minecraft.item.ItemStack;

public class MultiItemStacks implements IMultiItemStacks
{
	private List<ItemStack> itemStacks;

	public MultiItemStacks(@Nonnull ItemStack... stacks)
	{
		this.itemStacks = Arrays.asList(stacks);
	}

	@Override
	public boolean isEmpty()
	{
		return itemStacks.isEmpty();
	}

	@Override
	public int getStackSize()
	{
		for (ItemStack stack : itemStacks)
		{
			return stack.stackSize;
		}
		return 0;
	}

	@Override
	public List<ItemStack> getItemStacks()
	{
		return itemStacks;
	}

	@Override
	public boolean containsItemStack(@Nullable ItemStack stack)
	{
		if (!ItemTest.isValid(stack)) return false;
		for (ItemStack content : getItemStacks())
		{
			if (content.isItemEqual(stack)) return true;
		}
		return false;
	}
}
