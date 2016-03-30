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
package growthcraft.cellar.common.tileentity.device;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.culturing.ICultureRecipe;
import growthcraft.cellar.common.tileentity.component.TileHeatingComponent;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.device.DeviceInventorySlot;
import growthcraft.core.common.tileentity.device.DeviceProgressive;

public class CultureGenerator extends DeviceProgressive
{
	protected DeviceFluidSlot fluidSlot;
	protected DeviceInventorySlot invSlot;
	protected TileHeatingComponent heatComponent;

	/**
	 * @param te - parent tile entity
	 * @param fluidSlotIndex - fluid slot id to use in parent
	 *             Fluid will be used from this slot
	 * @param invSlotIndex - inventory slot id to use in parent
	 *             Culture will be generated into this slot
	 */
	public CultureGenerator(TileEntityCellarDevice te, TileHeatingComponent heatComp, int fluidSlotIndex, int invSlotIndex)
	{
		super(te);
		this.heatComponent = heatComp;
		this.fluidSlot = new DeviceFluidSlot(te, fluidSlotIndex);
		this.invSlot = new DeviceInventorySlot(te, invSlotIndex);
		setTimeMax(1200);
	}

	public float getHeatMultiplier()
	{
		return heatComponent.getHeatMultiplier();
	}

	@Override
	public void increaseTime()
	{
		this.time += 1;
	}

	public boolean isHeated()
	{
		return heatComponent.isHeated();
	}

	private boolean isRecipeValid(ICultureRecipe recipe)
	{
		if (recipe != null)
		{
			if (fluidSlot.hasEnough(recipe.getInputFluidStack()))
			{
				return invSlot.isEmpty() || invSlot.hasMatchingWithCapacity(recipe.getOutputItemStack());
			}
		}
		return false;
	}

	private void produceCulture(ICultureRecipe recipe)
	{
		fluidSlot.consume(recipe.getInputFluidStack(), true);
		invSlot.increaseStack(recipe.getOutputItemStack());
	}

	@Override
	public void update()
	{
		final ICultureRecipe activeRecipe = CellarRegistry.instance().culturing().findRecipe(fluidSlot.get(), heatComponent.getHeatMultiplier());

		if (isRecipeValid(activeRecipe))
		{
			setTimeMax(activeRecipe.getTime());
			increaseTime();
			if (time >= timeMax)
			{
				resetTime();
				produceCulture(activeRecipe);
				markForInventoryUpdate();
			}
		}
		else
		{
			if (resetTime()) markForInventoryUpdate();
		}
	}
}
