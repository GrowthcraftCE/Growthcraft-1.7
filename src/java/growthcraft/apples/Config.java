package growthcraft.apples;

import java.io.File;
import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	// Should a ripe apple fall as an item?
	public boolean dropRipeApples = true;
	public int appleDropChance = 8;
	public int appleGrowthRate = 8;
	public int appleLeavesGrowthRate = 25;
	public int appleSaplingGrowthRate = 7;
	public int appleCiderPressingTime = 20;
	public boolean generateAppleFarms = false;
	public final int appleCiderColor = 0x855425;

	protected void loadConfig()
	{
		this.appleGrowthRate = config.get(Configuration.CATEGORY_GENERAL, "Apple growth rate", appleGrowthRate, "[Higher -> Slower] Default : " + appleGrowthRate).getInt();
		this.dropRipeApples = config.get(Configuration.CATEGORY_GENERAL, "Allow natural apple falling?", dropRipeApples, "Default : " + dropRipeApples).getBoolean();
		this.appleDropChance = config.get(Configuration.CATEGORY_GENERAL, "Apple natural falling rate", appleDropChance, "[Higher -> Slower] Default : " + appleDropChance).getInt();
		this.appleLeavesGrowthRate = config.get(Configuration.CATEGORY_GENERAL, "Apple Leaves apple spawn rate", appleLeavesGrowthRate, "[Higher -> Slower] Default : " + appleLeavesGrowthRate).getInt();
		this.appleSaplingGrowthRate = config.get(Configuration.CATEGORY_GENERAL, "Apple Sapling growth rate", appleSaplingGrowthRate, "[Higher -> Slower] Default : " + appleSaplingGrowthRate).getInt();
		this.appleCiderPressingTime = config.get(Configuration.CATEGORY_GENERAL, "Apple Cider press time", appleCiderPressingTime, "[Higher -> Slower] Default : " + appleCiderPressingTime).getInt();
		this.generateAppleFarms = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Apple Farms", generateAppleFarms, "Controls apple farms spawning in villages Default : " + generateAppleFarms).getBoolean();
	}
}
