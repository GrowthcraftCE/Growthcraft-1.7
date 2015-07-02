package growthcraft.core.network;

import growthcraft.core.render.RenderFenceRope;
import growthcraft.core.render.RenderRope;
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
