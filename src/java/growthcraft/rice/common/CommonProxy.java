package growthcraft.rice.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.rice.client.ClientProxy", serverSide="growthcraft.rice.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders(){}
}
