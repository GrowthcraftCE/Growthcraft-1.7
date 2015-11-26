package growthcraft.core.client.gui;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.core.util.Rectangle;

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

@SideOnly(Side.CLIENT)
public class GrcGuiContainer extends GuiContainer
{
	public static class TooltipIndex
	{
		// use this to identify the tooltip index
		public final String handle;
		// The rectangle where this tooltip will be displayed
		public final Rectangle rect;

		public TooltipIndex(String h, Rectangle r)
		{
			this.handle = h;
			this.rect = r;
		}
	}

	protected List<TooltipIndex> tooltipIndices = new ArrayList<TooltipIndex>();
	protected List<String> tooltip = new ArrayList<String>();
	protected TileEntity tileEntity;

	public GrcGuiContainer(Container container, TileEntity te)
	{
		super(container);
		this.tileEntity = te;
	}

	protected void drawInventoryName(int x, int y)
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

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		super.drawGuiContainerForegroundLayer(x, y);
		drawInventoryName(x, y);
	}

	protected void addTooltipIndex(String handle, Rectangle r)
	{
		tooltipIndices.add(new TooltipIndex(handle, r));
	}

	protected void addTooltipIndex(String handle, int x, int y, int w, int h)
	{
		addTooltipIndex(handle, new Rectangle(x, y, w, h));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {}


	protected void addTooltips(String handle, List<String> tips) {}

	// Overwrite this method to draw tooltips, use the ti to identify which
	// rectangle you're working with.
	protected void drawTooltipIndex(TooltipIndex ti, int x, int y)
	{
		tooltip.clear();
		addTooltips(ti.handle, tooltip);
		if (tooltip.size() > 0)
		{
			drawText(tooltip, x, y, fontRendererObj);
		}
	}

	protected void drawTooltip(int x, int y)
	{
		final int gx = x - ((this.width - this.xSize) / 2);
		final int gy = y - ((this.height - this.ySize) / 2);

		for (TooltipIndex ti : tooltipIndices)
		{
			if (ti.rect.contains(gx, gy))
			{
				drawTooltipIndex(ti, x, y);
				break;
			}
		}
	}

	@Override
	public void drawScreen(int x, int y, float par3)
	{
		super.drawScreen(x, y, par3);
		drawTooltip(x, y);
	}

	protected void bindTexture(ResourceLocation res)
	{
		this.mc.getTextureManager().bindTexture(res);
	}

	protected void drawFluidStack(int w, int h, int wp, int hp, int width, int amount, FluidStack fluidstack)
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
				this.drawTexturedModelRectFromIcon(w + wp, h + hp + 52 - amount, icon, width, amount);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}

	protected void drawText(List<String> list, int par2, int par3, FontRenderer font)
	{
		if (!list.isEmpty())
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			final Iterator<String> iterator = list.iterator();

			while (iterator.hasNext())
			{
				final String s = iterator.next();
				final int l = font.getStringWidth(s);

				if (l > k)
				{
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (list.size() > 1)
			{
				k1 += 2 + (list.size() - 1) * 10;
			}

			if (i1 + k > this.width)
			{
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > this.height)
			{
				j1 = this.height - k1 - 6;
			}

			this.zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			final int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			final int i2 = 1347420415;
			final int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < list.size(); ++k2)
			{
				final String s1 = list.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0)
				{
					j1 += 2;
				}

				j1 += 10;
			}

			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}
