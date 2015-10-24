package growthcraft.grapes;

import growthcraft.core.ConfigBase;

import net.minecraft.util.MathHelper;

public class Config extends ConfigBase
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

	@ConfigOption(catergory="Drops", name="Grape vine drop rarity", desc="[Higher -> Rarer]")
	public int vineGrapeDropRarity = 10;


	@ConfigOption(catergory="Pressing", name="Grape Wine press time", desc="[Higher -> Slower]")
	public int grapeWinePressingTime = 20;


	@ConfigOption(catergory="Booze", name="Grape Wine Color", desc="What color should grape wine be?")
	public int grapeWineColor = 0x550E24;


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
