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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class NBTHelper
{
	public static class NBTType
	{
		public static final int END = 0;
		public static final int BYTE = 1;
		public static final int SHORT = 2;
		public static final int INT = 3;
		public static final int LONG = 4;
		public static final int FLOAT = 5;
		public static final int DOUBLE = 6;
		public static final int BYTE_ARRAY = 7;
		public static final int STRING = 8;
		public static final int LIST = 9;
		public static final int COMPOUND = 10;
		public static final int INT_ARRAY = 11;

		private NBTType() {}
	}

	private NBTHelper() {}

	public static NBTTagList writeInventorySlotsToNBT(ItemStack[] invSlots, NBTTagList invTags)
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

	public static NBTTagList writeInventorySlotsToNBT(ItemStack[] invSlots)
	{
		return writeInventorySlotsToNBT(invSlots, new NBTTagList());
	}

	public static NBTTagList readInventorySlotsFromNBT(ItemStack[] invSlots, NBTTagList tags)
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

	public static NBTTagCompound writeIFluidHandlerToNBT(IFluidHandler fluidHandler, NBTTagCompound tag)
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

	public static NBTTagCompound writeItemStackToNBT(ItemStack itemStack, NBTTagCompound tag)
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

	public static NBTTagCompound writeItemStackToNBT(ItemStack itemStack)
	{
		return writeItemStackToNBT(itemStack, new NBTTagCompound());
	}
}
