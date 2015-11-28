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
package growthcraft.api.cellar.common;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ProcessingResult
{
	public final int time;
	public final Residue residue;
	private final FluidStack fluid;

	public ProcessingResult(FluidStack f, int t, Residue r)
	{
		this.fluid = f;
		this.time = t;
		this.residue = r;
	}

	public Fluid getFluid()
	{
		return fluid.getFluid();
	}

	public FluidStack getFluidStack()
	{
		return fluid;
	}

	public int getAmount()
	{
		return fluid.amount;
	}

	public FluidStack asFluidStack(int size)
	{
		final FluidStack result = fluid.copy();
		result.amount = size;
		return result;
	}

	public FluidStack asFluidStack()
	{
		return fluid.copy();
	}
}
