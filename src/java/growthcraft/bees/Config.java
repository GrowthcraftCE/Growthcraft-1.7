package growthcraft.bees;

import java.io.File;
import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public int villagerApiaristID = 14;
	public String beeBiomesList = "1;4;18;27;28;129;132;155;156";
	public boolean useBiomeDict = true;
	public int beeWorldGenDensity = 2;
	public float beeBoxHoneyCombSpawnRate = 18.75f;
	public float beeBoxHoneySpawnRate = 6.25f;
	public float beeBoxBeeSpawnRate = 6.25f;
	public float beeBoxFlowerSpawnRate = 6.25f;
	public boolean generateApiaristStructure = false;
	public final int honeyMeadColor = 0xA3610C;

	protected void loadConfig()
	{
		this.villagerApiaristID = config.get("Villager", "Apiarist ID", villagerApiaristID).getInt();
		this.beeBiomesList = config.get(Configuration.CATEGORY_GENERAL, "Biomes (IDs) That Generate Beehives", beeBiomesList, "Separate the IDs with ';' (without the quote marks)").getString();
		this.useBiomeDict = config.get(Configuration.CATEGORY_GENERAL, "Enable Biome Dictionary compatability?", useBiomeDict, "Default : true  || false = Disable").getBoolean();
		this.beeWorldGenDensity = config.get(Configuration.CATEGORY_GENERAL, "Bee Hive WorldGen density", beeWorldGenDensity, "[Higher -> Denser] Default : " + beeWorldGenDensity).getInt();
		this.beeBoxHoneyCombSpawnRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Bee Box Honeycomb spawn rate", (double)beeBoxHoneyCombSpawnRate, "[Higher -> Slower] Default : " + beeBoxHoneyCombSpawnRate).getDouble();
		this.beeBoxFlowerSpawnRate =
			this.beeBoxBeeSpawnRate =
			this.beeBoxHoneySpawnRate = (float)config.get(Configuration.CATEGORY_GENERAL, "Bee Box Honey and Bee spawn rate", (double)beeBoxHoneySpawnRate, "[Higher -> Slower] Default : " + beeBoxHoneySpawnRate).getDouble();
		this.generateApiaristStructure = config.get(Configuration.CATEGORY_GENERAL, "Spawn Village Apiarist Structure", generateApiaristStructure, "Should the apiarist structure be generated in villages? : " + generateApiaristStructure).getBoolean();
	}
}
