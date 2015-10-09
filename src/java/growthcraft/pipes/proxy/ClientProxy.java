package growthcraft.pipes.proxy;

import growthcraft.pipes.client.render.TileEntityPipeRenderer;
import growthcraft.pipes.client.render.RenderPipe;
import growthcraft.pipes.tileentity.TileEntityPipeBase;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerBlockHandler(new RenderPipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipeBase.class, new TileEntityPipeRenderer());
	}
}
