package growthcraft.core.common;

import java.util.List;
import java.util.ArrayList;

// Base class for Growthcraft Mods
public class ModuleContainer implements IModule
{
	protected List<IModule> subModules = new ArrayList<IModule>();

	public void add(IModule module)
	{
		subModules.add(module);
	}

	public void preInit()
	{
		for (IModule module : subModules)
		{
			module.preInit();
		}
	}

	public void register()
	{
		for (IModule module : subModules)
		{
			module.register();
		}
	}

	public void init()
	{
		for (IModule module : subModules)
		{
			module.init();
		}
	}

	public void postInit()
	{
		for (IModule module : subModules)
		{
			module.postInit();
		}
	}
}
