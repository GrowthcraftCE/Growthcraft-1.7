package growthcraft.core.common;

public interface IModule
{
	// Pre-Initialization, create blocks and items here
	void preInit();
	// Called right after Pre-Initialization for basic registrations
	void register();
	// Initialization, use this step to setup extra internal stuff
	void init();
	// Post-Initialization, use this step to integrate with other mods
	void postInit();
}
