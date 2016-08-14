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
package growthcraft.core;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import growthcraft.api.core.log.ILogger;
import growthcraft.core.common.tileentity.feature.IInteractionObject;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Cross Ported from YATM
 */
public class GrcGuiProvider implements IGuiHandler
{
	public static class InvalidGuiElement extends IllegalStateException
	{
		public static final long serialVersionUID = 1L;

		public InvalidGuiElement(String str)
		{
			super(str);
		}
	}

	// Leave this Map empty on server, only the client should fill it
	@SuppressWarnings({"rawtypes"})
	public final Map<String, Class> guiMap = new HashMap<String, Class>();
	protected ILogger logger;

	public GrcGuiProvider(ILogger p_logger)
	{
		this.logger = p_logger;
	}

	/**
	 * Register your GUI class mappings here and have your tile entities import
	 * @param name name to register the gui as, prefix your mod domain, just because
	 * @param guiClass - the Class<Gui> to use, ensure it has a constructor for (InventoryPlayer, TileEntity), (IInventory, TileEntity) will work as well
	 */
	@SuppressWarnings({"rawtypes"})
	public void register(String name, Class guiClass)
	{
		if (guiMap.containsKey(name))
		{
			logger.warn("Overwriting Existing Gui mapping: %s with `%s`", name, guiClass);
		}
		guiMap.put(name, guiClass);
	}

	private String typeName(Object inventory)
	{
		if (inventory == null)
		{
			return "NULL";
		}
		return inventory.getClass().getName();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private Constructor findConstructor(Constructor[] c, InventoryPlayer inventory, Object te)
	{
		final Class teClass = te.getClass();
		final Class invClass = inventory.getClass();
		for (Constructor con : c)
		{
			final Class[] types = con.getParameterTypes();
			if (types.length == 2)
			{
				if (types[0].isAssignableFrom(invClass) && types[1].isAssignableFrom(teClass))
				{
					return con;
				}
			}
		}
		return null;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private Object createContainerInstance(@Nonnull Class containerClass, @Nonnull InventoryPlayer inventory, Object te)
	{
		try
		{
			final Constructor[] c = containerClass.getConstructors();
			if (c.length == 0)
			{
				throw new InvalidGuiElement("Invalid Gui Element Class " + containerClass.getName());
			}

			final Constructor target = findConstructor(c, inventory, te);

			if (target == null)
			{
				throw new IllegalStateException("Cannot find " + containerClass.getName() + "( " + this.typeName(inventory) + ", " + this.typeName(te) + " )");
			}

			return target.newInstance(inventory, te);
		}
		catch (Throwable t)
		{
			throw new IllegalStateException(t);
		}
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IInteractionObject)
		{
			final IInteractionObject iobj = (IInteractionObject)te;
			return iobj.createContainer(player.inventory, player);
		}
		else
		{
			logger.error("Container requested for TE but TE was not a IInteractionObject tile_entity=%s id=%d", te, id);
		}
		return null;
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IInteractionObject)
		{
			final IInteractionObject iobj = (IInteractionObject)te;
			final String guiId = iobj.getGuiID();
			final Class klass = guiMap.get(guiId);
			return createContainerInstance(klass, player.inventory, te);
		}
		return null;
	}
}
