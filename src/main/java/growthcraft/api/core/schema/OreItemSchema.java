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
package growthcraft.api.core.schema;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.core.definition.IItemStackListProvider;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreItemSchema implements IItemStackListProvider, IValidatable, ICommentable
{
	public String comment = "";
	public String name;
	public int amount;

	/**
	 * @param n - ore name
	 * @param a - amount
	 */
	public OreItemSchema(String n, int a)
	{
		this.name = n;
		this.amount = a;
	}

	public OreItemSchema() {}

	@Override
	public void setComment(String comm)
	{
		this.comment = comm;
	}

	@Override
	public String getComment()
	{
		return comment;
	}

	/**
	 * @return list of ores or null if the name was invalid
	 */
	public List<ItemStack> getOres()
	{
		if (name != null) return OreDictionary.getOres(name);
		return null;
	}

	/**
	 * @return list with ores, this list may be empty if the ores were invalid
	 */
	@Override
	public List<ItemStack> getItemStacks()
	{
		final List<ItemStack> result = new ArrayList<ItemStack>();
		final List<ItemStack> ores = getOres();
		if (ores != null)
		{
			for (ItemStack stack : ores)
			{
				final ItemStack newStack = stack.copy();
				newStack.stackSize = amount;
				result.add(newStack);
			}
		}
		return result;
	}

	@Override
	public String toString()
	{
		return String.format("Schema<OreItem>(name: '%s', amount: %s)", name, amount);
	}

	@Override
	public boolean isValid()
	{
		return name != null;
	}

	@Override
	public boolean isInvalid()
	{
		return !isValid();
	}
}
