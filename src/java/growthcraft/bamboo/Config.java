package growthcraft.bamboo;

import java.io.File;
import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public boolean bambooGenerateBiome = true;
	public boolean useBiomeDict = true;
	public int bambooBiomeID = 170;
	// Ocean, Plains, Forest, River, ForestHills, Jungle, JungleHills, JungleEdge,JungleM, JungleEdgeM
	public String bambooBiomesList = "0;1;4;7;18;21;22;23;149;151";
	public boolean generateBambooYard = false;
	public int bambooShootGrowthRate = 7;
	public int bambooStalkGrowthRate = 4;
	public int bambooTreeMaxHeight = 256;
	public int bambooTreeMinHeight = 12;
	public int bambooWorldGenDensity = 64;
	public int bambooWorldGenRarity = 32;

	protected void loadConfig()
	{
		this.bambooBiomeID = config.get("Biomes", "Bamboo Forest biome ID", bambooBiomeID).getInt();
		this.bambooBiomesList = config.get(Configuration.CATEGORY_GENERAL, "Biomes (IDs) That Generate Bamboos", bambooBiomesList, "Separate the IDs with ';' (without the quote marks)").getString();
		this.useBiomeDict = config.get(Configuration.CATEGORY_GENERAL, "Enable Biome Dictionary compatability?", useBiomeDict, "Default : true  || false = Disable").getBoolean();
		this.bambooGenerateBiome = config.get(Configuration.CATEGORY_GENERAL, "Generate Bamboo Forest biome?", bambooGenerateBiome, "Default : true  || false = Disable").getBoolean();
		this.bambooWorldGenRarity = config.get(Configuration.CATEGORY_GENERAL, "Bamboo WorldGen rarity", bambooWorldGenRarity, "[Higher -> Rarer] Default : " + bambooWorldGenRarity).getInt();
		this.bambooWorldGenDensity = config.get(Configuration.CATEGORY_GENERAL, "Bamboo WorldGen density", bambooWorldGenDensity, "[Higher -> Denser] Default : " + bambooWorldGenDensity).getInt();
		this.bambooStalkGrowthRate = config.get(Configuration.CATEGORY_GENERAL, "Bamboo Spread rate", bambooStalkGrowthRate, "[Higher -> Slower] Default : " + bambooStalkGrowthRate).getInt();
		this.bambooShootGrowthRate = config.get(Configuration.CATEGORY_GENERAL, "Bamboo Shoot growth rate", bambooShootGrowthRate, "[Higher -> Slower] Default : " + bambooShootGrowthRate).getInt();
		this.generateBambooYard = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Bamboo Yard", generateBambooYard, "Controls bamboo yard spawning in villages Default : " + generateBambooYard).getBoolean();
	}
}
