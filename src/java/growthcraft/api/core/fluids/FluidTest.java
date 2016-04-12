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
package growthcraft.api.core.fluids;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.definition.IMultiFluidStacks;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidTest
{
	private FluidTest() {}

	/**
	 * Determines if a Fluid is invalid, this is a very simple null check.
	 *
	 * @param fluid - the fluid to check
	 * @return true, if the fluid is valid, false otherwise
	 */
	public static boolean isValid(@Nullable Fluid fluid)
	{
		return fluid != null;
	}

	/**
	 * Determines if a FluidStack is valid by checking:
	 *   if the stack is null, it is invalid
	 *   if the stack has a null fluid, it is invalid
	 *   if the stack has an amount less than or equal to 0, it is invalid
	 *
	 * @param stack - the fluid stack to check
	 * @return true, the stack is valid, false otherwise
	 */
	public static boolean isValid(@Nullable FluidStack stack)
	{
		if (stack == null) return false;
		if (stack.getFluid() == null) return false;
		if (stack.amount <= 0) return false;
		return true;
	}

	public static boolean hasTags(@Nullable Fluid fluid, FluidTag... tags)
	{
		if (FluidTest.isValid(fluid))
		{
			return CoreRegistry.instance().fluidDictionary().hasFluidTags(fluid, tags);
		}
		return false;
	}

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
		if (expected == null)
		{
			return other == null;
		}
		else
		{
			if (other == null) return false;
			return expected.isFluidEqual(other);
		}
	}

	public static boolean fluidMatches(@Nullable FluidStack expected, @Nullable FluidStack other)
	{
		return areStacksEqual(expected, other);
	}

	public static boolean fluidMatches(@Nullable IMultiFluidStacks expected, @Nullable FluidStack other)
	{
		if (expected == null)
		{
			return other == null;
		}
		else
		{
			if (other == null) return false;
			return expected.containsFluidStack(other);
		}
	}

	public static boolean hasEnough(@Nullable FluidStack expected, @Nullable FluidStack actual)
	{
		if (expected == null) return actual == null;
		if (actual == null) return false;
		if (!expected.isFluidEqual(actual)) return false;
		if (actual.amount < expected.amount) return false;
		return true;
	}

	public static boolean hasEnough(@Nullable IMultiFluidStacks expected, @Nullable FluidStack actual)
	{
		if (expected == null) return actual == null;
		if (actual == null) return false;
		if (!expected.containsFluidStack(actual)) return false;
		if (actual.amount < expected.getAmount()) return false;
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

	@SuppressWarnings({"rawtypes"})
	public static boolean isValidAndExpected(@Nonnull List expectedFluids, @Nonnull List<FluidStack> givenFluids)
	{
		if (expectedFluids.size() != givenFluids.size()) return false;
		for (int i = 0; i < expectedFluids.size(); ++i)
		{
			final Object expected = expectedFluids.get(i);
			final FluidStack actual = givenFluids.get(i);
			if (expected != null)
			{
				if (!isValid(actual)) return false;
				if (expected instanceof IMultiFluidStacks)
				{
					if (!((IMultiFluidStacks)expected).containsFluidStack(actual)) return false;
				}
				else if (expected instanceof FluidStack)
				{
					if (!((FluidStack)expected).isFluidEqual(actual)) return false;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if (actual != null) return false;
			}
		}
		return true;
	}

	@SuppressWarnings({"rawtypes"})
	public static boolean hasEnoughAndExpected(@Nonnull List expectedFluids, @Nonnull List<FluidStack> givenFluids)
	{
		if (expectedFluids.size() != givenFluids.size()) return false;
		for (int i = 0; i < expectedFluids.size(); ++i)
		{
			final Object expected = expectedFluids.get(i);
			final FluidStack actual = givenFluids.get(i);
			if (expected != null)
			{
				if (!isValid(actual)) return false;
				if (expected instanceof IMultiFluidStacks)
				{
					if (!hasEnough((IMultiFluidStacks)expected, actual)) return false;
				}
				else if (expected instanceof FluidStack)
				{
					if (!hasEnough((FluidStack)expected, actual)) return false;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if (actual != null) return false;
			}
		}
		return true;
	}
}
