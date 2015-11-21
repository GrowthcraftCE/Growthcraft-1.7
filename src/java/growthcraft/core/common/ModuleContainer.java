package growthcraft.core.common;

import java.util.List;
import java.util.ArrayList;

import growthcraft.api.core.log.ILogger;

// Container class for Growthcraft sub modules
public class ModuleContainer extends GrcModuleBase
{
	protected List<IModule> subModules = new ArrayList<IModule>();

	public void add(IModule module)
	{
		subModules.add(module);
	}

	@Override
	public void setLogger(ILogger l)
	{
		super.setLogger(l);
		for (IModule module : subModules)
		{
			if (module instanceof GrcModuleBase)
			{
				((GrcModuleBase)module).setLogger(l);
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
