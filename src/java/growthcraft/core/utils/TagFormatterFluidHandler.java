package growthcraft.core.utils;

import java.util.List;

import growthcraft.core.utils.ConstID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TagFormatterFluidHandler implements ITagFormatter
{
	public final static TagFormatterFluidHandler INSTANCE = new TagFormatterFluidHandler();

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		final int tankCount = tag.getInteger("tank_count");
		final NBTTagList tanks = tag.getTagList("tanks", 10);
		for (int i = 0; i < tankCount; ++i)
		{
			final NBTTagCompound tankTag = tanks.getCompoundTagAt(i);
			String content = "";

			if (tankCount > 1) {
				// If the FluidHandler has multiple tanks, then prefix them as such,
				// otherwise, display their content like normal
				content = content +
					EnumChatFormatting.GRAY +
					StatCollector.translateToLocalFormatted("grc.format.tank_id", (tankTag.getInteger("tank_id") + 1)) + " " +
					content;
			}
			final int fluidID = tankTag.getInteger("fluid_id");
			if (fluidID != ConstID.NO_FLUID)
			{
				final int amount = tankTag.getInteger("amount");
				content = content + EnumChatFormatting.WHITE + amount +
					EnumChatFormatting.GRAY + " / " +
					EnumChatFormatting.WHITE + tankTag.getInteger("capacity") +
					EnumChatFormatting.GRAY + " " + StatCollector.translateToLocal("grc.unit.millibuckets");

				final FluidStack fluidStack = new FluidStack(FluidRegistry.getFluid(fluidID), amount);
				if (fluidStack != null)
				{
					content += " " + EnumChatFormatting.WHITE + fluidStack.getLocalizedName();
				}
				else
				{
					content += " " + EnumChatFormatting.RED + StatCollector.translateToLocal("grc.format.invalid_fluid");
				}
			}
			else
			{
				content = content + EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.format.tank.empty");
			}
			list.add(content);
		}
		return list;
	}
}
