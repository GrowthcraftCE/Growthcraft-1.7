package growthcraft.grapes;

import growthcraft.core.GrowthCraftCore;
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
