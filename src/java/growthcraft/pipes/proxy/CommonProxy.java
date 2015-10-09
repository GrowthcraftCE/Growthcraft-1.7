package growthcraft.pipes.proxy;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.pipes.proxy.ClientProxy", serverSide="growthcraft.pipes.proxy.CommonProxy")
	public static CommonProxy INSTANCE;

	public void registerRenderers()
	{

	}
}
