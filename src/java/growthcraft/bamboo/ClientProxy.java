package growthcraft.bamboo;

import growthcraft.bamboo.entity.EntityBambooRaft;
import growthcraft.bamboo.renderer.RenderBamboo;
import growthcraft.bamboo.renderer.RenderBambooFence;
import growthcraft.bamboo.renderer.RenderBambooWall;
import growthcraft.bamboo.renderer.RenderBambooScaffold;
import growthcraft.bamboo.renderer.RenderBambooRaft;

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
