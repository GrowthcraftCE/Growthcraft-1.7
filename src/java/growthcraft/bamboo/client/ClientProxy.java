package growthcraft.bamboo.client;

import growthcraft.bamboo.client.renderer.RenderBamboo;
import growthcraft.bamboo.client.renderer.RenderBambooFence;
import growthcraft.bamboo.client.renderer.RenderBambooRaft;
import growthcraft.bamboo.client.renderer.RenderBambooScaffold;
import growthcraft.bamboo.client.renderer.RenderBambooWall;
import growthcraft.bamboo.common.CommonProxy;
import growthcraft.bamboo.common.entity.EntityBambooRaft;

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
