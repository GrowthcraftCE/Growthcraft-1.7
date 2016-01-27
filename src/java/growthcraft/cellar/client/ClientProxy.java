package growthcraft.cellar.client;

import growthcraft.cellar.client.render.RenderBrewKettle;
import growthcraft.cellar.client.render.RenderFermentBarrel;
import growthcraft.cellar.client.render.RenderCultureJar;
import growthcraft.cellar.client.render.RenderFruitPress;
import growthcraft.cellar.client.render.RenderFruitPresser;
import growthcraft.cellar.client.render.item.ItemRenderCultureJar;
import growthcraft.cellar.client.renderer.TileEntityCultureJarRenderer;
import growthcraft.cellar.client.renderer.TileEntityFruitPresserRenderer;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
	public void initRenders()
	{
		MinecraftForgeClient.registerItemRenderer(GrowthCraftCellar.fermentJar.getItem(), new ItemRenderCultureJar());
		RenderingRegistry.registerBlockHandler(new RenderFruitPress());
		RenderingRegistry.registerBlockHandler(new RenderFruitPresser());
		RenderingRegistry.registerBlockHandler(new RenderBrewKettle());
		RenderingRegistry.registerBlockHandler(new RenderFermentBarrel());
		RenderingRegistry.registerBlockHandler(new RenderCultureJar());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFruitPresser.class, new TileEntityFruitPresserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCultureJar.class, new TileEntityCultureJarRenderer());
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
