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
package growthcraft.api.core.nbt;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.util.ConstID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class NBTHelper
{
	private NBTHelper() {}

	public static int getInteger(@Nonnull NBTBase tag)
	{
		return tag != null && (tag instanceof NBTBase.NBTPrimitive) ? ((NBTBase.NBTPrimitive)tag).func_150287_d() : 0;
	}

	public static int getInteger(@Nonnull NBTTagCompound tag, String name)
	{
		return getInteger(tag.getTag(name));
	}

	/**
	 * Determines if the provided compound tag is nonnull AND actually contains
	 * tags.
	 *
	 * @param tag - compound tag to check
	 * @return null if the tag was null or empty, the tag otherwise
	 */
	public static NBTTagCompound compoundTagPresence(@Nullable NBTTagCompound tag)
	{
		if (tag != null && !tag.hasNoTags())
		{
			return tag;
		}
		return null;
	}

	@SuppressWarnings({"unchecked"})
	public static NBTTagCompound copyCompoundTag(@Nonnull NBTTagCompound tag)
	{
		final NBTBase newTag = tag.copy();
		return (NBTTagCompound)newTag;
	}

	public static NBTTagCompound openItemStackTag(@Nonnull ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null)
		{
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		return tag;
	}

	public static NBTTagList writeInventorySlotsToNBT(@Nonnull ItemStack[] invSlots, @Nonnull NBTTagList invTags)
	{
		for (int i = 0; i < invSlots.length; ++i)
		{
			if (invSlots[i] != null)
			{
				final NBTTagCompound slotTag = new NBTTagCompound();
				slotTag.setByte("slot_id", (byte)i);
				invSlots[i].writeToNBT(slotTag);
				invTags.appendTag(slotTag);
			}
		}
		return invTags;
	}

	public static NBTTagList writeInventorySlotsToNBT(@Nonnull ItemStack[] invSlots)
	{
		return writeInventorySlotsToNBT(invSlots, new NBTTagList());
	}

	public static NBTTagList readInventorySlotsFromNBT(@Nonnull ItemStack[] invSlots, @Nonnull NBTTagList tags)
	{
		for (int i = 0; i < tags.tagCount(); ++i)
		{
			final NBTTagCompound itemTag = tags.getCompoundTagAt(i);
			final byte slotID = itemTag.getByte("slot_id");
			if (slotID >= 0 && slotID < invSlots.length)
			{
				invSlots[slotID] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}
		return tags;
	}

	public static NBTTagCompound writeIFluidHandlerToNBT(@Nonnull IFluidHandler fluidHandler, @Nonnull NBTTagCompound tag)
	{
		final NBTTagList tankTagList = new NBTTagList();
		int tankId = 0;
		for (FluidTankInfo tankInfo : fluidHandler.getTankInfo(ForgeDirection.UNKNOWN))
		{
			final NBTTagCompound tankTag = new NBTTagCompound();
			tankTag.setInteger("tank_id", tankId);
			tankTag.setInteger("capacity", tankInfo.capacity);
			final FluidStack fluidStack = tankInfo.fluid;
			if (fluidStack != null)
			{
				tankTag.setInteger("fluid_id", tankInfo.fluid.getFluidID());
				final NBTTagCompound fluidTag = new NBTTagCompound();
				fluidStack.writeToNBT(fluidTag);
				tankTag.setTag("fluid", fluidTag);
			}
			else
			{
				tankTag.setInteger("fluid_id", ConstID.NO_FLUID);
			}
			tankTagList.appendTag(tankTag);
			++tankId;
		}
		tag.setTag("tanks", tankTagList);
		tag.setInteger("tank_count", tankId);
		return tag;
	}

	public static NBTTagCompound writeItemStackToNBT(@Nullable ItemStack itemStack, @Nonnull NBTTagCompound tag)
	{
		if (itemStack != null)
		{
			itemStack.writeToNBT(tag);
		}
		else
		{
			tag.setInteger("id", ConstID.NO_ITEM);
		}
		return tag;
	}

	public static NBTTagCompound writeItemStackToNBT(@Nullable ItemStack itemStack)
	{
		return writeItemStackToNBT(itemStack, new NBTTagCompound());
	}

	public static NBTTagCompound writeEffectsList(@Nonnull NBTTagCompound data, @Nonnull List<IEffect> list)
	{
		data.setInteger("size", list.size());
		final NBTTagList effectsList = new NBTTagList();
		for (IEffect effect : list)
		{
			final NBTTagCompound item = new NBTTagCompound();
			effect.writeToNBT(item, "value");
			effectsList.appendTag(item);
		}
		data.setTag("effects", effectsList);
		return data;
	}

	public static void loadEffectsList(@Nonnull List<IEffect> list, @Nonnull NBTTagCompound data)
	{
		final int size = data.getInteger("size");
		final NBTTagList effectsList = (NBTTagList)data.getTag("effects");
		for (int i = 0; i < size; ++i)
		{
			final NBTTagCompound effectData = effectsList.getCompoundTagAt(i);
			final IEffect effect = CoreRegistry.instance().getEffectsRegistry().loadEffectFromNBT(effectData, "value");
			list.add(effect);
		}
	}

	/**
	 * Writes the given collection to the NBTTagCompound as an IntArray
	 *
	 * @param data - target NBTTagCompound
	 *   Will add `size: int` and `data: NBTTagIntArray` fields
	 * @param coll - the collection to write
	 */
	public static void writeIntegerCollection(@Nonnull NBTTagCompound data, @Nonnull Collection<Integer> coll)
	{
		data.setInteger("size", coll.size());
		final int[] ary = new int[coll.size()];
		int i = 0;
		for (Integer num : coll)
		{
			ary[i] = num;
			i++;
		}
		data.setTag("data", new NBTTagIntArray(ary));
	}

	/**
	 * Reads an IntegerCollection from the given NBTTagCompound
	 *
	 * @param coll - collection to write to
	 * @param data - tag to read from
	 *   Expects a `data: NBTTagIntArray` field, `size: int` will be ignored.
	 */
	public static void readIntegerCollection(@Nonnull Collection<Integer> coll, @Nonnull NBTTagCompound data)
	{
		final NBTBase base = data.getTag("data");
		if (base instanceof NBTTagIntArray)
		{
			final NBTTagIntArray ary = (NBTTagIntArray)base;
			for (int i : ary.func_150302_c())
			{
				coll.add(i);
			}
		}
		else
		{
			final NBTType actual = NBTType.byId(base.getId());
			throw UnexpectedNBTTagType.createFor(NBTType.INT_ARRAY, actual);
		}
	}
}
