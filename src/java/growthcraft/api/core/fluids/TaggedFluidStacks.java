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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.definition.IMultiFluidStacks;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TaggedFluidStacks implements IMultiFluidStacks
{
	public int amount;
	private List<String> tags;
	private List<FluidTag> fluidTags;

	/**
	 * @param amt - expected fluid stack size
	 * @param ptags - fluid tag names
	 */
	public TaggedFluidStacks(int amt, @Nonnull String... ptags)
	{
		this.amount = amt;
		this.tags = Arrays.asList(ptags);
		this.fluidTags = CoreRegistry.instance().fluidTags().expandTagNames(tags);
	}

	/**
	 * The tags to filter by
	 *
	 * @return tags
	 */
	public List<String> getTags()
	{
		return tags;
	}

	/**
	 * All fluids registered under the tags
	 *
	 * @return fluids
	 */
	public Collection<Fluid> getFluids()
	{
		return CoreRegistry.instance().fluidDictionary().getFluidsByTags(fluidTags);
	}

	@Override
	public int getAmount()
	{
		return amount;
	}

	@Override
	public List<FluidStack> getFluidStacks()
	{
		final Collection<Fluid> fluids = getFluids();
		final List<FluidStack> result = new ArrayList<FluidStack>();
		for (Fluid fluid : fluids)
		{
			result.add(new FluidStack(fluid, amount));
		}
		return result;
	}

	@Override
	public boolean containsFluidStack(@Nullable FluidStack stack)
	{
		if (!FluidTest.isValid(stack)) return false;
		final Fluid expected = stack.getFluid();
		for (Fluid content : getFluids())
		{
			if (expected == content) return true;
		}
		return false;
	}
}
