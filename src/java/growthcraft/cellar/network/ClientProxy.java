package growthcraft.cellar.network;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.render.RenderBrewKettle;
import growthcraft.cellar.render.RenderFermentBarrel;
import growthcraft.cellar.render.RenderFruitPress;
import growthcraft.cellar.render.RenderFruitPresser;
import growthcraft.cellar.render.TileEntityFruitPresserRenderer;
import growthcraft.cellar.tileentity.TileEntityFruitPresser;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

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
