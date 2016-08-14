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
package growthcraft.milk.common.tileentity;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.core.common.tileentity.feature.ITileProgressiveDevice;
import growthcraft.core.common.tileentity.GrcTileEntityDeviceBase;
import growthcraft.milk.common.tileentity.device.Pancheon;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityPancheon extends GrcTileEntityDeviceBase implements ITileProgressiveDevice, IPancheonTile
{
	private Pancheon pancheon = new Pancheon(this, 0, 2, 1);

	@Override
	public float getDeviceProgress()
	{
		return pancheon.getProgress();
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		return pancheon.getProgressScaled(scale);
	}

	/**
	 * Pancheons have 3 fluid slots, the first is its `input` slot
	 * The second slot is its `bottom` output slot
	 * And the thirs is its `top` slot
	 * Though the capacity of each is 1000 mB, the pancheon can only contain
	 * a total of 1000 mB, not 3k
	 *
	 * @return fluid tanks
	 */
	@Override
	protected FluidTank[] createTanks()
	{
		return new FluidTank[] {
			new FluidTank(1000),
			new FluidTank(1000),
			new FluidTank(1000)
		};
	}

	protected int getPresentTankIndex()
	{
		for (int i = getTankCount() - 1; i > 0; --i)
		{
			if (isFluidTankFilled(i))
			{
				return i;
			}
		}
		return 0;
	}

	/**
	 * Pancheon tanks are treated as a Stack.
	 * When a tank at the end if filled, it will be returned, if its
	 * empty then it returns the tank before it and so forth.
	 *
	 * @return the active fluid tank
	 */
	public FluidTank getPresentTank()
	{
		return getFluidTank(getPresentTankIndex());
	}

	public boolean outputTanksHaveFluid()
	{
		return isFluidTankFilled(1) || isFluidTankFilled(2);
	}

	@Override
	protected void updateDevice()
	{
		pancheon.update();
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain)
	{
		return getPresentTank().drain(amount, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		/**
		 * @todo Drain from bottom fluid tank when dir == DOWN
		 */

		if (!FluidTest.isValid(stack)) return null;
		final FluidTank tank = getPresentTank();
		final FluidStack expected = tank.getFluid();
		if (expected != null && expected.isFluidEqual(stack))
		{
			return tank.drain(stack.amount, doDrain);
		}
		return null;
	}

	@Override
	protected int doFill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		if (outputTanksHaveFluid()) return 0;
		return fillFluidTank(0, stack, doFill);
	}

	@Override
	protected void markForFluidUpdate()
	{
		markForBlockUpdate();
	}
}
