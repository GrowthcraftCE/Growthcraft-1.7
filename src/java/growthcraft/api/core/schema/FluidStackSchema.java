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
package growthcraft.api.core.schema;

import javax.annotation.Nonnull;

import growthcraft.api.core.definition.IFluidStackFactory;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidStackSchema implements IFluidStackFactory, IValidatable, ICommentable
{
	public String comment;
	public String name;
	public int amount;

	public FluidStackSchema(@Nonnull String nm, int amt)
	{
		this.name = nm;
		this.amount = amt;
		this.comment = "";
	}

	public FluidStackSchema(@Nonnull FluidStack stack)
	{
		this.name = stack.getFluid().getName();
		this.amount = stack.amount;
		this.comment = stack.getLocalizedName();
	}

	public FluidStackSchema()
	{
		this.comment = "";
		this.amount = 1;
	}

	@Override
	public void setComment(@Nonnull String comm)
	{
		this.comment = comm;
	}

	@Override
	public String getComment()
	{
		return comment;
	}

	public Fluid getFluid()
	{
		return FluidRegistry.getFluid(name);
	}

	@Override
	public FluidStack asFluidStack(int a)
	{
		final Fluid fluid = getFluid();
		if (fluid == null) return null;
		return new FluidStack(fluid, a);
	}

	@Override
	public FluidStack asFluidStack()
	{
		return asFluidStack(amount);
	}

	@Override
	public String toString()
	{
		return String.format("Schema<FluidStack>(name: '%s', amount: %d)", name, amount);
	}

	@Override
	public boolean isValid()
	{
		return asFluidStack() != null;
	}

	@Override
	public boolean isInvalid()
	{
		return !isValid();
	}
}
