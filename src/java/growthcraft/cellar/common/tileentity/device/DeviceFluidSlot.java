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
package growthcraft.cellar.common.tileentity.device;

import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class DeviceFluidSlot
{
	private TileEntityCellarDevice tanks;
	private int index;

	public DeviceFluidSlot(TileEntityCellarDevice src, int idx)
	{
		this.tanks = src;
		this.index = idx;
	}

	public FluidStack get()
	{
		return tanks.getFluidStack(index);
	}

	public Fluid getFluid()
	{
		final FluidStack stack = get();
		if (stack == null) return null;
		return stack.getFluid();
	}

	public void set(FluidStack newStack)
	{
		tanks.setFluidStack(index, newStack);
	}

	public void consume(int amount, boolean doDrain)
	{
		tanks.drainFluidTank(index, amount, doDrain);
	}

	public void fill(FluidStack fluid, boolean doFill)
	{
		tanks.fillFluidTank(index, fluid, doFill);
	}

	public boolean hasContent()
	{
		return tanks.isFluidTankFilled(index);
	}

	public boolean isFull()
	{
		return tanks.isFluidTankFull(index);
	}

	public boolean isEmpty()
	{
		return tanks.isFluidTankEmpty(index);
	}

	public boolean hasEnough(FluidStack stack)
	{
		final FluidStack s = get();
		if (s != null)
		{
			if (stack.isFluidEqual(s))
			{
				if (s.amount >= stack.amount)
				{
					return true;
				}
			}
		}
		return false;
	}
}
