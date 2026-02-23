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
package growthcraft.api.core.util;

import io.netty.buffer.ByteBuf;

import growthcraft.api.core.nbt.INBTSerializableContext;
import growthcraft.api.core.stream.IStreamable;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A simple tick keeping class for loop counting
 */
public class PulseStepper implements INBTSerializableContext, IStreamable
{
	public static enum State
	{
		NONE,
		TICK,
		PULSE;
	}

	/* How many times can the stepper start over, if set to 0 it will forever loop */
	public int maxLoops;
	/* The current loop */
	public int loops;
	/* How many steps, or ticks per loop? */
	public int maxSteps;
	/* The current step */
	public int steps;

	/**
	 * @param mxSteps - number of steps per loop
	 * @param mxLoops - maximum number of loops to do before the pulsar stops
	 *                  Setting this to 0 will cause it to loop forever
	 */
	public PulseStepper(int mxSteps, int mxLoops)
	{
		this.maxSteps = mxSteps;
		this.maxLoops = mxLoops;
	}

	public PulseStepper(int mxSteps)
	{
		this(mxSteps, 0);
	}

	public PulseStepper()
	{
		this(1);
	}

	public void setMaxLoops(int mx)
	{
		this.maxLoops = mx;
	}

	public void setMaxSteps(int mx)
	{
		this.maxSteps = mx;
	}

	public void resetLoops()
	{
		this.loops = 0;
	}

	public void resetSteps()
	{
		this.steps = 0;
	}

	public void reset()
	{
		resetLoops();
		resetSteps();
	}

	public void clear()
	{
		this.maxLoops = 0;
		this.loops = 0;
		this.maxSteps = 0;
		this.steps = 0;
	}

	/**
	 * @return State, if the stepper did not "step", then a State.NONE is returned
	 *         If it only advanced a step, then a State.TICK is returned,
	 *         if it advanced a loop, then a State.PULSE is returned.
	 */
	public State update()
	{
		if (maxLoops > 0 && loops >= maxLoops) return State.NONE;

		steps++;
		if (steps >= maxSteps)
		{
			resetSteps();
			loops++;
			return State.PULSE;
		}

		return State.TICK;
	}

	private void readFromNBT(NBTTagCompound data)
	{
		this.maxLoops = data.getInteger("max_loops");
		this.loops = data.getInteger("loops");
		this.maxSteps = data.getInteger("max_steps");
		this.steps = data.getInteger("steps");
	}

	@Override
	public void readFromNBT(NBTTagCompound data, String name)
	{
		if (data.hasKey(name))
		{
			readFromNBT(data.getCompoundTag(name));
		}
		else
		{
			// WARN
		}
	}

	private void writeToNBT(NBTTagCompound data)
	{
		data.setInteger("max_loops", maxLoops);
		data.setInteger("loops", loops);
		data.setInteger("max_steps", maxSteps);
		data.setInteger("steps", steps);
	}

	@Override
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		data.setTag(name, tag);
	}

	@Override
	public boolean readFromStream(ByteBuf stream)
	{
		this.maxLoops = stream.readInt();
		this.loops = stream.readInt();
		this.maxSteps = stream.readInt();
		this.steps = stream.readInt();
		return false;
	}

	@Override
	public boolean writeToStream(ByteBuf stream)
	{
		stream.writeInt(maxLoops);
		stream.writeInt(loops);
		stream.writeInt(maxSteps);
		stream.writeInt(steps);
		return false;
	}
}
