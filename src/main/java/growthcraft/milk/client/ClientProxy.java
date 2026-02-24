/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.milk.client;

import growthcraft.milk.client.render.item.ItemRendererCheeseBlock;
import growthcraft.milk.client.render.item.ItemRendererHangingCurds;
import growthcraft.milk.client.render.RenderButterChurn;
import growthcraft.milk.client.render.RenderCheeseBlock;
import growthcraft.milk.client.render.RenderCheesePress;
import growthcraft.milk.client.render.RenderCheeseVat;
import growthcraft.milk.client.render.RenderPancheon;
import growthcraft.milk.client.renderer.TileEntityButterChurnRenderer;
import growthcraft.milk.client.renderer.TileEntityCheesePressRenderer;
import growthcraft.milk.client.renderer.TileEntityCheeseVatRenderer;
import growthcraft.milk.client.renderer.TileEntityHangingCurdsRenderer;
import growthcraft.milk.client.renderer.TileEntityPancheonRenderer;
import growthcraft.milk.client.resource.GrcMilkResources;
import growthcraft.milk.common.CommonProxy;
import growthcraft.milk.common.tileentity.TileEntityButterChurn;
import growthcraft.milk.common.tileentity.TileEntityCheesePress;
import growthcraft.milk.common.tileentity.TileEntityCheeseVat;
import growthcraft.milk.common.tileentity.TileEntityHangingCurds;
import growthcraft.milk.common.tileentity.TileEntityPancheon;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		super.init();
		new GrcMilkResources();

		MinecraftForgeClient.registerItemRenderer(GrowthCraftMilk.blocks.cheeseBlock.getItem(), new ItemRendererCheeseBlock());
		MinecraftForgeClient.registerItemRenderer(GrowthCraftMilk.blocks.hangingCurds.getItem(), new ItemRendererHangingCurds());

		RenderingRegistry.registerBlockHandler(new RenderButterChurn());
		RenderingRegistry.registerBlockHandler(new RenderCheeseBlock());
		RenderingRegistry.registerBlockHandler(new RenderCheesePress());
		RenderingRegistry.registerBlockHandler(new RenderCheeseVat());
		RenderingRegistry.registerBlockHandler(new RenderPancheon());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityButterChurn.class, new TileEntityButterChurnRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCheesePress.class, new TileEntityCheesePressRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCheeseVat.class, new TileEntityCheeseVatRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHangingCurds.class, new TileEntityHangingCurdsRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPancheon.class, new TileEntityPancheonRenderer());
	}
}
