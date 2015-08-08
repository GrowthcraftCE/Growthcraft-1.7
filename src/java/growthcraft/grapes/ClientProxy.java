package growthcraft.grapes;

import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.renderer.RenderGrape;
import growthcraft.grapes.renderer.RenderGrapeLeaves;
import growthcraft.grapes.renderer.RenderGrapeVine1;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderGrapeVine1());
		RenderingRegistry.registerBlockHandler(new RenderGrapeLeaves());
		RenderingRegistry.registerBlockHandler(new RenderGrape());
	}
}
