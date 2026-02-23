/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.cellar.client;

import growthcraft.cellar.client.gui.GuiBrewKettle;
import growthcraft.cellar.client.gui.GuiCultureJar;
import growthcraft.cellar.client.gui.GuiFermentBarrel;
import growthcraft.cellar.client.gui.GuiFruitPress;
import growthcraft.cellar.client.render.item.ItemRenderCultureJar;
import growthcraft.cellar.client.render.RenderBrewKettle;
import growthcraft.cellar.client.render.RenderCultureJar;
import growthcraft.cellar.client.render.RenderFermentBarrel;
import growthcraft.cellar.client.render.RenderFruitPress;
import growthcraft.cellar.client.render.RenderFruitPresser;
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
	protected void initRenders()
	{
		MinecraftForgeClient.registerItemRenderer(GrowthCraftCellar.blocks.cultureJar.getItem(), new ItemRenderCultureJar());
		RenderingRegistry.registerBlockHandler(new RenderBrewKettle());
		RenderingRegistry.registerBlockHandler(new RenderCultureJar());
		RenderingRegistry.registerBlockHandler(new RenderFermentBarrel());
		RenderingRegistry.registerBlockHandler(new RenderFruitPress());
		RenderingRegistry.registerBlockHandler(new RenderFruitPresser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFruitPresser.class, new TileEntityFruitPresserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCultureJar.class, new TileEntityCultureJarRenderer());
	}

	protected void registerVillagerSkin()
	{
		final int villagerID = GrowthCraftCellar.getConfig().villagerBrewerID;
		if (villagerID > 0)
		{
			VillagerRegistry.instance().registerVillagerSkin(villagerID, new ResourceLocation("grccellar" , "textures/entity/brewer.png"));
		}
	}

	@Override
	public void init()
	{
		super.init();
		new GrcCellarResources();
		initRenders();
		registerVillagerSkin();
		GrowthCraftCellar.guiProvider.register("grccellar:fruit_press", GuiFruitPress.class);
		GrowthCraftCellar.guiProvider.register("grccellar:ferment_barrel", GuiFermentBarrel.class);
		GrowthCraftCellar.guiProvider.register("grccellar:culture_jar", GuiCultureJar.class);
		GrowthCraftCellar.guiProvider.register("grccellar:brew_kettle", GuiBrewKettle.class);
	}
}
