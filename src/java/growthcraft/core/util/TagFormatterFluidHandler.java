package growthcraft.core.util;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

/**
 * Tag Formatter for IFluidHandler NBT data
 */
public class TagFormatterFluidHandler implements ITagFormatter
{
	public static final TagFormatterFluidHandler INSTANCE = new TagFormatterFluidHandler();

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
					StatCollector.translateToLocalFormatted("grc.format.tank_id", tankTag.getInteger("tank_id") + 1) + " " +
					content;
			}
			final int fluidID = tankTag.getInteger("fluid_id");
			if (fluidID != ConstID.NO_FLUID)
			{
				final FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tankTag.getCompoundTag("fluid"));
				final String fluidName = UnitFormatter.fluidNameForContainer(fluidStack);
				content = content +
					UnitFormatter.fractionNum(fluidStack.amount, tankTag.getInteger("capacity")) +
					EnumChatFormatting.GRAY + " " + StatCollector.translateToLocalFormatted("grc.format.tank.content_suffix", fluidName);
			}
			else
			{
				content = content + UnitFormatter.noFluid();
			}
			list.add(content);
		}
		return list;
	}
}
