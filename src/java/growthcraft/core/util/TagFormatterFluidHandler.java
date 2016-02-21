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

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.ConstID;
import growthcraft.api.core.util.ITagFormatter;
import growthcraft.api.core.nbt.NBTHelper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
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
		final NBTTagList tanks = tag.getTagList("tanks", NBTHelper.NBTType.COMPOUND.id);
		final NBTTagList tankNames = tag.hasKey("tank_names") ? tag.getTagList("tank_names", NBTHelper.NBTType.STRING.id) : null;
		for (int i = 0; i < tankCount; ++i)
		{
			final NBTTagCompound tankTag = tanks.getCompoundTagAt(i);
			final String name = tankNames != null ? tankNames.getStringTagAt(i) : null;
			String content = "";

			if (name != null && name.length() > 0)
			{
				content += GrcI18n.translate(name) + " ";
			}
			else
			{
				if (tankCount > 1)
				{
					// If the FluidHandler has multiple tanks, then prefix them as such,
					// otherwise, display their content like normal
					content += EnumChatFormatting.GRAY + GrcI18n.translate("grc.format.tank_id", tankTag.getInteger("tank_id") + 1) + " ";
				}
			}

			final int fluidID = tankTag.getInteger("fluid_id");
			if (fluidID != ConstID.NO_FLUID)
			{
				final FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tankTag.getCompoundTag("fluid"));
				final String fluidName = UnitFormatter.fluidNameForContainer(fluidStack);
				content = content +
					UnitFormatter.fractionNum(fluidStack.amount, tankTag.getInteger("capacity")) +
					EnumChatFormatting.GRAY + " " + GrcI18n.translate("grc.format.tank.content_suffix", fluidName);
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
