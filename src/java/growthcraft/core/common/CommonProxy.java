package growthcraft.core.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.core.client.ClientProxy", serverSide="growthcraft.core.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders() {}
}
