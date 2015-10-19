package growthcraft.grapes;

import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public float grapeVineSeedlingGrowthRate = 25.0f;
	public float grapeVineTrunkGrowthRate = 25.0f;
	public int grapeLeavesGrowthRate = 2;
	public int grapeSpawnRate = 5;
	public int vineGrapeDropRarity = 10;
	public int grapeWinePressingTime = 20;
	public boolean generateGrapeVineyardStructure = true;
	public boolean enableThaumcraftIntegration = true;
	public final int grapeWineColor = 0x550E24;
	public final int grapeSupportedLength = 3;

	protected void loadConfig()
	{
		this.grapeVineSeedlingGrowthRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Grape Vine (Seedling) growth rate", (double)grapeVineSeedlingGrowthRate, "[Higher -> Slower] Default : " + grapeVineSeedlingGrowthRate).getDouble();
		this.grapeVineTrunkGrowthRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Grape Vine (Trunk) growth rate", (double)grapeVineTrunkGrowthRate, "[Higher -> Slower] Default : " + grapeVineTrunkGrowthRate).getDouble();
		this.grapeLeavesGrowthRate = config.get(Configuration.CATEGORY_GENERAL, "Grape Leaves growth rate", grapeLeavesGrowthRate, "[Higher -> Slower] Default : " + grapeLeavesGrowthRate).getInt();
		this.grapeSpawnRate = config.get(Configuration.CATEGORY_GENERAL, "Grape spawn rate", grapeSpawnRate, "[Higher -> Slower] Default : " + grapeSpawnRate).getInt();
		this.vineGrapeDropRarity = config.get(Configuration.CATEGORY_GENERAL, "Grape vine drop rarity", vineGrapeDropRarity, "[Lower -> Rarer] Default : " + vineGrapeDropRarity).getInt();
		this.grapeWinePressingTime = config.get(Configuration.CATEGORY_GENERAL, "Grape Wine press time", grapeWinePressingTime, "[Higher -> Slower] Default : " + grapeWinePressingTime).getInt();
		this.generateGrapeVineyardStructure = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Grape Vineyards", generateGrapeVineyardStructure, "Controls hop vineyards spawning in villages Default : " + generateGrapeVineyardStructure).getBoolean();
		this.enableThaumcraftIntegration = config.get(Configuration.CATEGORY_GENERAL, "Enable Thaumcraft Integration", enableThaumcraftIntegration, "Should we integrate with Thaumcraft (if available); Default: " + enableThaumcraftIntegration).getBoolean();
	}
}
