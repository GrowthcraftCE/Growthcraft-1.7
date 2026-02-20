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

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.nbt.NBTHelper;
import growthcraft.core.common.item.IItemTileBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBlockHangingCurds extends ItemBlock implements IItemTileBlock
{
	public ItemBlockHangingCurds(Block block)
	{
		super(block);
		setHasSubtypes(true);
	}

	private NBTTagCompound getTileTagCompoundABS(ItemStack stack)
	{
		final NBTTagCompound tag = NBTHelper.openItemStackTag(stack);
		if (!tag.hasKey("te_curd_block"))
		{
			final NBTTagCompound curdTag = new NBTTagCompound();
			final EnumCheeseType cheeseType = EnumCheeseType.getSafeById(stack.getItemDamage());
			cheeseType.writeToNBT(curdTag);
			tag.setTag("te_curd_block", curdTag);
		}
		return tag.getCompoundTag("te_curd_block");
	}

	public EnumCheeseType getCheeseType(ItemStack stack)
	{
		final NBTTagCompound tag = getTileTagCompoundABS(stack);
		return EnumCheeseType.loadFromNBT(tag);
	}

	@Override
	public void setTileTagCompound(ItemStack stack, NBTTagCompound tileTag)
	{
		final NBTTagCompound tag = NBTHelper.openItemStackTag(stack);
		tag.setTag("te_curd_block", tileTag);
	}

	@Override
	public NBTTagCompound getTileTagCompound(ItemStack stack)
	{
		final NBTTagCompound tag = getTileTagCompoundABS(stack);
		final EnumCheeseType type = getCheeseType(stack);
		if (stack.getItemDamage() != type.meta)
		{
			stack.setItemDamage(type.meta);
		}
		return tag;
	}

	public boolean isDried(ItemStack stack)
	{
		final NBTTagCompound nbt = getTileTagCompound(stack);
		if (nbt.hasKey("dried"))
		{
			return nbt.getBoolean("dried");
		}
		return false;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String str = super.getUnlocalizedName(stack);
		str += "." + getCheeseType(stack).name;
		if (isDried(stack)) str += ".dried";
		return str;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		final NBTTagCompound nbt = getTileTagCompound(stack);
		if (nbt.hasKey("dried") && nbt.getBoolean("dried"))
		{
			list.add(GrcI18n.translate("grcmilk.hanging_curds.dried"));
		}
		else
		{
			final int age = nbt.getInteger("age");
			if (age > 0)
			{
				final int ageMax = nbt.getInteger("age_max");
				final int t = age * 100 / (ageMax > 0 ? ageMax : 1200);
				list.add(GrcI18n.translate("grcmilk.hanging_curds.drying.prefix") +
					GrcI18n.translate("grcmilk.hanging_curds.drying.progress.format", t));
			}
		}
		super.addInformation(stack, player, list, bool);
	}

	public static NBTTagCompound openNBT(ItemStack stack)
	{
		final Item item = stack.getItem();
		if (item instanceof ItemBlockHangingCurds)
		{
			return ((ItemBlockHangingCurds)item).getTileTagCompound(stack);
		}
		else
		{
			// throw error
		}
		return null;
	}
}
