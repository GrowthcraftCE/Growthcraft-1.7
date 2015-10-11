package growthcraft.core.integration;

import cpw.mods.fml.common.event.FMLInterModComms;

// Because sometimes you don't want to do stupid stuff over and over again
public class WailaIntegrationBase
{
	public WailaIntegrationBase()
	{
		FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".register");
	}
}
