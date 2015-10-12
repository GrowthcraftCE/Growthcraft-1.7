package growthcraft.hops;

import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public float hopVineGrowthRate = 25.0f;
	public float hopVineFlowerSpawnRate = 40.0f;
	public int hopsVineDropRarity = 10;
	public int hopAleBrewTime = 20;
	public int hopAleHoppedBrewTime = 20;
	public boolean generateHopVineyardStructure = true;
	public final int hopAleColor = 0xCACA47;

	protected void loadConfig()
	{
		this.hopVineGrowthRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Hop (Vine) growth rate", (double)hopVineGrowthRate, "[Higher -> Slower] Default : " + hopVineGrowthRate).getDouble();
		this.hopVineFlowerSpawnRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Hop (Flower) spawn rate", (double)hopVineFlowerSpawnRate, "[Higher -> Slower] Default : " + hopVineFlowerSpawnRate).getDouble();
		this.hopsVineDropRarity = config.get(Configuration.CATEGORY_GENERAL, "Hops vine drop rarity", hopsVineDropRarity, "[Lower -> Rarer] Default : " + hopsVineDropRarity).getInt();
		this.hopAleBrewTime = config.get(Configuration.CATEGORY_GENERAL, "Hop Ale (no hops) brew time", hopAleBrewTime, "[Higher -> Slower] Default : " + hopAleBrewTime).getInt();
		this.hopAleHoppedBrewTime = config.get(Configuration.CATEGORY_GENERAL, "Hop Ale (hopped) brew time", hopAleHoppedBrewTime, "[Higher -> Slower] Default : " + hopAleHoppedBrewTime).getInt();
		this.generateHopVineyardStructure = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Hop Vineyards", generateHopVineyardStructure, "Controls hop vineyards spawning in villages Default : " + generateHopVineyardStructure).getBoolean();
	}
}
