/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.api.core.util;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidTag;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidTest
{
	private FluidTest() {}

	public static boolean hasTags(@Nullable FluidStack stack, FluidTag... tags)
	{
		if (FluidTest.isValid(stack))
		{
			return CoreRegistry.instance().fluidDictionary().hasFluidTags(stack.getFluid(), tags);
		}
		return false;
	}

	public static boolean areStacksEqual(@Nullable FluidStack expected, @Nullable FluidStack other)
	{
		if (expected != null)
		{
			if (expected.isFluidEqual(other)) return true;
		}
		else
		{
			if (other != null) return true;
		}
		return false;
	}

	public static boolean hasEnough(@Nonnull FluidStack expected, @Nullable FluidStack actual)
	{
		if (actual == null) return false;
		if (!expected.isFluidEqual(actual)) return false;
		if (actual.amount < expected.amount) return false;
		return true;
	}

	public static boolean isValid(@Nullable FluidStack stack)
	{
		if (stack == null) return false;
		if (stack.getFluid() == null) return false;
		if (stack.amount <= 0) return false;
		return true;
	}

	public static boolean isValidAndExpected(@Nonnull FluidStack expected, @Nullable FluidStack stack)
	{
		if (isValid(stack))
		{
			if (expected.isFluidEqual(stack)) return true;
		}
		return false;
	}

	public static boolean isValidAndExpected(@Nonnull Fluid expected, @Nullable FluidStack stack)
	{
		if (isValid(stack))
		{
			if (stack.getFluid() == expected) return true;
		}
		return false;
	}

	public static boolean isValidAndExpected(@Nonnull List<FluidStack> expectedFluids, @Nonnull List<FluidStack> givenFluids)
	{
		if (expectedFluids.size() != givenFluids.size()) return false;
		for (int i = 0; i < expectedFluids.size(); ++i)
		{
			final FluidStack expected = expectedFluids.get(i);
			final FluidStack actual = givenFluids.get(i);
			if (expected != null)
			{
				if (!isValid(actual)) return false;
				if (!expected.isFluidEqual(actual)) return false;
			}
			else
			{
				if (actual != null) return false;
			}
		}
		return true;
	}

	public static boolean hasEnoughAndExpected(@Nonnull List<FluidStack> expectedFluids, @Nonnull List<FluidStack> givenFluids)
	{
		if (expectedFluids.size() != givenFluids.size()) return false;
		for (int i = 0; i < expectedFluids.size(); ++i)
		{
			final FluidStack expected = expectedFluids.get(i);
			final FluidStack actual = givenFluids.get(i);
			if (expected != null)
			{
				if (!isValid(actual)) return false;
				if (!hasEnough(expected, actual)) return false;
			}
			else
			{
				if (actual != null) return false;
			}
		}
		return true;
	}
}
