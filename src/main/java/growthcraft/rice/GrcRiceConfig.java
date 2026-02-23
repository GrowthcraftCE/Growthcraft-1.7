package growthcraft.rice;

import growthcraft.core.ConfigBase;

public class GrcRiceConfig extends ConfigBase
{
	@ConfigOption(catergory="Growth Rates", name="Rice growth rate", desc="[Higher -> Slower]")
	public float riceGrowthRate = 25.0f;


	@ConfigOption(catergory="Drops", name="Rice grass drop rarity", desc="[Higher -> Common]")
	public int riceSeedDropRarity = 3;


	@ConfigOption(catergory="Booze", name="Rice Sake Color", desc="What color should Sake be?")
	public int riceSakeColor = 0xE9EFF7;

	@ConfigOption(catergory="Booze", name="Divine Rice Sake Color", desc="What color should Divine Sake be?")
	public int riceSakeDivineColor = 0xFFFACD;


	@ConfigOption(catergory="Villager", name="Enabled", desc="Should we register Village Generation, and Villager Trades?")
	public boolean enableVillageGen = true;

	@ConfigOption(catergory="Village", name="Generate Village Rice Fields", desc="Should we spawn rice fields in villages?")
	public boolean generateRiceFieldStructure;


	@ConfigOption(catergory="Integration", name="Enable Forestry Integration", desc="Should we integrate with Forestry (if available)?")
	public boolean enableForestryIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable MFR Integration", desc="Should we integrate with Mine Factory Reloaded (if available)?")
	public boolean enableMFRIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;


	public final int paddyFieldMax = 7;
}
