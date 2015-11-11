package growthcraft.core.common;

public interface IModule
{
	// Pre-Initialization, create blocks and items here
	public void preInit();
	// Called right after Pre-Initialization for basic registrations
	public void register();
	// Initialization, use this step to setup extra internal stuff
	public void init();
	// Post-Initialization, use this step to integrate with other mods
	public void postInit();
}
