package growthcraft.hops.client;

import growthcraft.hops.client.renderer.RenderHops;
import growthcraft.hops.common.CommonProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderingRegistry.registerBlockHandler(new RenderHops());
	}
}
