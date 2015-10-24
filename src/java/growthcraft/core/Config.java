package growthcraft.core;

public class Config extends ConfigBase
{
	// In case you don't have a wrench you can enable the amazing stick.
	@ConfigOption(name="Use Amazing Stick", desc="So, I heard you didn't have a wrench, we got you covered")
	public boolean useAmazingStick;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
