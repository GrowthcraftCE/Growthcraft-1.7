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
package growthcraft.api.core.util;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

/**
 * As the name implies, this class is used in place of a List for Item keys
 */
public class ItemKey extends HashKey
{
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;

	public final Item item;
	public final int meta;

	public ItemKey(@Nonnull Item iitem, int imeta)
	{
		super();
		this.item = iitem;
		this.meta = imeta;
		generateHashCode();
	}

	public ItemKey(@Nonnull Block block, int imeta)
	{
		super();
		final Item iitem = Item.getItemFromBlock(block);
		if (iitem == null)
		{
			throw new IllegalArgumentException("Invalid Block given for ItemKey (block=" + block + " meta=" + imeta + ")");
		}
		this.item = iitem;
		this.meta = imeta;
		generateHashCode();
	}

	public ItemKey(@Nonnull ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage());
	}

	public void generateHashCode()
	{
		this.hash = item.hashCode();
		this.hash = 31 * hash + meta;
	}
}
