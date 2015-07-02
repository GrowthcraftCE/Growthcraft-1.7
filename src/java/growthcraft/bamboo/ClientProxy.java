package growthcraft.bamboo;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderBamboo());
		RenderingRegistry.registerBlockHandler(new RenderBambooFence());
		RenderingRegistry.registerBlockHandler(new RenderBambooWall());
		RenderingRegistry.registerBlockHandler(new RenderBambooScaffold());

		RenderingRegistry.registerEntityRenderingHandler(EntityBambooRaft.class, new RenderBambooRaft());
	}
}
