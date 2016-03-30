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

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.pressing.PressingRecipe;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.device.DeviceInventorySlot;
import growthcraft.core.common.tileentity.device.DeviceProgressive;
import growthcraft.core.util.ItemUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class FruitPress extends DeviceProgressive
{
	private float pomace;
	private DeviceFluidSlot fluidSlot;
	private DeviceInventorySlot inputSlot;
	private DeviceInventorySlot residueSlot;
	private PressingRecipe currentResult;

	/**
	 * @param te - parent tile
	 * @param fs - fluid tank id
	 * @param is - input inventory slot id
	 * @param rs - residue inventory slot id
	 */
	public FruitPress(TileEntityCellarDevice te, int fs, int is, int rs)
	{
		super(te);
		this.fluidSlot = new DeviceFluidSlot(te, fs);
		this.inputSlot = new DeviceInventorySlot(te, is);
		this.residueSlot = new DeviceInventorySlot(te, rs);
	}

	/**
	 * @return meta - the metadata for the FruitPresser usually above the fruit press
	 */
	public int getPresserMetadata()
	{
		return getWorld().getBlockMetadata(parent.xCoord, parent.yCoord + 1, parent.zCoord);
	}

	private boolean preparePressing()
	{
		this.currentResult = null;
		final ItemStack primarySlotItem = inputSlot.get();
		if (primarySlotItem == null) return false;

		final int m = getPresserMetadata();
		if (m < 2) return false;

		if (fluidSlot.isFull()) return false;

		final PressingRecipe result = CellarRegistry.instance().pressing().getPressingRecipe(primarySlotItem);
		if (result == null) return false;
		if (!inputSlot.hasEnough(result.getInput())) return false;
		this.currentResult = result;
		setTimeMax(currentResult.getTime());

		if (fluidSlot.isEmpty()) return true;

		final FluidStack stack = currentResult.getFluidStack();
		return stack.isFluidEqual(fluidSlot.get());
	}

	public void producePomace()
	{
		if (currentResult == null) return;
		final Residue residue = currentResult.getResidue();
		if (residue != null)
		{
			this.pomace = this.pomace + residue.pomaceRate;
			if (this.pomace >= 1.0F)
			{
				this.pomace = this.pomace - 1.0F;
				final ItemStack residueResult = ItemUtils.mergeStacks(residueSlot.get(), residue.residueItem);
				if (residueResult != null) residueSlot.set(residueResult);
			}
		}
	}

	public void pressItem()
	{
		if (currentResult == null) return;
		final ItemStack pressingItem = inputSlot.get();
		producePomace();
		final FluidStack fluidstack = currentResult.getFluidStack();
		fluidSlot.fill(fluidstack, true);
		inputSlot.consume(currentResult.getInput());
	}

	public void update()
	{
		if (preparePressing())
		{
			increaseTime();
			if (getTime() >= getTimeMax())
			{
				resetTime();
				pressItem();
				markForInventoryUpdate();
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
		this.pomace = data.getFloat("pomace");
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setFloat("pomace", pomace);
	}
}
