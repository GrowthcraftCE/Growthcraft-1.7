package growthcraft.hops;

import growthcraft.core.ConfigBase;

public class GrcHopsConfig extends ConfigBase
{
	@ConfigOption(catergory="Hop Vine", name="Hop (Vine) growth rate", desc="[Higher -> Slower]")
	public float hopVineGrowthRate = 25.0f;

	@ConfigOption(catergory="Hop Vine", name="Hop (Flower) spawn rate", desc="[Higher -> Slower]")
	public float hopVineFlowerSpawnRate = 40.0f;


	@ConfigOption(catergory="Drops", name="Hops vine drop rarity", desc="[Higher -> Common]")
	public int hopsVineDropRarity = 10;


	@ConfigOption(catergory="Booze/Hop Ale", name="Color", desc="What color should Hop Ale be?")
	public int hopAleColor = 0xCACA47;

	@ConfigOption(catergory="Booze/Lager", name="Color", desc="What color should Lager be?")
	public int lagerColor = 0x9F7851;


	@ConfigOption(catergory="Village", name="Generate Village Hop Vineyards", desc="Should we spawn Hop Vineyards in villages?")
	public boolean generateHopVineyardStructure = true;


	@ConfigOption(catergory="Integration", name="Enable Forestry Integration", desc="Should we integrate with Forestry (if available)?")
	public boolean enableForestryIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable MFR Integration", desc="Should we integrate with Mine Factory Reloaded (if available)?")
	public boolean enableMFRIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
