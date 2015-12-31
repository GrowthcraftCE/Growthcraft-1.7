package growthcraft.fishtrap.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.fishtrap.client.ClientProxy", serverSide="growthcraft.fishtrap.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders(){}
}
