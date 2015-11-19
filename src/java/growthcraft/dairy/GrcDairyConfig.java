package growthcraft.dairy;

import growthcraft.core.ConfigBase;

public class GrcDairyConfig extends ConfigBase
{
	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
