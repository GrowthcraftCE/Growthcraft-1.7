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
package growthcraft.core.common.tileentity;

import java.io.IOException;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.core.common.tileentity.device.FluidTanks;
import growthcraft.core.common.tileentity.device.IFluidTanks;
import growthcraft.core.common.tileentity.event.EventHandler;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Extend this base class if you only need a device with Fluid Tanks
 */
public abstract class GrcTileEntityFluidsDevice extends GrcTileEntityBase implements IFluidHandler, IFluidTanks
{
	private FluidTanks tanks;

	public GrcTileEntityFluidsDevice()
	{
		super();

		this.tanks = new FluidTanks(createTanks());
	}

	protected abstract FluidTank[] createTanks();
	protected abstract void updateDevice();

	// Call this when you modify a fluid tank outside of its usual methods
	protected void markForFluidUpdate()
	{
		//
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			updateDevice();
		}
	}

	protected void readTanksFromNBT(NBTTagCompound nbt)
	{
		tanks.readFromNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readTanksFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tanks.writeToNBT(nbt);
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_FluidTanks(ByteBuf stream) throws IOException
	{
		tanks.readFromStream(stream);
		return true;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public void writeToStream_FluidTanks(ByteBuf stream) throws IOException
	{
		tanks.writeToStream(stream);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	protected abstract FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain);
	protected abstract FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain);

	@Override
	public FluidStack drain(ForgeDirection dir, int amount, boolean doDrain)
	{
		final FluidStack result = doDrain(dir, amount, doDrain);
		if (doDrain && FluidTest.isValid(result)) markForFluidUpdate();
		return result;
	}

	@Override
	public FluidStack drain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		if (!FluidTest.isValid(stack)) return null;
		final FluidStack result = doDrain(dir, stack, doDrain);
		if (doDrain && FluidTest.isValid(result)) markForFluidUpdate();
		return result;
	}

	protected abstract int doFill(ForgeDirection dir, FluidStack stack, boolean doFill);

	@Override
	public int fill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		final int result = doFill(dir, stack, doFill);
		if (doFill && result != 0) markForFluidUpdate();
		return result;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return tanks.getTankInfo(from);
	}

	public IFluidTanks getTanks()
	{
		return tanks;
	}

	@Override
	public int getTankCount()
	{
		return tanks.getTankCount();
	}

	@Override
	public FluidTank[] getFluidTanks()
	{
		return tanks.getFluidTanks();
	}

	@Override
	public int getFluidAmountScaled(int scalar, int slot)
	{
		return tanks.getFluidAmountScaled(scalar, slot);
	}

	@Override
	public float getFluidAmountRate(int slot)
	{
		return tanks.getFluidAmountRate(slot);
	}

	@Override
	public boolean isFluidTankFilled(int slot)
	{
		return tanks.isFluidTankFilled(slot);
	}

	@Override
	public boolean isFluidTankFull(int slot)
	{
		return tanks.isFluidTankFull(slot);
	}

	@Override
	public boolean isFluidTankEmpty(int slot)
	{
		return tanks.isFluidTankEmpty(slot);
	}

	@Override
	public int getFluidAmount(int slot)
	{
		return tanks.getFluidAmount(slot);
	}

	@Override
	public FluidTank getFluidTank(int slot)
	{
		return tanks.getFluidTank(slot);
	}

	@Override
	public FluidStack getFluidStack(int slot)
	{
		return tanks.getFluidStack(slot);
	}

	@Override
	public FluidStack drainFluidTank(int slot, int amount, boolean doDrain)
	{
		final FluidStack result = tanks.drainFluidTank(slot, amount, doDrain);
		if (result != null && result.amount != 0) markForFluidUpdate();
		return result;
	}

	@Override
	public int fillFluidTank(int slot, FluidStack fluid, boolean doFill)
	{
		final int result = tanks.fillFluidTank(slot, fluid, doFill);
		if (result != 0) markForFluidUpdate();
		return result;
	}

	@Override
	public void setFluidStack(int slot, FluidStack stack)
	{
		tanks.setFluidStack(slot, stack);
		markForFluidUpdate();
	}

	@Override
	public Fluid getFluid(int slot)
	{
		return tanks.getFluid(slot);
	}

	@Override
	public void clearTank(int slot)
	{
		tanks.clearTank(slot);
		markForFluidUpdate();
	}
}
