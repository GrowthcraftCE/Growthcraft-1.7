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
