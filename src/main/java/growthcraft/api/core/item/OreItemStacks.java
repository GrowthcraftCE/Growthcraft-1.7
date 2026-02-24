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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.definition.IMultiItemStacks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreItemStacks implements IMultiItemStacks
{
	public int stackSize;
	private String oreName;

	public OreItemStacks(@Nonnull String name, int amount)
	{
		this.oreName = name;
		this.stackSize = amount;
	}

	public OreItemStacks(@Nonnull String name)
	{
		this(name, 1);
	}

	@Override
	public int getStackSize()
	{
		return stackSize;
	}

	public String getName()
	{
		return oreName;
	}

	public List<ItemStack> getRawItemStacks()
	{
		return OreDictionary.getOres(oreName);
	}

	@Override
	public boolean isEmpty()
	{
		return getRawItemStacks().isEmpty();
	}

	@Override
	public List<ItemStack> getItemStacks()
	{
		final List<ItemStack> items = getRawItemStacks();
		final List<ItemStack> result = new ArrayList<ItemStack>();
		for (ItemStack stack : items)
		{
			final ItemStack newStack = stack.copy();
			if (newStack.stackSize <= 0) newStack.stackSize = 1;
			newStack.stackSize *= stackSize;
			result.add(newStack);
		}
		return result;
	}

	@Override
	public boolean containsItemStack(@Nullable ItemStack stack)
	{
		if (ItemTest.isValid(stack))
		{
			final int oreId = OreDictionary.getOreID(oreName);
			for (int i : OreDictionary.getOreIDs(stack))
			{
				if (i == oreId) return true;
			}
		}
		return false;
	}
}
