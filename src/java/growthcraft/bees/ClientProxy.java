package growthcraft.bees;

import growthcraft.bees.renderer.RenderBeeBox;
import growthcraft.bees.renderer.RenderBeeHive;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderBeeBox());
		RenderingRegistry.registerBlockHandler(new RenderBeeHive());
	}

	@Override
	public void initSounds()
	{//MinecraftForge.EVENT_BUS.register(new SoundLoadEventBees());
	}
}
