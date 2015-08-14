package growthcraft.bees;

import growthcraft.bees.renderer.RenderBeeBox;
import growthcraft.bees.renderer.RenderBeeHive;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.ResourceLocation;
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

	@Override
	public void registerVillagerSkin()
	{
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftBees.villagerApiarist_id, new ResourceLocation("grcbees" , "textures/entity/apiarist.png"));
	}
}
