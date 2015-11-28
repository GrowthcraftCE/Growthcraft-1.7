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

import io.netty.buffer.ByteBuf;

import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;

import net.minecraft.nbt.NBTTagCompound;

public class DeviceProgressive extends DeviceBase
{
	protected int time;
	protected int timeMax = 1200;

	public DeviceProgressive(TileEntityCellarDevice te)
	{
		super(te);
	}

	public int getProgressScaled(int scale)
	{
		if (timeMax <= 0) return 0;
		return this.time * scale / timeMax;
	}

	public int getTime()
	{
		return time;
	}

	public int getTimeMax()
	{
		return timeMax;
	}

	public void setTime(int t)
	{
		this.time = t;
	}

	public void setTimeMax(int t)
	{
		this.timeMax = t;
	}

	protected boolean resetTime()
	{
		if (this.time != 0)
		{
			setTime(0);
			return true;
		}
		return false;
	}


	public void increaseTime()
	{
		time++;
	}

	/**
	 * Serialization
	 */

	/**
	 * @param data - nbt data to read from
	 */
	public void readFromNBT(NBTTagCompound data)
	{
		this.time = data.getInteger("time");
		//this.timeMax = data.getInteger("timeMax");
	}

	/**
	 * @param data - parent nbt data to read from
	 * @param name - sub tag to read
	 */
	public void readFromNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound list = data.getCompoundTag(name);
		if (list != null)
		{
			readFromNBT(list);
		}
		else
		{
			// LOG error
		}
	}

	/**
	 * @param buf - buffer to read from
	 */
	public void readFromStream(ByteBuf buf)
	{
		this.time = buf.readInt();
		//this.timeMax = buf.readInt();
	}

	/**
	 * @param data - nbt to write to
	 */
	public void writeToNBT(NBTTagCompound data)
	{
		data.setInteger("time", time);
		//data.setInteger("timeMax", timeMax);
	}

	/**
	 * @param data - nbt to write to
	 * @param name - sub tag nbt to write to
	 */
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		writeToNBT(target);
		data.setTag(name, target);
	}

	/**
	 * @param buf - buffer to write to
	 */
	public void writeToStream(ByteBuf buf)
	{
		buf.writeInt(time);
		//buf.writeInt(timeMax);
	}
}
