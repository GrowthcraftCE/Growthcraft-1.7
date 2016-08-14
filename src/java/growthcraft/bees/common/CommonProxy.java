package growthcraft.bees.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.bees.client.ClientProxy", serverSide="growthcraft.bees.common.CommonProxy")
	public static CommonProxy instance;

	public void init() {}
	public void registerVillagerSkin(){}
}
