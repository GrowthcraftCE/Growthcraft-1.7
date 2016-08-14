package growthcraft.rice.client;

import growthcraft.rice.client.renderer.RenderRice;
import growthcraft.rice.common.CommonProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderingRegistry.registerBlockHandler(new RenderRice());
	}
}
