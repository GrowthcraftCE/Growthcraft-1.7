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

public class Widget
{
	public boolean visible = true;
	public Rectangle rect;
	protected WidgetManager manager;

	public Widget(WidgetManager man, Rectangle r)
	{
		this.manager = man;
		this.rect = r;
	}

	public Widget(WidgetManager man, int x, int y, int w, int h)
	{
		this(man, new Rectangle(x, y, w, h));
	}

	public int getX()
	{
		return rect.x;
	}

	public int getY()
	{
		return rect.y;
	}

	public int getWidth()
	{
		return rect.w;
	}

	public int getHeight()
	{
		return rect.h;
	}

	public int getGuiX()
	{
		return manager.gui.getGuiX();
	}

	public int getGuiY()
	{
		return manager.gui.getGuiY();
	}

	public int getGuiXSize()
	{
		return manager.gui.getXSize();
	}

	public int getGuiYSize()
	{
		return manager.gui.getYSize();
	}

	// Get Screen X
	public int getSX()
	{
		return getGuiX() + getX();
	}

	// Get Screen X
	public int getSY()
	{
		return getGuiY() + getY();
	}

	public boolean isVisible()
	{
		return visible;
	}

	public Widget show()
	{
		this.visible = true;
		return this;
	}

	public Widget hide()
	{
		this.visible = false;
		return this;
	}

	/**
	 * @param mx - mouse x coordinate
	 * @param my - mouse y coordinate
	 * @param x - screen x coordinate to draw to
	 * @param y - screen y coordinate to draw to
	 */
	protected void doDraw(int mx, int my, int x, int y)
	{
	}

	/**
	 * @param mx - mouse x coordinate
	 * @param my - mouse y coordinate
	 * @param x - screen x coordinate to draw to
	 * @param y - screen y coordinate to draw to
	 */
	protected void doDrawForeground(int mx, int my, int x, int y)
	{
	}

	/**
	 * @param mx - mouse x coordinate
	 * @param my - mouse y coordinate
	 */
	public final void draw(int mx, int my)
	{
		if (isVisible())
		{
			manager.gui.bindGuiTexture();
			doDraw(mx, my, getSX(), getSY());
			manager.gui.bindGuiTexture();
		}
	}

	/**
	 * @param mx - mouse x coordinate
	 * @param my - mouse y coordinate
	 */
	public final void drawForeground(int mx, int my)
	{
		if (isVisible())
		{
			manager.gui.bindGuiTexture();
			doDrawForeground(mx, my, getSX(), getSY());
			manager.gui.bindGuiTexture();
		}
	}
}
