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
import java.util.ArrayList;

import growthcraft.api.core.util.Rectangle;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class TooltipManager<C extends Container, T extends TileEntity>
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

	public final GrcGuiContainer<C, T> gui;
	protected final List<TooltipIndex> tooltipIndices = new ArrayList<TooltipIndex>();
	protected final List<String> tooltipCache = new ArrayList<String>();

	public TooltipManager(GrcGuiContainer<C, T> g)
	{
		this.gui = g;
	}

	public void addTooltipIndex(String handle, Rectangle r)
	{
		tooltipIndices.add(new TooltipIndex(handle, r));
	}

	public void addTooltipIndex(String handle, int x, int y, int w, int h)
	{
		addTooltipIndex(handle, new Rectangle(x, y, w, h));
	}

	// Overwrite this method to draw tooltips, use the ti to identify which
	// rectangle you're working with.
	protected void drawTooltipIndex(TooltipIndex ti, int x, int y)
	{
		tooltipCache.clear();
		gui.addTooltips(ti.handle, tooltipCache);
		if (tooltipCache.size() > 0)
		{
			gui.drawHoveringText(tooltipCache, x, y);
		}
	}

	protected void drawTooltip(int mx, int my)
	{
		final int gx = mx - ((gui.width - gui.getXSize()) / 2);
		final int gy = my - ((gui.height - gui.getYSize()) / 2);

		for (TooltipIndex ti : tooltipIndices)
		{
			if (ti.rect.contains(gx, gy))
			{
				drawTooltipIndex(ti, mx, my);
				break;
			}
		}
	}

	public void draw(int mx, int my)
	{
		drawTooltip(mx, my);
	}
}
