package growthcraft.core;

public class GrcCoreConfig extends ConfigBase
{
	// In case you don't have a wrench you can enable the amazing stick.
	@ConfigOption(name="Use Amazing Stick", desc="So, I heard you didn't have a wrench, we got you covered")
	public boolean useAmazingStick;


	@ConfigOption(catergory="Fluid Container", name="Bottle Capacity", desc="How much booze does a bottle hold?")
	public int bottleCapacity = 333;


	@ConfigOption(catergory="Integration", name="Enable Apple Core Integration", desc="Should we integrate with Apple Core (if available)?")
	public boolean enableAppleCoreIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Waila Integration", desc="Should we integrate with Waila (if available)?")
	public boolean enableWailaIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
