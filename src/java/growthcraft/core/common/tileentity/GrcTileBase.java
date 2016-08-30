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
package growthcraft.core.common.tileentity;

import java.util.List;
import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import growthcraft.api.core.stream.IStreamable;
import growthcraft.api.core.nbt.INBTSerializable;
import growthcraft.core.common.tileentity.event.EventFunction;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.event.TileEventHandlerMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Extend this base class if you just need a Base tile with the event system.
 *
 * Event handling system is a stripped version of the one seen in AE2, I've
 * copied the code for use in YATM, but I've ported it over to Growthcraft as
 * well.
 */
public abstract class GrcTileBase extends TileEntity implements IStreamable, INBTSerializable
{
	protected static TileEventHandlerMap<GrcTileBase> HANDLERS = new TileEventHandlerMap<GrcTileBase>();

	protected List<EventFunction> getHandlersFor(@Nonnull EventHandler.EventType type)
	{
		return HANDLERS.getEventFunctionsForClass(getClass(), type);
	}

	@Override
	public final boolean writeToStream(ByteBuf stream)
	{
		final List<EventFunction> handlers = getHandlersFor(EventHandler.EventType.NETWORK_WRITE);
		if (handlers != null)
		{
			for (EventFunction func : handlers)
			{
				func.writeToStream(this, stream);
			}
		}
		return false;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		final NBTTagCompound data = new NBTTagCompound();
		final ByteBuf stream = Unpooled.buffer();

		try
		{
			writeToStream(stream);
			if (stream.readableBytes() == 0)
			{
				return null;
			}
		}
		catch (Throwable t)
		{
			System.err.println(t);
		}


		// P, for payload
		data.setByteArray("P", stream.array());

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 127, data);
	}

	@Override
	public final boolean readFromStream(ByteBuf stream)
	{
		boolean shouldUpdate = false;
		final List<EventFunction> handlers = getHandlersFor(EventHandler.EventType.NETWORK_READ);
		if (handlers != null)
		{
			for (EventFunction func : handlers)
			{
				if (func.readFromStream(this, stream))
				{
					shouldUpdate = true;
				}
			}
		}
		return shouldUpdate;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		if (packet.func_148853_f() == 127)
		{
			final NBTTagCompound tag = packet.func_148857_g();
			boolean dirty = false;
			if (tag != null)
			{
				final ByteBuf stream = Unpooled.copiedBuffer(tag.getByteArray("P"));
				if (readFromStream(stream))
				{
					dirty = true;
				}
			}
			if (dirty) markDirty();
		}
	}

	public void readFromNBTForItem(NBTTagCompound tag)
	{
	}

	public void writeToNBTForItem(NBTTagCompound tag)
	{
	}

	@Override
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		final List<EventFunction> handlers = getHandlersFor(EventHandler.EventType.NBT_READ);
		if (handlers != null)
		{
			for (EventFunction func : handlers)
			{
				func.readFromNBT(this, nbt);
			}
		}
	}

	@Override
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		final List<EventFunction> handlers = getHandlersFor(EventHandler.EventType.NBT_WRITE);
		if (handlers != null)
		{
			for (EventFunction func : handlers)
			{
				func.writeToNBT(this, nbt);
			}
		}
	}
}
