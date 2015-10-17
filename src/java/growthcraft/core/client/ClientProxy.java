package growthcraft.core.client;

import growthcraft.core.client.renderer.RenderBlockFruit;
import growthcraft.core.client.renderer.RenderFenceRope;
import growthcraft.core.client.renderer.RenderPaddy;
import growthcraft.core.client.renderer.RenderRope;
import growthcraft.core.common.CommonProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static int paddyRenderPass;

	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderPaddy());
		RenderingRegistry.registerBlockHandler(new RenderFenceRope());
		RenderingRegistry.registerBlockHandler(new RenderRope());
		RenderingRegistry.registerBlockHandler(new RenderBlockFruit());
	}
}
