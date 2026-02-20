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
package growthcraft.core.common.tileentity.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;

public class TileEventHandlerMap<T extends TileEntity> extends HashMap<Class<? extends T>, TileEventFunctionMap>
{
	public static final long serialVersionUID = 1L;

	protected void addHandlerEventFunction(@Nonnull TileEventFunctionMap handlerMap, @Nonnull TileEventHandler.EventType type, @Nonnull Method method)
	{
		if (!handlerMap.containsKey(type))
		{
			handlerMap.put(type, new ArrayList<TileEventFunction>());
		}
		handlerMap.get(type).add(new TileEventFunction(method));
	}

	public TileEventFunctionMap getEventFunctionMap(Class<? extends T> klass)
	{
		TileEventFunctionMap cached = get(klass);
		if (cached == null)
		{
			cached = new TileEventFunctionMap();
			put(klass, cached);
			for (Method method : klass.getMethods())
			{
				final TileEventHandler anno = method.getAnnotation(TileEventHandler.class);
				if (anno != null) addHandlerEventFunction(cached, anno.event(), method);
			}
		}
		return cached;
	}

	public List<TileEventFunction> getEventFunctionsForClass(Class<? extends T> klass, TileEventHandler.EventType type)
	{
		return getEventFunctionMap(klass).get(type);
	}
}
