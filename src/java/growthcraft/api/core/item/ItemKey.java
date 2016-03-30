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
package growthcraft.api.core.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.definition.IItemStackFactory;
import growthcraft.api.core.nbt.NBTHelper;
import growthcraft.api.core.util.HashKey;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

/**
 * As the name implies, this class is used in place of a List for Item keys
 */
public class ItemKey extends HashKey implements IItemStackFactory
{
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;

	public final Item item;
	public final int meta;
	public final NBTTagCompound compoundTag;

	public ItemKey(@Nonnull Item pitem, int pmeta, @Nullable NBTTagCompound tag)
	{
		super();
		this.item = pitem;
		this.meta = pmeta;
		this.compoundTag = NBTHelper.compoundTagPresence(tag);
		generateHashCode();
	}

	public ItemKey(@Nonnull Item pitem, int pmeta)
	{
		this(pitem, pmeta, null);
	}

	public ItemKey(@Nonnull Block block, int pmeta, @Nullable NBTTagCompound tag)
	{
		super();
		final Item pitem = Item.getItemFromBlock(block);
		if (pitem == null)
		{
			throw new IllegalArgumentException("Invalid Block given for ItemKey (block=" + block + " meta=" + pmeta + ")");
		}
		this.item = pitem;
		this.meta = pmeta;
		this.compoundTag = NBTHelper.compoundTagPresence(tag);
		generateHashCode();
	}

	public ItemKey(@Nonnull Block block, int pmeta)
	{
		this(block, pmeta, null);
	}

	public ItemKey(@Nonnull ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage(), stack.getTagCompound());
	}

	private void generateHashCode()
	{
		this.hash = item.hashCode();
		this.hash = 31 * hash + meta;
		this.hash = 31 * hash + (compoundTag != null ? compoundTag.hashCode() : 0);
	}

	@Override
	public ItemStack asStack(int size)
	{
		final ItemStack result = new ItemStack(item, size, meta);
		result.setTagCompound(NBTHelper.copyCompoundTag(compoundTag));
		return result;
	}

	@Override
	public ItemStack asStack()
	{
		return asStack(1);
	}

	public static ItemKey newWoNBT(@Nonnull ItemStack stack)
	{
		return new ItemKey(stack.getItem(), stack.getItemDamage());
	}
}
