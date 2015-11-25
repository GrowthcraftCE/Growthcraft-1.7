package growthcraft.dairy;

import growthcraft.core.ConfigBase;

public class GrcDairyConfig extends ConfigBase
{
	@ConfigOption(catergory="Booze/Milk", name="Color", desc="What color is milk?")
	public int milkColor = 0xFFFFFF;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
