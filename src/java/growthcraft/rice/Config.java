package growthcraft.rice;

import java.io.File;
import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public float riceGrowthRate = 25.0f;
	public int riceSeedDropRarity = 3;
	public int riceSakeBrewingTime = 20;
	public boolean generateRiceFieldStructure = false;
	public final int paddyFieldMax = 7;
	public final int riceSakeColor = 0xE9EFF7;

	protected void loadConfig()
	{
		this.riceGrowthRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Rice growth rate", (double)riceGrowthRate, "[Higher -> Slower] Default : " + riceGrowthRate).getDouble();
		this.riceSeedDropRarity = config.get(Configuration.CATEGORY_GENERAL, "Rice grass drop rarity", riceSeedDropRarity, "[Lower -> Rarer] Default : " + riceSeedDropRarity).getInt();
		this.riceSakeBrewingTime = config.get(Configuration.CATEGORY_GENERAL, "Rice Sake brew time", riceSakeBrewingTime, "[Higher -> Slower] Default : " + riceSakeBrewingTime).getInt();
		this.generateRiceFieldStructure = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Rice Fields", generateRiceFieldStructure, "Controls rice field spawning in villages Default : " + generateRiceFieldStructure).getBoolean();
	}
}
