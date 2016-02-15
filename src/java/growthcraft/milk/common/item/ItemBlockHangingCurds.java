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
package growthcraft.milk.common.item;

import growthcraft.api.core.nbt.NBTHelper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBlockHangingCurds extends ItemBlock
{
	public ItemBlockHangingCurds(Block block)
	{
		super(block);
	}

	public NBTTagCompound getTileData(ItemStack stack)
	{
		final NBTTagCompound tag = NBTHelper.openItemStackTag(stack);
		if (!tag.hasKey("te_curd_block"))
		{
			final NBTTagCompound curdTag = new NBTTagCompound();
			final EnumCheeseType curd = EnumCheeseType.getSafeById(stack.getItemDamage());
			curd.writeToNBT(curdTag);
			tag.setTag("te_curd_block", curdTag);
		}
		return tag.getCompoundTag("te_curd_block");
	}

	public EnumCheeseType getCheeseType(ItemStack stack)
	{
		final NBTTagCompound tag = getTileData(stack);
		return EnumCheeseType.loadFromNBT(tag);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) +
			"." + getCheeseType(stack).name;
	}

	public static NBTTagCompound openNBT(ItemStack stack)
	{
		final Item item = stack.getItem();
		if (item instanceof ItemBlockHangingCurds)
		{
			return ((ItemBlockHangingCurds)item).getTileData(stack);
		}
		else
		{
			// throw error
		}
		return null;
	}
}
