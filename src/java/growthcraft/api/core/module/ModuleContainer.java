package growthcraft.api.core.module;

import java.util.List;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import growthcraft.api.core.freeze.FrozenObjectError;
import growthcraft.api.core.freeze.IFreezable;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;

// Container class storing sub modules
public class ModuleContainer implements IModule, IFreezable, ILoggable
{
	protected List<IModule> subModules = new ArrayList<IModule>();
	protected ILogger logger = NullLogger.INSTANCE;
	private boolean frozen;

	/**
	 * Prevents further external changes to the container by freezing it
	 */
	@Override
	public void freeze()
	{
		this.frozen = true;
		logger.info("ModuleContainer %s has been frozen, it will error the next time a Module is added.", this);
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
