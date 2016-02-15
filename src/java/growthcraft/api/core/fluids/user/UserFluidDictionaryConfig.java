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
package growthcraft.api.core.fluids.user;

import java.io.BufferedReader;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidTag;
import growthcraft.api.core.fluids.IFluidTagsRegistry;
import growthcraft.api.core.fluids.IFluidDictionary;
import growthcraft.api.core.util.JsonConfigDef;

import net.minecraftforge.fluids.Fluid;

public class UserFluidDictionaryConfig extends JsonConfigDef
{
	private final UserFluidDictionaryEntries defaultEntries = new UserFluidDictionaryEntries();
	private UserFluidDictionaryEntries entries;

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader)
	{
		this.entries = gson.fromJson(reader, UserFluidDictionaryEntries.class);
	}

	private void addFluidDictionaryEntry(UserFluidDictionaryEntry entry)
	{
		if (entry == null)
		{
			logger.error("Entry was invalid");
			return;
		}

		if (entry.getFluid() == null)
		{
			logger.error("Entry fluid is invalid! %s", entry);
			return;
		}

		if (entry.tags == null)
		{
			logger.error("Entry tags are invalid! %s", entry);
			return;
		}

		final IFluidTagsRegistry fluidTags = CoreRegistry.instance().fluidTags();
		final IFluidDictionary fluidDict = CoreRegistry.instance().fluidDictionary();
		final Fluid fluid = entry.getFluid();

		for (String tagName : entry.tags)
		{
			FluidTag fluidTag = fluidTags.findTag(tagName);
			if (fluidTag == null)
			{
				logger.warn("Creating new FluidTag '%s'", tagName);
				fluidTag = fluidTags.createTag(tagName);
			}
			fluidDict.addFluidTags(fluid, fluidTag);
		}
	}

	@Override
	public void init()
	{
		super.init();
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.info("Adding %d fluid dictionary entries.", entries.data.size());
				for (UserFluidDictionaryEntry entry : entries.data) addFluidDictionaryEntry(entry);
			}
			else
			{
				logger.error("Invalid fluid dictionary entries data");
			}
		}
	}
}
