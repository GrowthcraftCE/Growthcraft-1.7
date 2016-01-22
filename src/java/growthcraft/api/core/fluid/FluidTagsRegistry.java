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
package growthcraft.api.core.fluid;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraftforge.fluids.Fluid;

public class FluidTagsRegistry implements IFluidTagsRegistry
{
	private Map<Fluid, Set<FluidTag>> tagMap = new HashMap<Fluid, Set<FluidTag>>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addFluidTags(@Nonnull Fluid fluid, @Nonnull FluidTag... tags)
	{
		if (!tagMap.containsKey(fluid))
		{
			tagMap.put(fluid, new HashSet<FluidTag>());
		}

		for (FluidTag tag : tags)
		{
			tagMap.get(fluid).add(tag);
		}
	}

	@Override
	public boolean hasFluidTags(@Nonnull Fluid fluid, @Nonnull FluidTag... tags)
	{
		final Set<FluidTag> fluidTags = tagMap.get(fluid);
		if (fluidTags == null) return false;
		for (FluidTag tag : tags)
		{
			if (!fluidTags.contains(tag)) return false;
		}
		return true;
	}
}
