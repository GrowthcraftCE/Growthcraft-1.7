/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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

/**
 * The 3d counterpart to Rectangle
 */
public class CuboidI
{
	public int x;
	public int y;
	public int z;
	public int w;
	public int h;
	public int l;

	public CuboidI(int ix, int iy, int iz, int iw, int ih, int il)
	{
		set(ix, iy, iz, iw, ih, il);
	}

	public CuboidI(CuboidI other)
	{
		this(other.x, other.y, other.z, other.w, other.h, other.l);
	}

	public CuboidI() {}

	public CuboidI copy()
	{
		return new CuboidI(this);
	}

	public CuboidI set(int ix, int iy, int iz, int iw, int ih, int il)
	{
		this.x = ix;
		this.y = iy;
		this.z = iz;
		this.w = iw;
		this.h = ih;
		this.l = il;
		return this;
	}

	public CuboidI set(CuboidI cuboid)
	{
		set(cuboid.x, cuboid.y, cuboid.z, cuboid.w, cuboid.h, cuboid.l);
		return this;
	}

	public boolean contains(int ix, int iy, int iz)
	{
		return ix >= x && iy >= y && iz >= z &&
			ix < (x + w) && iy < (y + h) && iz < (z + l);
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

	public int z1()
	{
		return z;
	}

	public int z2()
	{
		return z + l;
	}

	public boolean isEmpty()
	{
		return w == 0 || h == 0 || l == 0;
	}

	public CuboidI translate(int tx, int ty, int tz)
	{
		this.x += tx;
		this.y += ty;
		this.z += tz;
		return this;
	}

	public CuboidI moveto(int tx, int ty, int tz)
	{
		this.x = tx;
		this.y = ty;
		this.z = tz;
		return this;
	}

	public CuboidI scale(int tx, int ty, int tz)
	{
		this.w *= tx;
		this.h *= ty;
		this.l *= tz;
		return this;
	}

	public CuboidI scaleAll(int s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		this.w *= s;
		this.h *= s;
		this.l *= s;
		return this;
	}

	public CuboidI resize(int tx, int ty, int tz)
	{
		this.w = tx;
		this.h = ty;
		this.l = tz;
		return this;
	}

	public int[] toBoundsArray(int[] bounds)
	{
		assert bounds.length == 6;
		bounds[0] = x;
		bounds[1] = y;
		bounds[2] = z;
		bounds[3] = x + w;
		bounds[4] = y + h;
		bounds[5] = z + l;
		return bounds;
	}

	public int[] toBoundsArray()
	{
		return toBoundsArray(new int[6]);
	}

	public int[] toIntArray(int[] target)
	{
		assert target.length == 6;
		target[0] = x;
		target[1] = y;
		target[2] = z;
		target[3] = w;
		target[4] = h;
		target[5] = l;
		return target;
	}

	public int[] toIntArray()
	{
		return toIntArray(new int[6]);
	}

	@Override
	public String toString()
	{
		return String.format("CuboidI(x: %d, y: %d, z: %d, w: %d, h: %d, l: %d)", x, y, z, w, h, l);
	}
}
