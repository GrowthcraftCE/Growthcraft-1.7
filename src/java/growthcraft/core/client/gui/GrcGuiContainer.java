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
package growthcraft.core.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.Rectangle;
import growthcraft.core.client.gui.widget.WidgetManager;
import growthcraft.core.util.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

@SideOnly(Side.CLIENT)
public abstract class GrcGuiContainer<C extends Container, T extends TileEntity> extends GuiContainer
{
	protected T tileEntity;
	protected ResourceLocation guiResource;
	protected TooltipManager tooltipManager;
	protected WidgetManager widgets;

	public GrcGuiContainer(ResourceLocation res, C container, T te)
	{
		super(container);
		this.guiResource = res;
		this.tileEntity = te;
	}

	public T getTileEntity()
	{
		return tileEntity;
	}

	public FontRenderer getFontRenderer()
	{
		return fontRendererObj;
	}

	public int getGuiX()
	{
		return (width - xSize) / 2;
	}

	public int getGuiY()
	{
		return (height - ySize) / 2;
	}

	public int getXSize()
	{
		return xSize;
	}

	public int getYSize()
	{
		return ySize;
	}

	public void setZLevel(float z)
	{
		this.zLevel = z;
	}

	public void getItemRendererZLevel(float z)
	{
		itemRender.zLevel = z;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.tooltipManager = new TooltipManager<C, T>(this);
		this.widgets = new WidgetManager<C, T>(this);
	}

	@Override
	public void drawHoveringText(List l, int x, int y, FontRenderer renderer)
	{
		super.drawHoveringText(l, x, y, renderer);
	}

	public void drawHoveringText(List<String> l, int x, int y)
	{
		drawHoveringText(l, x, y, fontRendererObj);
	}

	@Override
	public void drawGradientRect(int x, int y, int w, int h, int color1, int color2)
	{
		super.drawGradientRect(x, y, w, h, color1, color2);
	}

	public void bindTexture(ResourceLocation res)
	{
		mc.getTextureManager().bindTexture(res);
	}

	public void bindGuiTexture()
	{
		bindTexture(guiResource);
	}

	public void drawFluidStack(int x, int y, int wp, int hp, int width, int height, int amount, FluidStack fluidstack)
	{
		if (fluidstack == null) return;

		final Fluid fluid = fluidstack.getFluid();
		final int color = fluid.getColor();

		if (fluid != null)
		{
			final IIcon icon = fluid.getStillIcon();

			if (icon != null)
			{
				bindTexture(TextureMap.locationBlocksTexture);

				final float r = (float)(color >> 16 & 255) / 255.0F;
				final float g = (float)(color >> 8 & 255) / 255.0F;
				final float b = (float)(color & 255) / 255.0F;
				GL11.glColor4f(r, g, b, 1.0f);
				drawTexturedModelRectFromIcon(x + wp, y + hp + height - amount, icon, width, amount);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}

	public void drawTank(int w, int h, int wp, int hp, int width, int height, int amount, FluidStack fluidstack, FluidTank _tank)
	{
		drawFluidStack(w, h, wp, hp, width, height, amount, fluidstack);
	}

	protected void addFluidTooltips(FluidStack fluid, List<String> tooltip)
	{
		if (fluid == null) return;
		if (fluid.amount <= 0) return;

		tooltip.add(fluid.getLocalizedName());

		final String s = UnitFormatter.fluidModifier(fluid.getFluid());
		if (s != null) tooltip.add(s);
	}

	public void addTooltipIndex(String handle, Rectangle r)
	{
		tooltipManager.addTooltipIndex(handle, r);
	}

	public void addTooltipIndex(String handle, int x, int y, int w, int h)
	{
		tooltipManager.addTooltipIndex(handle, x, y, w, h);
	}

	protected void drawInventoryName(int mx, int my)
	{
		if (tileEntity instanceof IInventory)
		{
			final IInventory inv = (IInventory)tileEntity;
			final String invName = inv.getInventoryName();
			final String s = inv.hasCustomInventoryName() ? invName : GrcI18n.translate(invName);
			fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
			fontRendererObj.drawString(GrcI18n.translate("container.inventory"), 8, ySize - 96 + 2, 4210752);
		}
	}

	protected void drawWidgets(int mx, int my)
	{
		widgets.draw(mx, my);
	}

	protected void drawWidgetsForeground(int mx, int my)
	{
		widgets.drawForeground(mx, my);
	}

	protected void drawGuiPanel(int mx, int my)
	{
		drawTexturedModalRect(getGuiX(), getGuiY(), 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		bindGuiTexture();
		drawGuiPanel(mx, my);
		drawWidgets(mx, my);
		bindGuiTexture();
	}

	protected void drawTooltips(int mx, int my)
	{
		tooltipManager.draw(mx, my);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mx, int my)
	{
		super.drawGuiContainerForegroundLayer(mx, my);
		drawInventoryName(mx, my);
		drawWidgetsForeground(mx, my);
	}

	@Override
	public void drawScreen(int mx, int my, float par)
	{
		super.drawScreen(mx, my, par);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableGUIStandardItemLighting();

		GL11.glPushMatrix();
		{
			drawTooltips(mx, my);
		}
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	// Overwrite this method to add tooltips based on the handle
	public void addTooltips(String handle, List<String> tips) {}
}
