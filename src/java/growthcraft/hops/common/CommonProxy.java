package growthcraft.hops.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.hops.client.ClientProxy", serverSide="growthcraft.hops.common.CommonProxy")
	public static CommonProxy instance;

	public void init() {}
}
