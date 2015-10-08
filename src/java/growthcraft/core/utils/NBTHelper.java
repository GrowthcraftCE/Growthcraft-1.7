package growthcraft.core.utils;

import growthcraft.core.utils.ConstID;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class NBTHelper
{
	public static NBTTagCompound getIFluidHandlerData(IFluidHandler fluidHandler, NBTTagCompound tag)
	{
		final NBTTagList tankTagList = new NBTTagList();
		int tankId = 0;
		for (FluidTankInfo tankInfo : fluidHandler.getTankInfo(ForgeDirection.UNKNOWN))
		{
			final NBTTagCompound tankTag = new NBTTagCompound();
			tankTag.setInteger("tank_id", tankId);
			tankTag.setInteger("capacity", tankInfo.capacity);
			if (tankInfo.fluid != null)
			{
				tankTag.setInteger("fluid_id", tankInfo.fluid.getFluidID());
				tankTag.setInteger("amount", tankInfo.fluid.amount);
			}
			else
			{
				// no fluid
				tankTag.setInteger("fluid_id", ConstID.NO_FLUID);
				tankTag.setInteger("amount", 0);
			}
			tankTagList.appendTag(tankTag);
			++tankId;
		}
		tag.setTag("tanks", tankTagList);
		tag.setInteger("tank_count", tankId);
		return tag;
	}

	public static NBTTagCompound getItemData(ItemStack itemStack, NBTTagCompound tag)
	{
		if (itemStack != null)
		{
			final Item item = itemStack.getItem();
			tag.setInteger("id", (item != null) ? Item.getIdFromItem(item) : ConstID.NO_ITEM);
			tag.setInteger("damage", itemStack.getItemDamage());
			tag.setInteger("size", itemStack.stackSize);
		}
		else
		{
			tag.setInteger("id", ConstID.NO_ITEM);
			tag.setInteger("damage", 0);
			tag.setInteger("size", 0);
		}
		return tag;
	}
}
