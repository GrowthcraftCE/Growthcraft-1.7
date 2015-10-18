package growthcraft.nether;

import growthcraft.core.ConfigBase;
//import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public int minPepperPicked = 1;
	public int maxPepperPicked = 4;
	public boolean dropRipeMaliceFruit = true;
	public int maliceFruitDropChance = 8;
	public int maliceFruitGrowthRate = 8;
	public int maliceLeavesGrowthRate = 25;
	public int maliceSaplingGrowthRate = 7;
	public int maliceCiderPressingTime = 20;
	public int fireBrandyBrewTime = 20;

	public float cinderrotSpreadRate = 0.2F;

	public final int paddyFieldMax = 7;
	public final int fireBrandyColor = 0xB22222;
	public final int maliceCiderColor = 0xA99F6E;

	protected void loadConfig()
	{
		/* Nothing here but us chickens */
	}
}
