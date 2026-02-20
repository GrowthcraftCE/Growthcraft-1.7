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
package growthcraft.api.milk.pancheon;

import growthcraft.api.core.fluids.FluidFormatString;

import net.minecraftforge.fluids.FluidStack;

public class PancheonRecipe implements IPancheonRecipe
{
	private FluidStack inputFluid;
	private FluidStack topOutFluid;
	private FluidStack bottomOutFluid;
	// time in ticks
	private int time;

	public PancheonRecipe(FluidStack inputStack, FluidStack topOutput, FluidStack bottomOutput, int t)
	{
		this.inputFluid = inputStack;
		this.topOutFluid = topOutput;
		this.bottomOutFluid = bottomOutput;
		this.time = t;
	}

	@Override
	public boolean isValidForRecipe(FluidStack stack)
	{
		if (stack == null) return false;
		if (!inputFluid.isFluidEqual(stack)) return false;
		if (stack.amount < inputFluid.amount) return false;
		return true;
	}

	@Override
	public FluidStack getInputFluid()
	{
		return inputFluid;
	}

	@Override
	public FluidStack getTopOutputFluid()
	{
		return topOutFluid;
	}

	@Override
	public FluidStack getBottomOutputFluid()
	{
		return bottomOutFluid;
	}

	@Override
	public int getTime()
	{
		return time;
	}

	@Override
	public String toString()
	{
		return String.format("PancheonRecipe(`%s` / %d = `%s` & `%s`)", FluidFormatString.format(inputFluid), time, FluidFormatString.format(topOutFluid), FluidFormatString.format(bottomOutFluid));
	}
}
