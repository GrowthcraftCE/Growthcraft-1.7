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
package growthcraft.core.common.tileentity.device;

import io.netty.buffer.ByteBuf;

import growthcraft.api.core.nbt.INBTSerializableContext;
import growthcraft.api.core.stream.IStreamable;
import growthcraft.api.core.stream.StreamUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class FluidTanks implements IFluidTanks, INBTSerializableContext, IStreamable
{
	private FluidTank[] tanks;

	public FluidTanks(FluidTank[] ts)
	{
		this.tanks = ts;
	}

	public int getTankCount()
	{
		return tanks.length;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		final FluidTankInfo[] tankInfos = new FluidTankInfo[tanks.length];
		for (int i = 0; i < tanks.length; ++i)
		{
			tankInfos[i] = tanks[i].getInfo();
		}
		return tankInfos;
	}

	@Override
	public FluidTank[] getFluidTanks()
	{
		return tanks;
	}

	@Override
	public FluidTank getFluidTank(int slot)
	{
		return tanks[slot];
	}

	@Override
	public void clearTank(int slot)
	{
		tanks[slot].setFluid(null);
	}

	@Override
	public int getFluidAmountScaled(int scalar, int slot)
	{
		final int cap = tanks[slot].getCapacity();
		if (cap <= 0) return 0;
		return this.getFluidAmount(slot) * scalar / cap;
	}

	@Override
	public float getFluidAmountRate(int slot)
	{
		final int cap = tanks[slot].getCapacity();
		if (cap <= 0) return 0;
		return (float)this.getFluidAmount(slot) / (float)cap;
	}

	@Override
	public boolean isFluidTankFilled(int slot)
	{
		return this.getFluidAmount(slot) > 0;
	}

	@Override
	public boolean isFluidTankFull(int slot)
	{
		return this.getFluidAmount(slot) >= tanks[slot].getCapacity();
	}

	@Override
	public boolean isFluidTankEmpty(int slot)
	{
		return this.getFluidAmount(slot) <= 0;
	}

	@Override
	public int getFluidAmount(int slot)
	{
		return tanks[slot].getFluidAmount();
	}

	@Override
	public FluidStack getFluidStack(int slot)
	{
		return tanks[slot].getFluid();
	}

	@Override
	public FluidStack drainFluidTank(int slot, int amount, boolean doDrain)
	{
		return tanks[slot].drain(amount, doDrain);
	}

	@Override
	public int fillFluidTank(int slot, FluidStack fluid, boolean doFill)
	{
		return tanks[slot].fill(fluid, doFill);
	}

	@Override
	public void setFluidStack(int slot, FluidStack stack)
	{
		tanks[slot].setFluid(stack);
	}

	@Override
	public Fluid getFluid(int slot)
	{
		final FluidStack stack = getFluidStack(slot);
		if (stack == null) return null;
		return stack.getFluid();
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			tanks[i].setFluid(null);
			if (nbt.hasKey("Tank" + i))
			{
				tanks[i].readFromNBT(nbt.getCompoundTag("Tank" + i));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String name)
	{
		if (nbt.hasKey(name))
		{
			readFromNBT(nbt.getCompoundTag(name));
		}
		else
		{
			// log error
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			tanks[i].writeToNBT(tag);
			nbt.setTag("Tank" + i, tag);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String name)
	{
		final NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		nbt.setTag(name, tag);
	}

	@Override
	public void readFromStream(ByteBuf stream)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			StreamUtils.readFluidTank(stream, tanks[i]);
		}
	}

	@Override
	public void writeToStream(ByteBuf stream)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			StreamUtils.writeFluidTank(stream, tanks[i]);
		}
	}
}
