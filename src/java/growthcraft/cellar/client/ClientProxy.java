package growthcraft.cellar.client;

import growthcraft.cellar.client.renderer.RenderBrewKettle;
import growthcraft.cellar.client.renderer.RenderFermentBarrel;
import growthcraft.cellar.client.renderer.RenderFruitPress;
import growthcraft.cellar.client.renderer.RenderFruitPresser;
import growthcraft.cellar.client.renderer.TileEntityFruitPresserRenderer;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderFruitPress());
		RenderingRegistry.registerBlockHandler(new RenderFruitPresser());
		RenderingRegistry.registerBlockHandler(new RenderBrewKettle());
		RenderingRegistry.registerBlockHandler(new RenderFermentBarrel());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFruitPresser.class, new TileEntityFruitPresserRenderer());
	}

	@Override
	public void registerVillagerSkin()
	{
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftCellar.getConfig().villagerBrewerID, new ResourceLocation("grccellar" , "textures/entity/brewer.png"));
	}
}
