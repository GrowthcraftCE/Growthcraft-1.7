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
import growthcraft.core.client.util.InterpolatedIcon;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.botania.EnumBotaniaWoodType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

public class GrcBeesHandleTextureStitch
{
	// Fluid Icons
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidHoneyStill;
	@SideOnly(Side.CLIENT)
	public IIcon iconFluidHoneyFlow;

	@SideOnly(Side.CLIENT)
	private void loadShimmerWoodInterpolatedIcons(TextureStitchEvent.Pre event)
	{
		if (GrowthCraftBees.beeBoxBotania != null)
		{
			final int offset = EnumBotaniaWoodType.SHIMMER_WOOD.meta;
			final String[] sides = { "bottom", "top", "side", "side_honey" };
			final IIcon[] icons = GrowthCraftBees.beeBoxBotania.getBlock().getIcons();
			int i = 0;
			for (String side : sides)
			{
				final String name = String.format("grcbees:beebox/botania/shimmer_wood/%s", side);
				final TextureAtlasSprite icon = new InterpolatedIcon(name);
				if (event.map.setTextureEntry(name, icon))
				{
					icons[offset * 4 + i] = icon;
				}
				i++;
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		if (event.map.getTextureType() == 0)
		{
			this.iconFluidHoneyStill = event.map.registerIcon("grcbees:fluids/honey_still");
			this.iconFluidHoneyFlow = event.map.registerIcon("grcbees:fluids/honey_flow");
			loadShimmerWoodInterpolatedIcons(event);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			if (GrowthCraftBees.fluids.honey != null) GrowthCraftBees.fluids.honey.getFluid().setIcons(iconFluidHoneyStill, iconFluidHoneyFlow);
			for (int i = 0; i < GrowthCraftBees.fluids.honeyMeadBooze.length; ++i)
			{
				GrowthCraftBees.fluids.honeyMeadBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}
}
