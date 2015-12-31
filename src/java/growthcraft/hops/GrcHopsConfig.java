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


	@ConfigOption(catergory="Brewing", name="Hop Ale (no hops) brew time", desc="[Higher -> Slower]")
	public int hopAleBrewTime = 20;

	@ConfigOption(catergory="Brewing", name="Hop Ale (no hops) brew yield", desc="How many milli-buckets are created per grain?")
	public int hopAleBrewYield = 40;

	@ConfigOption(catergory="Brewing", name="Hop Ale (hopped) brew time", desc="[Higher -> Slower]")
	public int hopAleHoppedBrewTime = 20;

	@ConfigOption(catergory="Brewing", name="Hop Ale (hopped) brew yield", desc="How many milli-buckets are created per hop?")
	public int hopAleHoppedBrewYield = 40;

	@ConfigOption(catergory="Brewing/Lager", name="Brew time", desc="[Higher -> Slower]")
	public int lagerBrewTime = 20;

	@ConfigOption(catergory="Brewing/Lager", name="Brew yield", desc="How many milli-buckets are created per hop?")
	public int lagerBrewYield = 40;


	@ConfigOption(catergory="Booze/Hop Ale", name="Color", desc="What color should Hop Ale be?")
	public int hopAleColor = 0xCACA47;

	@ConfigOption(catergory="Booze/Lager", name="Color", desc="What color should Lager be?")
	public int lagerColor = 0x9F7851;


	@ConfigOption(catergory="Village", name="Generate Village Hop Vineyards", desc="Should we spawn Hop Vineyards in villages?")
	public boolean generateHopVineyardStructure = true;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
