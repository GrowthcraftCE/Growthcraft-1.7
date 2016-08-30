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

import java.util.Random;

import io.netty.buffer.ByteBuf;

import growthcraft.api.core.nbt.INBTSerializableContext;
import growthcraft.api.core.stream.IStreamable;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;

public class DeviceBase implements INBTSerializableContext, IStreamable
{
	protected Random random = new Random();
	protected TileEntity parent;

	public DeviceBase(TileEntity te)
	{
		this.parent = te;
	}

	public TileEntity getTileEntity()
	{
		return parent;
	}

	public World getWorld()
	{
		return parent.getWorldObj();
	}

	public int getMetadata()
	{
		return getWorld().getBlockMetadata(parent.xCoord, parent.yCoord, parent.zCoord);
	}

	public IInventory getInventory()
	{
		if (parent instanceof IInventory)
		{
			return (IInventory)parent;
		}
		return null;
	}

	protected void markDirty()
	{
		parent.markDirty();
	}

	/**
	 * @param data - nbt data to read from
	 */
	public void readFromNBT(NBTTagCompound data)
	{
	}

	/**
	 * @param data - parent nbt data to read from
	 * @param name - sub tag to read
	 */
	@Override
	public void readFromNBT(NBTTagCompound data, String name)
	{
		if (data.hasKey(name))
		{
			final NBTTagCompound tag = data.getCompoundTag(name);
			readFromNBT(tag);
		}
		else
		{
			// LOG error
		}
	}

	/**
	 * @param data - nbt to write to
	 */
	public void writeToNBT(NBTTagCompound data)
	{
	}

	/**
	 * @param data - nbt to write to
	 * @param name - sub tag nbt to write to
	 */
	@Override
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		writeToNBT(target);
		data.setTag(name, target);
	}

	/**
	 * @param buf - buffer to read from
	 */
	@Override
	public boolean readFromStream(ByteBuf buf)
	{
		return false;
	}

	/**
	 * @param buf - buffer to write to
	 */
	@Override
	public boolean writeToStream(ByteBuf buf)
	{
		return false;
	}
}
