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
package growthcraft.api.core.fluids;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidDictionary implements IFluidDictionary
{
	private Map<Fluid, Set<FluidTag>> fluidToTagsMap = new HashMap<Fluid, Set<FluidTag>>();
	private Map<FluidTag, Set<Fluid>> tagToFluidsMap = new HashMap<FluidTag, Set<Fluid>>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addFluidTags(@Nonnull Fluid fluid, @Nonnull FluidTag... tags)
	{
		if (!fluidToTagsMap.containsKey(fluid))
		{
			logger.info("Initializing new HashSet for fluid {%s}", fluid);
			fluidToTagsMap.put(fluid, new HashSet<FluidTag>());
		}

		final Set<FluidTag> tagSet = fluidToTagsMap.get(fluid);
		for (FluidTag tag : tags)
		{
			logger.info("Adding tag '%s' to fluid {%s}", tag, fluid);
			tagSet.add(tag);
			if (!tagToFluidsMap.containsKey(tag))
			{
				tagToFluidsMap.put(tag, new HashSet<Fluid>());
			}
			tagToFluidsMap.get(tag).add(fluid);
		}
	}

	@Override
	public Collection<FluidTag> getFluidTags(@Nullable Fluid fluid)
	{
		if (fluid == null) return null;
		return fluidToTagsMap.get(fluid);
	}

	@Override
	public Collection<FluidTag> getFluidTags(@Nullable FluidStack stack)
	{
		if (stack == null) return null;
		return getFluidTags(stack.getFluid());
	}

	@Override
	public boolean hasFluidTags(@Nonnull Fluid fluid, @Nonnull FluidTag... tags)
	{
		final Set<FluidTag> fluidTags = fluidToTagsMap.get(fluid);
		if (fluidTags == null) return false;
		for (FluidTag tag : tags)
		{
			if (!fluidTags.contains(tag)) return false;
		}
		return true;
	}

	@Override
	public Collection<Fluid> getFluidsByTags(@Nonnull List<FluidTag> tags)
	{
		final Set<Fluid> fluids = new HashSet<Fluid>();
		if (tags.size() > 0)
		{
			final Set<Fluid> prim = tagToFluidsMap.get(tags.get(0));
			if (prim != null)
			{
				fluids.addAll(prim);
				for (int i = 1; i < tags.size(); ++i)
				{
					if (fluids.isEmpty()) break;
					final Set<Fluid> exc = tagToFluidsMap.get(tags.get(i));
					fluids.retainAll(exc);
				}
			}
		}
		return fluids;
	}

	@Override
	public Collection<Fluid> getFluidsByTags(@Nonnull FluidTag... tags)
	{
		return getFluidsByTags(Arrays.asList(tags));
	}
}
