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
package growthcraft.api.core.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import growthcraft.api.core.freeze.FrozenObjectError;
import growthcraft.api.core.freeze.IFreezable;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;

// Container class storing sub modules
public class ModuleContainer implements IModule, IFreezable, ILoggable, Iterable<IModule>
{
	protected List<IModule> subModules = new ArrayList<IModule>();
	protected ILogger logger = NullLogger.INSTANCE;
	private boolean frozen;

	/**
	 * Returns an iterator for the module's submodules
	 */
	@Override
	public Iterator<IModule> iterator()
	{
		return subModules.iterator();
	}

	/**
	 * @return size of the module container
	 */
	public int size()
	{
		return subModules.size();
	}

	/**
	 * Prevents further external changes to the container by freezing it
	 */
	@Override
	public void freeze()
	{
		this.frozen = true;
		logger.info("ModuleContainer %s has froze, it will error the next time a Module is added.", this);
	}

	/**
	 * Is this container frozen?
	 *
	 * @return true, it cannot be modified, false otherwise
	 */
	@Override
	public boolean isFrozen()
	{
		return frozen;
	}

	/**
	 * Pokes the container, telling it that "I will modify you"
	 */
	public void touch()
	{
		if (isFrozen()) throw FrozenObjectError.newFor(this);
	}

	/**
	 * Adds a new module to the container, this WILL throw an error if the
	 * container is frozen and no longer accepts modules
	 *
	 * @param module - an IModule instance
	 */
	public void add(@Nonnull IModule module)
	{
		touch();

		subModules.add(module);
	}

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		touch();

		this.logger = l;
		for (IModule module : subModules)
		{
			if (module instanceof ILoggable)
			{
				((ILoggable)module).setLogger(l);
			}
		}
	}

	@Override
	public void preInit()
	{
		for (IModule module : subModules)
		{
			module.preInit();
		}
	}

	@Override
	public void register()
	{
		for (IModule module : subModules)
		{
			module.register();
		}
	}

	@Override
	public void init()
	{
		for (IModule module : subModules)
		{
			module.init();
		}
	}

	@Override
	public void postInit()
	{
		for (IModule module : subModules)
		{
			module.postInit();
		}
	}
}
