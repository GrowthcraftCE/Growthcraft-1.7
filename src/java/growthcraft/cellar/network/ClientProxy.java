package growthcraft.cellar.network;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.renderer.RenderBrewKettle;
import growthcraft.cellar.renderer.RenderFermentBarrel;
import growthcraft.cellar.renderer.RenderFruitPress;
import growthcraft.cellar.renderer.RenderFruitPresser;
import growthcraft.cellar.renderer.TileEntityFruitPresserRenderer;
import growthcraft.cellar.tileentity.TileEntityFruitPresser;

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
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftCellar.villagerBrewer_id, new ResourceLocation("grccellar" , "textures/entity/brewer.png"));
	}
}
