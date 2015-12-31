package growthcraft.apples.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.apples.client.ClientProxy", serverSide="growthcraft.apples.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders() {}
}
