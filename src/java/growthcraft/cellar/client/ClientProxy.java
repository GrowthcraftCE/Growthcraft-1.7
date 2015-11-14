package growthcraft.cellar.client;

import growthcraft.cellar.client.render.RenderBrewKettle;
import growthcraft.cellar.client.render.RenderFermentBarrel;
import growthcraft.cellar.client.render.RenderFermentJar;
import growthcraft.cellar.client.render.RenderFruitPress;
import growthcraft.cellar.client.render.RenderFruitPresser;
import growthcraft.cellar.client.renderer.TileEntityFermentJarRenderer;
import growthcraft.cellar.client.renderer.TileEntityFruitPresserRenderer;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy
{
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderFruitPress());
		RenderingRegistry.registerBlockHandler(new RenderFruitPresser());
		RenderingRegistry.registerBlockHandler(new RenderBrewKettle());
		RenderingRegistry.registerBlockHandler(new RenderFermentBarrel());
		RenderingRegistry.registerBlockHandler(new RenderFermentJar());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFruitPresser.class, new TileEntityFruitPresserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFermentJar.class, new TileEntityFermentJarRenderer());
	}

	public void registerVillagerSkin()
	{
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftCellar.getConfig().villagerBrewerID, new ResourceLocation("grccellar" , "textures/entity/brewer.png"));
	}

	public void init()
	{
		new GrcCellarResources();

		initRenders();
		registerVillagerSkin();
	}
}
