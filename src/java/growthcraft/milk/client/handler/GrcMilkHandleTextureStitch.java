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
package growthcraft.milk.client.handler;

import growthcraft.core.GrowthCraftCore;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraft.util.IIcon;

public class GrcMilkHandleTextureStitch
{
	// Fluid Icons
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidButterMilkStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidButterMilkFlow;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidCreamStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidCreamFlow;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidMilkStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidMilkFlow;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidRennetStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidRennetFlow;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidSkimMilkStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidSkimMilkFlow;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidWheyStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidWheyFlow;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		if (event.map.getTextureType() == 0)
		{
			iconFluidButterMilkStill = event.map.registerIcon("grcmilk:fluids/buttermilk_still");
			iconFluidButterMilkFlow = event.map.registerIcon("grcmilk:fluids/buttermilk_flow");
			iconFluidCreamStill = event.map.registerIcon("grcmilk:fluids/cream_still");
			iconFluidCreamFlow = event.map.registerIcon("grcmilk:fluids/cream_flow");
			iconFluidMilkStill = event.map.registerIcon("grcmilk:fluids/milk_still");
			iconFluidMilkFlow = event.map.registerIcon("grcmilk:fluids/milk_flow");
			iconFluidRennetStill = event.map.registerIcon("grcmilk:fluids/rennet_still");
			iconFluidRennetFlow = event.map.registerIcon("grcmilk:fluids/rennet_flow");
			iconFluidSkimMilkStill = event.map.registerIcon("grcmilk:fluids/skimmilk_still");
			iconFluidSkimMilkFlow = event.map.registerIcon("grcmilk:fluids/skimmilk_flow");
			iconFluidWheyStill = event.map.registerIcon("grcmilk:fluids/whey_still");
			iconFluidWheyFlow = event.map.registerIcon("grcmilk:fluids/whey_flow");
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < GrowthCraftMilk.fluids.boozeMilk.length; ++i)
			{
				GrowthCraftMilk.fluids.boozeMilk[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
			GrowthCraftMilk.fluids.boozeMilk[5].setIcons(GrowthCraftCore.liquidBlobsTexture);

			GrowthCraftMilk.fluids.butterMilk.getFluid().setIcons(iconFluidButterMilkStill, iconFluidButterMilkFlow);
			GrowthCraftMilk.fluids.cream.getFluid().setIcons(iconFluidCreamStill, iconFluidCreamFlow);
			GrowthCraftMilk.fluids.milk.getFluid().setIcons(iconFluidMilkStill, iconFluidMilkFlow);
			GrowthCraftMilk.fluids.rennet.getFluid().setIcons(iconFluidRennetStill, iconFluidRennetFlow);
			GrowthCraftMilk.fluids.skimMilk.getFluid().setIcons(iconFluidSkimMilkStill, iconFluidSkimMilkFlow);
			GrowthCraftMilk.fluids.whey.getFluid().setIcons(iconFluidWheyStill, iconFluidWheyFlow);
		}
	}
}
