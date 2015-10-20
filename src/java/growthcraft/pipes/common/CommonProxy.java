package growthcraft.pipes.common;

import cpw.mods.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.pipes.client.ClientProxy", serverSide="growthcraft.pipes.common.CommonProxy")
	public static CommonProxy instance;

	public void registerRenderers()
	{

	}
}
