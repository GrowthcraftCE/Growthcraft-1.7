package growthcraft.cellar;

import java.io.File;
import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public int potionTipsyID = 50;
	public int villagerBrewerID = 10;
	public int fermentSpeed = 24000;
	public final int fruitPressMaxCap = 1000;
	public final int fermentBarrelMaxCap = 3000;
	public final int brewKettleMaxCap = 1000;

	public Config(File dirname, String filename)
	{
		super(dirname, filename);
	}

	protected void loadConfig()
	{
		this.potionTipsyID = config.get("Potions", "Potion Tipsy ID", 50).getInt();
		this.villagerBrewerID = config.get("Villager", "Brewer ID", 10).getInt();
		this.fermentSpeed = config.get(Configuration.CATEGORY_GENERAL, "Ferment Barrel fermenting time", fermentSpeed, "[Higher -> Slower] Default : " + fermentSpeed).getInt();
	}
}
