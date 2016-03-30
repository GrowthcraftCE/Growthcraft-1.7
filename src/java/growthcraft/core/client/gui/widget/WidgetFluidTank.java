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

import growthcraft.api.core.util.Rectangle;
import growthcraft.core.common.tileentity.device.IFluidTanks;

import net.minecraft.tileentity.TileEntity;

public class WidgetFluidTank extends Widget
{
	public final int tankIndex;
	public int numberColor = 0xFFFFFF;
	public Rectangle ruleRect = new Rectangle();

	public WidgetFluidTank(WidgetManager man, int index, int x, int y, int w, int h)
	{
		super(man, x, y, w, h);
		this.tankIndex = index;
	}

	public WidgetFluidTank setRuleOverlay(int x, int y, int w, int h)
	{
		ruleRect.set(x, y, w, h);
		return this;
	}

	@Override
	protected void doDraw(int mx, int my, int x, int y)
	{
		final TileEntity tileEntity = manager.gui.getTileEntity();

		if (tileEntity instanceof IFluidTanks)
		{
			final IFluidTanks tanks = (IFluidTanks)tileEntity;
			final int w = getWidth();
			final int h = getHeight();
			if (tanks.getFluidAmountScaled(h, tankIndex) > 0)
			{
				manager.gui.drawTank(manager.gui.getGuiX(), manager.gui.getGuiY(), getX(), getY(), w, h,
					tanks.getFluidAmountScaled(h, tankIndex),
					tanks.getFluidStack(tankIndex),
					tanks.getFluidTank(tankIndex));
			}
			if (!ruleRect.isEmpty())
			{
				manager.gui.bindGuiTexture();
				manager.gui.drawTexturedModalRect(x, y, ruleRect.x, ruleRect.y, ruleRect.w, ruleRect.h);
			}
		}
	}

	@Override
	protected void doDrawForeground(int mx, int my, int x, int y)
	{
		final TileEntity tileEntity = manager.gui.getTileEntity();

		if (tileEntity instanceof IFluidTanks)
		{
			final IFluidTanks tanks = (IFluidTanks)tileEntity;
			if (!tanks.isFluidTankEmpty(tankIndex))
			{
				final String s = String.valueOf(tanks.getFluidAmount(tankIndex));
				manager.gui.getFontRenderer().drawStringWithShadow(s,
					rect.x2() - manager.gui.getFontRenderer().getStringWidth(s),
					rect.y2() - 8,
					numberColor);
			}
		}
	}
}
