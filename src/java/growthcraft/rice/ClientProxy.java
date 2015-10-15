package growthcraft.rice;

import growthcraft.rice.renderer.RenderRice;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderRice());
	}
}
