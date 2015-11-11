package growthcraft.core.integration;

import cpw.mods.fml.common.event.FMLInterModComms;

// Because sometimes you don't want to do stupid stuff over and over again
public class WailaIntegrationBase extends ModIntegrationBase
{
	public WailaIntegrationBase(String parentMod)
	{
		super(parentMod, "Waila");
	}

	@Override
	public void doInit()
	{
		FMLInterModComms.sendMessage(modID, "register", getClass().getName() + ".register");
	}
}
