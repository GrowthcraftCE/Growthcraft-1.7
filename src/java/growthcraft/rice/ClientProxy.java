package growthcraft.rice;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static int renderPass;

	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderRice());
		RenderingRegistry.registerBlockHandler(new RenderPaddy());
	}
}
