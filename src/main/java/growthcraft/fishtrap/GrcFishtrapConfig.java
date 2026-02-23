package growthcraft.fishtrap;

import growthcraft.core.ConfigBase;

public class GrcFishtrapConfig extends ConfigBase
{
	@ConfigOption(catergory="Fish Trap", name="Fish Trap catching rate", desc="[Higher -> Slower]")
	public float fishTrapCatchRate = 55.0f;

	@ConfigOption(catergory="Fish Trap", name="Enable Biome Dictionary compatability?")
	public boolean useBiomeDict = true;

	@ConfigOption(catergory="Fish Trap", name="Biomes (IDs) That Increases Fish Trap Productivity", desc="Separate the IDs with ';' (without the quote marks)")
	public String biomesList = "0;7;24";


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
