package growthcraft.bees;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;

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
