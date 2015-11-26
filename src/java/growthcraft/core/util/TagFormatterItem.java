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
package growthcraft.core.util;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

/**
 * Tag Formatter for item NBT data
 */
public class TagFormatterItem implements ITagFormatter
{
	public static final TagFormatterItem INSTANCE = new TagFormatterItem();

	public String formatItem(NBTTagCompound tag)
	{
		final int id = tag.getInteger("id");
		if (id == ConstID.NO_ITEM)
		{
			return UnitFormatter.noItem();
		}
		else
		{
			final ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
			if (stack != null)
			{
				return EnumChatFormatting.WHITE + GrcI18n.translate("grc.format.itemslot.item", stack.getDisplayName(), stack.stackSize);
			}
			else
			{
				return UnitFormatter.invalidItem();
			}
		}
	}

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(formatItem(tag));
		return list;
	}
}
