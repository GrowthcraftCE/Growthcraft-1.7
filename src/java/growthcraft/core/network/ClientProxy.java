package growthcraft.core.network;

import growthcraft.core.renderer.RenderFenceRope;
import growthcraft.core.renderer.RenderRope;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderFenceRope());
		RenderingRegistry.registerBlockHandler(new RenderRope());
	}
}
