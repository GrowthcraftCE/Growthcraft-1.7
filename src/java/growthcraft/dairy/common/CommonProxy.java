package growthcraft.dairy.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.dairy.client.ClientProxy", serverSide="growthcraft.dairy.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders() {}
}
