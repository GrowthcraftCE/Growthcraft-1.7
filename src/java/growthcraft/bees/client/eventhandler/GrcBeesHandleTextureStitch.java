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
package growthcraft.bees.client.eventhandler;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraft.util.IIcon;

public class GrcBeesHandleTextureStitch
{
	// Fluid Icons
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidHoneyStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidHoneyFlow;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		if (event.map.getTextureType() == 0)
		{
			this.iconFluidHoneyStill = event.map.registerIcon("grcbees:fluids/honey_still");
			this.iconFluidHoneyFlow = event.map.registerIcon("grcbees:fluids/honey_flow");
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			GrowthCraftBees.fluids.honey.getFluid().setIcons(iconFluidHoneyStill, iconFluidHoneyFlow);
			for (int i = 0; i < GrowthCraftBees.fluids.honeyMeadBooze.length; ++i)
			{
				GrowthCraftBees.fluids.honeyMeadBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}
}
