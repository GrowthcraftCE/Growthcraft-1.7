package growthcraft.nether;

import growthcraft.core.ConfigBase;

public class GrcNetherConfig extends ConfigBase
{
	@ConfigOption(catergory="Nether Pepper", name="Minimum Peppers Picked", desc="What is the minimum number of peppers picked?")
	public int minPepperPicked = 1;

	@ConfigOption(catergory="Nether Pepper", name="Maximum Peppers Picked", desc="What is the maximum number of peppers picked?")
	public int maxPepperPicked = 4;


	@ConfigOption(catergory="Malice Tree/Fruit", name="Drop Ripe Malice Fruit", desc="Should ripe Malice Fruits drop from the tree?")
	public boolean dropRipeMaliceFruit = true;

	@ConfigOption(catergory="Malice Tree/Fruit", name="Drop Chance", desc="Chance that a Malice Fruit will drop after it is ripe")
	public int maliceFruitDropChance = 8;

	@ConfigOption(catergory="Malice Tree/Fruit", name="Growth Rate", desc="Chance that a Malice Fruit will advance one stage when ticked [Higher -> Less likely]")
	public int maliceFruitGrowthRate = 8;

	@ConfigOption(catergory="Malice Tree/Leaves", name="Growth Rate", desc="Chance that malice leaves will spawn a Malice Fruit [Higher -> Less likely]")
	public int maliceLeavesGrowthRate = 25;

	@ConfigOption(catergory="Malice Tree/Sapling", name="Growth Rate", desc="Chance that malice sapling will grow into a tree [Higher -> Less likely]")
	public int maliceSaplingGrowthRate = 7;


	@ConfigOption(catergory="Booze/Malice Cider", name="Pressing Time", desc="How long does it take to press a Malice Fruit?")
	public int maliceCiderPressingTime = 20;

	@ConfigOption(catergory="Booze/Malice Cider", name="Yield", desc="How much Malice Cider is produced from a Malice Fruit?")
	public int maliceCiderYield = 40;

	@ConfigOption(catergory="Booze/Malice Cider", name="Color", desc="What color is Malice Cider? (use a RGB24 integer (that is 8 bits per channel, 3 channels))")
	public int maliceCiderColor = 0xA99F6E;

	@ConfigOption(catergory="Booze/Amrita", name="Color", desc="What color is Amrita? (use a RGB24 integer (that is 8 bits per channel, 3 channels))")
	public int amritaColor = 0xFF4800;

	@ConfigOption(catergory="Booze/Gelid Booze", name="Color", desc="What color is Gelid Booze? (use a RGB24 integer (that is 8 bits per channel, 3 channels))")
	public int gelidBoozeColor = 0x89E9F0;

	@ConfigOption(catergory="Booze/Vile Slop", name="Color", desc="What color is Vile Slop? (use a RGB24 integer (that is 8 bits per channel, 3 channels))")
	public int vileSlopColor = 0x3F2A14;

	@ConfigOption(catergory="Booze/Fire Brandy", name="Brewing Time", desc="How long does it take to brew a Cinderrot?")
	public int fireBrandyBrewTime = 20;

	@ConfigOption(catergory="Booze/Fire Brandy", name="Yield", desc="How much Fire Brandy is produced from a Cinderrot?")
	public int fireBrandyYield = 40;

	@ConfigOption(catergory="Booze/Fire Brandy", name="Color", desc="What color is Fire Brandy? (use a RGB24 integer (that is 8 bits per channel, 3 channels))")
	public int fireBrandyColor = 0xB22222;


	@ConfigOption(catergory="Vegetation/Cinderrot", name="Spread Rate", desc="How quickly does Cinderrot spread? [Higher -> Faster]")
	public float cinderrotSpreadRate = 0.1F;

	@ConfigOption(catergory="Vegetation/Baals Rot", name="Spread Rate", desc="How quickly does Baals Rot spread? [Higher -> Faster]")
	public float baalsRotSpreadRate = 0.1F;

	@ConfigOption(catergory="Vegetation/Muertecap", name="Spread Rate", desc="How quickly does Muertecap spread? [Higher -> Faster]")
	public float muertecapSpreadRate = 0.1F;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;


	public final int paddyFieldMax = 7;
}
