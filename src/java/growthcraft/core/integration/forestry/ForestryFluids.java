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
package growthcraft.core.integration.forestry;

import growthcraft.api.core.definition.IFluidStackFactory;
import growthcraft.api.core.fluids.FluidTest;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public enum ForestryFluids implements IFluidStackFactory
{
	// This is really just vanilla water though ;O
	MILK("milk"),
	WATER("water"),
	HONEY("honey"),
	BIOMASS("biomass"),
	JUICE("juice"),
	SEEDOIL("seedoil");

	public final String name;

	private ForestryFluids(String nm)
	{
		this.name = nm;
	}

	public Fluid getFluid()
	{
		return FluidRegistry.getFluid(name);
	}

	@Override
	public FluidStack asFluidStack(int amount)
	{
		final Fluid fluid = getFluid();
		if (fluid == null) return null;
		return new FluidStack(fluid, amount);
	}

	@Override
	public FluidStack asFluidStack()
	{
		return asFluidStack(1);
	}

	/**
	 * Does the underlying fluid exist?
	 *
	 * @return true if it exists, false otherwise
	 */
	public boolean exists()
	{
		return getFluid() != null;
	}
}
