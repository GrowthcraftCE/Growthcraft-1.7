package growthcraft.bees.client;

import growthcraft.bees.client.gui.GuiBeeBox;
import growthcraft.bees.client.renderer.RenderBeeBox;
import growthcraft.bees.client.renderer.RenderBeeHive;
import growthcraft.bees.common.CommonProxy;
import growthcraft.bees.GrowthCraftBees;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy
{
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderBeeBox());
		RenderingRegistry.registerBlockHandler(new RenderBeeHive());
	}

	@Override
	public void registerVillagerSkin()
	{
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftBees.getConfig().villagerApiaristID, new ResourceLocation("grcbees" , "textures/entity/apiarist.png"));
	}

	@Override
	public void init()
	{
		super.init();
		initRenders();
		GrowthCraftBees.guiProvider.register("grcbees:bee_box", GuiBeeBox.class);
	}
}
