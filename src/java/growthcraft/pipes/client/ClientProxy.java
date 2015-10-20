package growthcraft.pipes.client;

import growthcraft.pipes.client.renderer.RenderPipe;
import growthcraft.pipes.client.renderer.TileEntityPipeRenderer;
import growthcraft.pipes.common.CommonProxy;
import growthcraft.pipes.common.tileentity.TileEntityPipeBase;

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
