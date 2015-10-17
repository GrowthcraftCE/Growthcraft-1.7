package growthcraft.nether;

import growthcraft.core.ConfigBase;
//import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public int minPepperPicked = 1;
	public int maxPepperPicked = 4;
	public int maliceLeavesGrowthRate = 25;

	public final int paddyFieldMax = 7;
	public final int fireBrandyColor = 0xD43427;
	public final int maliceCiderColor = 0xA99F6E;

	protected void loadConfig()
	{
		/* Nothing here but us chickens */
	}
}
