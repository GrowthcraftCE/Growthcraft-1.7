package growthcraft.grapes;

import growthcraft.core.ConfigBase;

import net.minecraft.util.MathHelper;

public class GrcGrapesConfig extends ConfigBase
{
	@ConfigOption(catergory="Grape Vine", name="Grape Vine (Seedling) growth rate", desc="[Higher -> Slower]")
	public float grapeVineSeedlingGrowthRate = 25.0f;

	@ConfigOption(catergory="Grape Vine", name="Grape Vine (Trunk) growth rate", desc="[Higher -> Slower]")
	public float grapeVineTrunkGrowthRate = 25.0f;

	@ConfigOption(catergory="Grape Vine", name="Grape Leaves growth rate", desc="[Higher -> Slower]")
	public int grapeLeavesGrowthRate = 2;

	@ConfigOption(catergory="Grape Vine", name="Grape spawn rate", desc="[Higher -> Slower]")
	public int grapeSpawnRate = 5;

	@ConfigOption(catergory="Grape Vine", name="Support Growth Length", desc="How far can a grape vine extend?")
	public int grapeVineSupportedLength = 3;


	@ConfigOption(catergory="Drops", name="Grapes drop Minimum", desc="Minimum number of grapes to drop when a grape block is broken")
	public int grapesDropMin = 1;

	@ConfigOption(catergory="Drops", name="Grapes drop Maximum", desc="Maximum number of grapes to drop when a grape block is broken")
	public int grapesDropMax = 2;

	@ConfigOption(catergory="Drops", name="Bayanus Yeast rarity", desc="[Higher -> Rarer]")
	public int bayanusDropRarity = 20;

	@ConfigOption(catergory="Drops", name="Grape vine drop rarity", desc="[Higher -> Common]")
	public int vineGrapeDropRarity = 10;


	@ConfigOption(catergory="Pressing", name="Grape Wine press time", desc="[Higher -> Slower]")
	public int grapeWinePressingTime = 20;

	@ConfigOption(catergory="Pressing", name="Grape Wine press yield", desc="How many milli-buckets are created per grape?")
	public int grapeWinePressingYield = 40;


	@ConfigOption(catergory="Booze/Grape Wine", name="Grape Wine Color", desc="What color should Grape wine be?")
	public int grapeWineColor = 0x550E24;

	@ConfigOption(catergory="Booze/Ambrosia", name="Grape Wine Color", desc="What color should Ambrosia be?")
	public int ambrosiaColor = 0x43302E;

	@ConfigOption(catergory="Booze/Port Wine", name="Grape Wine Color", desc="What color should Port Wine be?")
	public int portWineColor = 0x4E2026;


	@ConfigOption(catergory="Brewing/Port Wine", name="Time", desc="[Higher -> Slower]")
	public int portWineBrewingTime = 20;

	@ConfigOption(catergory="Brewing/Port Wine", name="Yield", desc="How much port wine is produced per brew?")
	public int portWineBrewingYield = 40;


	@ConfigOption(catergory="Village", name="Generate Village Grape Vineyards", desc="Controls hop vineyards spawning in villages")
	public boolean generateGrapeVineyardStructure = true;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;


	@Override
	protected void postLoadConfig()
	{
		this.grapesDropMin = MathHelper.clamp_int(grapesDropMin, 0, grapesDropMax);
	}
}
