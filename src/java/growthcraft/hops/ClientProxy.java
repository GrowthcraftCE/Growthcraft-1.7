package growthcraft.hops;

import growthcraft.hops.renderer.RenderHops;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderHops());
	}
}
