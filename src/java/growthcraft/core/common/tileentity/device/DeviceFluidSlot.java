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
package growthcraft.core.common.tileentity.device;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class DeviceFluidSlot
{
	private IFluidTanks tanks;
	private int index;

	/**
	 * @param src - source fluid tanks
	 * @param idx - fluid tank index
	 */
	public DeviceFluidSlot(IFluidTanks src, int idx)
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

	public int getAmount()
	{
		final FluidStack stack = get();
		if (stack == null) return 0;
		return stack.amount;
	}

	public int getCapacity()
	{
		return tanks.getFluidTank(index).getCapacity();
	}

	public int getAvailableCapacity()
	{
		return getCapacity() - getAmount();
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

	/**
	 * Does the slot have ANY fluid content?
	 *
	 * @return true, the slot contains a fluid, false otherwise.
	 */
	public boolean hasContent()
	{
		return tanks.isFluidTankFilled(index);
	}

	/**
	 * Is the slot full?
	 *
	 * @return true, the slot has reached its maximum capacity, false otherwise.
	 */
	public boolean isFull()
	{
		return tanks.isFluidTankFull(index);
	}

	/**
	 * Is the slot empty?
	 *
	 * @return true, the slot has no valid fluid, false otherwise.
	 */
	public boolean isEmpty()
	{
		return tanks.isFluidTankEmpty(index);
	}

	/**
	 * Does the provided fluid match the one in the slot?
	 *
	 * @param stack - fluid stack to test
	 * @return true, it has the same fluid, false otherwise
	 */
	public boolean hasMatching(FluidStack stack)
	{
		final FluidStack s = get();
		if (s != null) return stack.isFluidEqual(s);
		return true;
	}

	/**
	 * Does the slot have the same fluid, and the capacity to hold the stack?
	 *
	 * @param stack - fluid stack to test
	 * @return true, it has the same fluid and has capacity, false otherwise
	 */
	public boolean hasMatchingWithCapacity(FluidStack stack)
	{
		if (!hasMatching(stack)) return false;
		return getAvailableCapacity() >= stack.amount;
	}

	/**
	 * Does the slot contain the same fluid, and its amount is greater or equal to the given?
	 *
	 * @param stack - fluid stack to test
	 * @return true, it has the same fluid and has greater than or equal to the stack size;
	 */
	public boolean hasEnough(FluidStack stack)
	{
		if (!hasMatching(stack)) return false;
		final FluidStack s = get();
		if (s.amount >= stack.amount)
		{
			return true;
		}
		return false;
	}
}
