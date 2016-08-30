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
package growthcraft.milk.common.tileentity.device;

import growthcraft.api.milk.MilkRegistry;
import growthcraft.api.milk.pancheon.IPancheonRecipe;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.device.DeviceProgressive;
import growthcraft.core.common.tileentity.device.IFluidTanks;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

public class Pancheon extends DeviceProgressive
{
	private DeviceFluidSlot inputSlot;
	private DeviceFluidSlot topSlot;
	private DeviceFluidSlot bottomSlot;

	/**
	 * @param te - tile entity
	 * @param fsInput - input fluid slot
	 * @param fsTop - top output slot
	 * @param fsBottom - bottom output slot
	 */
	public Pancheon(TileEntity te, int fsInput, int fsTop, int fsBottom)
	{
		super(te);
		if (te instanceof IFluidTanks)
		{
			final IFluidTanks ifl = (IFluidTanks)te;
			this.inputSlot = new DeviceFluidSlot(ifl, fsInput);
			this.topSlot = new DeviceFluidSlot(ifl, fsTop);
			this.bottomSlot = new DeviceFluidSlot(ifl, fsBottom);
		}
		else
		{
			throw new IllegalArgumentException("The provided TileEntity MUST implement the IFluidTanks interface");
		}
	}

	/**
	 * Get the matching recipe
	 *
	 * @return recipe
	 */
	private IPancheonRecipe getRecipe()
	{
		return MilkRegistry.instance().pancheon().getRecipe(inputSlot.get());
	}

	/**
	 * Get the matching recipe AND it can be worked on with the current pancheon
	 *
	 * @return recipe
	 */
	public IPancheonRecipe getWorkingRecipe()
	{
		final IPancheonRecipe recipe = getRecipe();
		if (recipe == null) return null;
		if (!this.topSlot.hasMatchingWithCapacity(recipe.getTopOutputFluid())) return null;
		if (!this.bottomSlot.hasMatchingWithCapacity(recipe.getBottomOutputFluid())) return null;
		return recipe;
	}

	/**
	 * Complete the process and commit the changes
	 */
	private void commitRecipe()
	{
		final IPancheonRecipe recipe = getRecipe();
		if (recipe != null)
		{
			this.inputSlot.consume(recipe.getInputFluid().amount, true);

			final FluidStack top = recipe.getTopOutputFluid();
			if (top != null) this.topSlot.fill(top, true);

			final FluidStack bottom = recipe.getBottomOutputFluid();
			if (bottom != null) this.bottomSlot.fill(bottom, true);
		}
	}

	/**
	 * Tick update
	 */
	public void update()
	{
		final IPancheonRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			setTimeMax(recipe.getTime());
			increaseTime();
			if (time >= timeMax)
			{
				resetTime();
				commitRecipe();
			}
		}
		else
		{
			if (resetTime()) markDirty();
		}
	}
}
