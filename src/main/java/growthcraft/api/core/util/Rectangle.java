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
package growthcraft.api.core.util;

// I'm surprised minecraft doesn't have a Rectangle class, these things are
// darn handy!
public class Rectangle
{
	public int x;
	public int y;
	public int w;
	public int h;

	public Rectangle(int ix, int iy, int iw, int ih)
	{
		this.x = ix;
		this.y = iy;
		this.w = iw;
		this.h = ih;
	}

	public Rectangle(Rectangle rect)
	{
		this(rect.x, rect.y, rect.w, rect.h);
	}

	public Rectangle()
	{
		this(0, 0, 0, 0);
	}

	public Rectangle copy()
	{
		return new Rectangle(this);
	}

	public Rectangle translate(int tx, int ty)
	{
		this.x += tx;
		this.y += ty;
		return this;
	}

	public Rectangle scale(float tx, float ty)
	{
		this.w *= tx;
		this.h *= ty;
		return this;
	}

	public Rectangle moveto(int tx, int ty)
	{
		this.x = tx;
		this.y = ty;
		return this;
	}

	public Rectangle resize(int tw, int th)
	{
		this.w = tw;
		this.h = th;
		return this;
	}

	public Rectangle set(int px, int py, int pw, int ph)
	{
		return moveto(px, py).resize(pw, ph);
	}

	public Rectangle set(Rectangle rect)
	{
		return set(rect.x, rect.y, rect.w, rect.h);
	}

	public boolean contains(int ix, int iy)
	{
		return ix >= x && iy >= y && ix < (x + w) && iy < (y + h);
	}

	public int x1()
	{
		return x;
	}

	public int x2()
	{
		return x + w;
	}

	public int y1()
	{
		return y;
	}

	public int y2()
	{
		return y + h;
	}

	public boolean isEmpty()
	{
		return w == 0 || h == 0;
	}
}
