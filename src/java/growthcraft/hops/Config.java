package growthcraft.hops;

import growthcraft.core.ConfigBase;

public class Config extends ConfigBase
{
	@ConfigOption(catergory="Hop Vine", name="Hop (Vine) growth rate", desc="[Higher -> Slower]")
	public float hopVineGrowthRate = 25.0f;

	@ConfigOption(catergory="Hop Vine", name="Hop (Flower) spawn rate", desc="[Higher -> Slower]")
	public float hopVineFlowerSpawnRate = 40.0f;


	@ConfigOption(catergory="Drops", name="Hops vine drop rarity", desc="[Higher -> Rarer]")
	public int hopsVineDropRarity = 10;


	@ConfigOption(catergory="Brewing", name="Hop Ale (no hops) brew time", desc="[Higher -> Slower]")
	public int hopAleBrewTime = 20;

	@ConfigOption(catergory="Brewing", name="Hop Ale (hopped) brew time", desc="[Higher -> Slower]")
	public int hopAleHoppedBrewTime = 20;


	@ConfigOption(catergory="Booze", name="Hop Ale Color", desc="What color should ale be?")
	public int hopAleColor = 0xCACA47;


	@ConfigOption(catergory="Village", name="Generate Village Hop Vineyards", desc="Should we spawn Hop Vineyards in villages?")
	public boolean generateHopVineyardStructure = true;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
