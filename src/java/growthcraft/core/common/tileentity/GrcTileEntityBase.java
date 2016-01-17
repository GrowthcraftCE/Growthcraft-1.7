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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.event.EventFunction;

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
public abstract class GrcTileEntityBase extends TileEntity implements IBlockUpdateFlagging
{
	protected static class HandlerMap extends EnumMap<EventHandler.EventType, List<EventFunction>>
	{
		public static final long serialVersionUID = 1L;

		public HandlerMap()
		{
			super(EventHandler.EventType.class);
		}
	}

	protected static Map<Class<? extends GrcTileEntityBase>, HandlerMap> HANDLERS = new HashMap<Class<? extends GrcTileEntityBase>, HandlerMap>();

	protected boolean needBlockUpdate = true;

	public void markForBlockUpdate()
	{
		needBlockUpdate = true;
	}

	public void doMarkForUpdate()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	protected void preMarkForUpdate()
	{

	}

	@Override
	public void updateEntity()
	{
		if (needBlockUpdate)
		{
			needBlockUpdate = false;
			preMarkForUpdate();
			doMarkForUpdate();
		}

		super.updateEntity();
	}

	protected void addHandler(@Nonnull HandlerMap handlerMap, @Nonnull EventHandler.EventType type, @Nonnull Method method)
	{
		if (!handlerMap.containsKey(type))
		{
			handlerMap.put(type, new ArrayList<EventFunction>());
		}
		handlerMap.get(type).add(new EventFunction(method));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Nonnull
	protected HandlerMap getHandlersMap()
	{
		final Class klass = getClass();
		HandlerMap cached = HANDLERS.get(klass);
		if (cached == null)
		{
			cached = new HandlerMap();
			HANDLERS.put(klass, cached);
			for (Method method : klass.getMethods())
			{
				final EventHandler anno = method.getAnnotation(EventHandler.class);
				if (anno != null) addHandler(cached, anno.type(), method);
			}
		}
		return cached;
	}

	protected List<EventFunction> getHandlersFor(@Nonnull EventHandler.EventType type)
	{
		return getHandlersMap().get(type);
	}

	protected void writeToStream(ByteBuf stream)
	{
		final List<EventFunction> handlers = getHandlersFor(EventHandler.EventType.NETWORK_WRITE);
		if (handlers != null)
		{
			for (EventFunction func : handlers)
			{
				func.writeToStream(this, stream);
			}
		}
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

	protected boolean readFromStream(ByteBuf stream)
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
			if (tag != null)
			{
				final ByteBuf stream = Unpooled.copiedBuffer(tag.getByteArray("P"));
				if (readFromStream(stream))
				{
					doMarkForUpdate();
				}
			}
		}
	}
}
