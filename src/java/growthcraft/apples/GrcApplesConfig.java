package growthcraft.apples;

import growthcraft.core.ConfigBase;

public class GrcApplesConfig extends ConfigBase
{
	// Should a ripe apple fall as an item?
	@ConfigOption(catergory="Apple Tree", name="Allow natural apple falling?", desc="Should ripe apples fall from the tree?")
	public boolean dropRipeApples = true;

	@ConfigOption(catergory="Apple Tree", name="Apple natural falling rate", desc="[Higher -> Less Likely to Fall]")
	public int appleDropChance = 8;

	@ConfigOption(catergory="Apple Tree", name="Apple Leaves apple spawn rate", desc="[Higher -> Slower]")
	public int appleLeavesGrowthRate = 25;

	@ConfigOption(catergory="Apple Tree", name="Apple growth rate", desc="[Higher -> Slower]")
	public int appleGrowthRate = 8;

	@ConfigOption(catergory="Apple Tree", name="Apple Sapling growth rate", desc="[Higher -> Slower]")
	public int appleSaplingGrowthRate = 7;


	@ConfigOption(catergory="Fruit Press", name="Apple Cider press time", desc="[Higher -> Slower]")
	public int appleCiderPressingTime = 20;

	@ConfigOption(catergory="Fruit Press", name="Apple Cider press yield", desc="How many milli-buckets are created per apple?")
	public int appleCiderPressYield = 40;


	@ConfigOption(catergory="Booze", name="Apple Cider Color", desc="What color should apple cider be?")
	public int appleCiderColor = 0x855425;


	@ConfigOption(catergory="Village", name="Generate Village Apple Farms", desc="Should we spawn Apple Farms in Villages?")
	public boolean generateAppleFarms;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
