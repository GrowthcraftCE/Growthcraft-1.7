package growthcraft.core.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class NBTHelper
{
	private NBTHelper() {}

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
}
