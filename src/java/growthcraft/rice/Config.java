package growthcraft.rice;

import growthcraft.core.ConfigBase;

public class Config extends ConfigBase
{
	@ConfigOption(catergory="Growth Rates", name="Rice growth rate", desc="[Higher -> Slower]")
	public float riceGrowthRate = 25.0f;


	@ConfigOption(catergory="Drops", name="Rice grass drop rarity", desc="[Higher -> Common]")
	public int riceSeedDropRarity = 3;


	@ConfigOption(catergory="Brewing", name="Rice Sake brew time", desc="[Higher -> Slower]")
	public int riceSakeBrewingTime = 20;


	@ConfigOption(catergory="Booze", name="Rice Sake Color", desc="What color should sake be?")
	public int riceSakeColor = 0xE9EFF7;


	@ConfigOption(catergory="Village", name="Generate Village Rice Fields", desc="Should we spawn rice fields in villages?")
	public boolean generateRiceFieldStructure;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;


	public final int paddyFieldMax = 7;
}
