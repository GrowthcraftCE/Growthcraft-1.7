package growthcraft.grapes.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.grapes.client.ClientProxy", serverSide="growthcraft.grapes.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders(){}
}
