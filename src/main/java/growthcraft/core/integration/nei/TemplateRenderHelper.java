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
package growthcraft.core.integration.nei;

import growthcraft.core.client.gui.GrcGuiRenderHelper;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class TemplateRenderHelper
{
	public static final int PROGRESS_RIGHT = 0;
	public static final int PROGRESS_DOWN = 1;
	public static final int PROGRESS_LEFT = 2;
	public static final int PROGRESS_UP = 3;

	private TemplateRenderHelper() {}

	public static void drawFluid(int x, int y, int w, int h, Fluid fluid, int amount)
	{
		if (fluid != null)
		{
			final IIcon icon = fluid.getStillIcon();

			if (icon != null)
			{
				GuiDraw.changeTexture(TextureMap.locationBlocksTexture);
				final int color = fluid.getColor();
				final float r = (float)(color >> 16 & 255) / 255.0F;
				final float g = (float)(color >> 8 & 255) / 255.0F;
				final float b = (float)(color & 255) / 255.0F;
				GL11.glColor4f(r, g, b, 1.0f);
				GrcGuiRenderHelper.drawTexturedModelRectFromIcon(x, y + h - amount, GuiDraw.gui.getZLevel(), icon, w, amount);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}

	public static void drawFluidStack(int x, int y, int w, int h, FluidStack fluidstack, int amountMax)
	{
		if (fluidstack == null) return;
		drawFluid(x, y, w, h, fluidstack.getFluid(), h * fluidstack.amount / amountMax);
	}
}
