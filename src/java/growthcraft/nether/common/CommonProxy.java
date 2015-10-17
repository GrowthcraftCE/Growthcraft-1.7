package growthcraft.nether.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.nether.client.ClientProxy", serverSide="growthcraft.nether.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders() {}
}
