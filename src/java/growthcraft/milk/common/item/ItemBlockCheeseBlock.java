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

public class ItemBlockCheeseBlock extends ItemBlock
{
	public ItemBlockCheeseBlock(Block block)
	{
		super(block);
		this.maxStackSize = 1;
	}

	private NBTTagCompound getTileDataABS(ItemStack stack)
	{
		final NBTTagCompound tag = NBTHelper.openItemStackTag(stack);
		if (!tag.hasKey("te_cheese_block"))
		{
			final NBTTagCompound cheeseTag = new NBTTagCompound();
			final EnumCheeseType cheese = EnumCheeseType.getSafeById(stack.getItemDamage());
			cheese.writeToNBT(cheeseTag);
			cheese.stages.get(0).writeToNBT(cheeseTag);
			tag.setTag("te_cheese_block", cheeseTag);
		}
		return tag.getCompoundTag("te_cheese_block");
	}

	public NBTTagCompound getTileData(ItemStack stack)
	{
		final NBTTagCompound tag = getTileDataABS(stack);
		final EnumCheeseType type = getCheeseType(stack);
		if (stack.getItemDamage() != type.meta)
		{
			stack.setItemDamage(type.meta);
		}
		return tag;
	}

	public EnumCheeseType getCheeseType(ItemStack stack)
	{
		final NBTTagCompound tag = getTileDataABS(stack);
		return EnumCheeseType.loadFromNBT(tag);
	}

	public EnumCheeseStage getCheeseStage(ItemStack stack)
	{
		final NBTTagCompound tag = getTileDataABS(stack);
		return EnumCheeseStage.loadFromNBT(tag);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) +
			"." + getCheeseType(stack).name +
			"." + getCheeseStage(stack).name;
	}

	public int getSlices(ItemStack stack)
	{
		return getTileData(stack).getInteger("slices");
	}

	public int getSlicesMax(ItemStack stack)
	{
		return getTileData(stack).getInteger("slices_max");
	}

	public static NBTTagCompound openNBT(ItemStack stack)
	{
		final Item item = stack.getItem();
		if (item instanceof ItemBlockCheeseBlock)
		{
			return ((ItemBlockCheeseBlock)item).getTileData(stack);
		}
		else
		{
			// throw error
		}
		return null;
	}
}
