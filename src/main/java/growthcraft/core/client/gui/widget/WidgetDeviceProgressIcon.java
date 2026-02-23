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

import growthcraft.core.common.tileentity.feature.ITileProgressiveDevice;

import net.minecraft.tileentity.TileEntity;

public class WidgetDeviceProgressIcon extends WidgetIconBase
{
	public static enum ProgressDirection
	{
		LEFT_TO_RIGHT,
		RIGHT_TO_LEFT,
		TOP_TO_BOTTOM,
		BOTTOM_TO_TOP;
	}

	public ProgressDirection progressDirection = ProgressDirection.LEFT_TO_RIGHT;

	@SuppressWarnings({"rawtypes"})
	public WidgetDeviceProgressIcon(WidgetManager man, int x, int y, int w, int h)
	{
		super(man, x, y, w, h);
	}

	public WidgetDeviceProgressIcon setProgressDirection(ProgressDirection dir)
	{
		this.progressDirection = dir;
		return this;
	}

	@Override
	protected void doDraw(int mx, int my, int x, int y)
	{
		final TileEntity tileEntity = manager.gui.getTileEntity();
		if (tileEntity instanceof ITileProgressiveDevice)
		{
			final ITileProgressiveDevice progDevice = (ITileProgressiveDevice)tileEntity;
			int dx = x;
			int dy = y;
			int tx = textureRect.x;
			int ty = textureRect.y;
			int tw = textureRect.w;
			int th = textureRect.h;

			switch (progressDirection)
			{
				case LEFT_TO_RIGHT:
				{
					tw = progDevice.getDeviceProgressScaled(tw);
				} break;
				case RIGHT_TO_LEFT:
				{
					tw = progDevice.getDeviceProgressScaled(tw);
					final int offX = textureRect.w - tw;
					dx += offX;
					tx += offX;
				} break;
				case TOP_TO_BOTTOM:
				{
					th = progDevice.getDeviceProgressScaled(th);
				} break;
				case BOTTOM_TO_TOP:
				{
					th = progDevice.getDeviceProgressScaled(th);
					final int offY = textureRect.h - th;
					dy += offY;
					ty += offY;
				} break;
				default:
			}

			if (tw > 0 && th > 0)
			{
				manager.gui.drawTexturedModalRect(dx, dy, tx, ty, tw, th);
			}
		}
	}
}
