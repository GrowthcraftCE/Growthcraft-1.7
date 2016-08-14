package growthcraft.fishtrap.client;

import growthcraft.fishtrap.client.gui.GuiFishTrap;
import growthcraft.fishtrap.common.CommonProxy;
import growthcraft.fishtrap.GrowthCraftFishTrap;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		GrowthCraftFishTrap.guiProvider.register("grcfishtrap:fish_trap", GuiFishTrap.class);
	}
}
