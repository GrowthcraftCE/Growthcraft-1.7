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

import io.netty.buffer.ByteBuf;

import growthcraft.api.cellar.brewing.BrewingRecipe;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.cellar.common.tileentity.component.TileHeatingComponent;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;
import growthcraft.cellar.event.EventBrewed;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.tileentity.device.DeviceBase;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.device.DeviceInventorySlot;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class BrewKettle extends DeviceBase
{
	private float grain;
	private double time;
	private double timeMax;
	private DeviceInventorySlot brewingSlot;
	private DeviceInventorySlot residueSlot;
	private DeviceFluidSlot inputFluidSlot;
	private DeviceFluidSlot outputFluidSlot;
	private TileHeatingComponent heatComponent;

	public BrewKettle(TileEntityCellarDevice te, int brewSlotId, int residueSlotId, int inputFluidSlotId, int outputFluidSlotId)
	{
		super(te);
		this.brewingSlot = new DeviceInventorySlot(te, brewSlotId);
		this.residueSlot = new DeviceInventorySlot(te, residueSlotId);
		this.inputFluidSlot = new DeviceFluidSlot(te, inputFluidSlotId);
		this.outputFluidSlot = new DeviceFluidSlot(te, outputFluidSlotId);
		this.heatComponent = new TileHeatingComponent(te, 0.5f);
	}

	public void setGrain(float g)
	{
		this.grain = g;
	}

	public double getTime()
	{
		return time;
	}

	public void setTime(double t)
	{
		this.time = t;
	}

	public double getTimeMax()
	{
		return timeMax;
	}

	public void setTimeMax(double t)
	{
		this.timeMax = t;
	}

	public boolean resetTime()
	{
		if (time != 0)
		{
			setTime(0);
			return true;
		}
		return false;
	}

	public float getProgress()
	{
		if (timeMax == 0) return 0f;
		return (float)(time / timeMax);
	}

	public int getProgressScaled(int scale)
	{
		return (int)(getProgress() * scale);
	}

	public BrewKettle setHeatMultiplier(float h)
	{
		heatComponent.setHeatMultiplier(h);
		return this;
	}

	public float getHeatMultiplier()
	{
		return heatComponent.getHeatMultiplier();
	}

	public boolean isHeated()
	{
		return getHeatMultiplier() > 0;
	}

	private BrewingRecipe getBrewingRecipe()
	{
		return CellarRegistry.instance().brewing().getBrewingRecipe(inputFluidSlot.get(), brewingSlot.get());
	}

	public BrewingRecipe getWorkingRecipe()
	{
		if (!isHeated()) return null;

		final BrewingRecipe recipe = getBrewingRecipe();
		if (recipe == null) return null;

		final IMultiItemStacks expected = recipe.getInputItemStack();
		brewingSlot.hasEnough(expected);

		final FluidStack inputFluid = recipe.getInputFluidStack();
		inputFluidSlot.hasEnough(inputFluid);

		if (outputFluidSlot.isEmpty()) return recipe;

		final FluidStack outputFluid = recipe.asFluidStack();
		if (!outputFluidSlot.hasCapacityFor(outputFluid)) return null;

		return recipe;
	}

	public boolean canBrew()
	{
		return getWorkingRecipe() != null;
	}

	private void produceGrain(BrewingRecipe recipe)
	{
		final Residue res = recipe.getResidue();
		if (res != null)
		{
			this.grain = this.grain + res.pomaceRate;
			while (this.grain >= 1.0F)
			{
				this.grain -= 1.0F;
				residueSlot.increaseStack(res.residueItem);
			}
		}
	}

	private void brewItem(BrewingRecipe recipe)
	{
		produceGrain(recipe);
		inputFluidSlot.consume(recipe.getInputFluidStack(), true);
		outputFluidSlot.fill(recipe.asFluidStack(), true);
		brewingSlot.consume(recipe.getInputItemStack());
		markForBlockUpdate();
		GrowthCraftCellar.CELLAR_BUS.post(new EventBrewed(parent, recipe));
	}

	public void update()
	{
		heatComponent.update();
		final BrewingRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			this.timeMax = (double)recipe.getTime();

			final float multiplier = getHeatMultiplier();
			this.time += multiplier * 1;

			if (time >= timeMax)
			{
				resetTime();
				brewItem(recipe);
			}
		}
		else
		{
			if (resetTime()) markForInventoryUpdate();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		this.time = data.getDouble("time");
		this.grain = data.getFloat("grain");
		heatComponent.readFromNBT(data, "heat_component");
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setDouble("time", time);
		data.setFloat("grain", grain);
		heatComponent.writeToNBT(data, "heat_component");
	}

	/**
	 * @param buf - buffer to read from
	 */
	@Override
	public boolean readFromStream(ByteBuf buf)
	{
		super.readFromStream(buf);
		this.time = buf.readDouble();
		this.timeMax = buf.readDouble();
		this.grain = buf.readFloat();
		heatComponent.readFromStream(buf);
		return false;
	}

	/**
	 * @param buf - buffer to write to
	 */
	@Override
	public boolean writeToStream(ByteBuf buf)
	{
		super.writeToStream(buf);
		buf.writeDouble(time);
		buf.writeDouble(timeMax);
		buf.writeFloat(grain);
		heatComponent.writeToStream(buf);
		return false;
	}
}
