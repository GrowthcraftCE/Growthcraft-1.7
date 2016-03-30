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
package growthcraft.core.client.gui.widget;

import growthcraft.core.common.tileentity.ITileHeatedDevice;

import net.minecraft.tileentity.TileEntity;

public class WidgetHeatIcon extends WidgetIconBase
{
	public WidgetHeatIcon(WidgetManager man, int x, int y, int w, int h)
	{
		super(man, x, y, w, h);
	}

	@Override
	protected void doDraw(int mx, int my, int x, int y)
	{
		final TileEntity tileEntity = manager.gui.getTileEntity();
		if (tileEntity instanceof ITileHeatedDevice)
		{
			final ITileHeatedDevice heatedDevice = (ITileHeatedDevice)tileEntity;
			if (heatedDevice.isHeated())
			{
				final int iconHeight = heatedDevice.getHeatScaled(textureRect.h);
				final int offY = textureRect.h - iconHeight;
				manager.gui.drawTexturedModalRect(x, y + offY, textureRect.x, textureRect.y + offY, textureRect.w, iconHeight);
			}
		}
	}
}
