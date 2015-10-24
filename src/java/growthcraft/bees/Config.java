package growthcraft.bees;

import growthcraft.core.ConfigBase;

public class Config extends ConfigBase
{
	@ConfigOption(catergory="World Gen", name="Biomes (IDs) That Generate Beehives", desc="Separate the IDs with ';' (without the quote marks)")
	public String beeBiomesList = "1;4;18;27;28;129;132;155;156";

	@ConfigOption(catergory="World Gen",name="Enable Biome Dictionary compatability?", desc="Default : true  || false = Disable")
	public boolean useBiomeDict = true;

	@ConfigOption(catergory="World Gen",name="Bee Hive WorldGen density", desc="[Higher -> Denser]")
	public int beeWorldGenDensity = 2;


	@ConfigOption(catergory="Bee Box", name="Honeycomb spawn rate", desc="[Higher -> Slower]")
	public float beeBoxHoneyCombSpawnRate = 18.75f;

	@ConfigOption(catergory="Bee Box", name="Honey spawn rate", desc="[Higher -> Slower]")
	public float beeBoxHoneySpawnRate = 6.25f;

	@ConfigOption(catergory="Bee Box", name="Bee spawn rate", desc="[Higher -> Slower]")
	public float beeBoxBeeSpawnRate = 6.25f;

	@ConfigOption(catergory="Bee Box", name="Flower spawn rate", desc="[Higher -> Slower]")
	public float beeBoxFlowerSpawnRate = 6.25f;


	@ConfigOption(catergory="Booze", name="Honey Mead Color", desc="What color should honey mead be?")
	public int honeyMeadColor = 0xA3610C;


	@ConfigOption(catergory="Village", name="Apiarist ID")
	public int villagerApiaristID = 14;

	@ConfigOption(catergory="Village", name="Spawn Village Apiarist Structure", desc="Should the apiarist structure be generated in villages?")
	public boolean generateApiaristStructure;


	@ConfigOption(catergory="Integration", name="Enable Forestry Integration", desc="Should we integrate with Forestry (if available)?")
	public boolean enableForestryIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
