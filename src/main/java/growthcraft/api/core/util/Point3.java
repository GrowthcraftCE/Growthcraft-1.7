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
package growthcraft.api.core.util;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * To mimic Forestry's internal Vect class
 */
public class Point3
{
	public final int x;
	public final int y;
	public final int z;

	public Point3(int px, int py, int pz)
	{
		this.x = px;
		this.y = py;
		this.z = pz;
	}

	public Point3(Point3 point)
	{
		this(point.x, point.y, point.z);
	}

	public Point3(ForgeDirection dir)
	{
		this(dir.offsetX, dir.offsetY, dir.offsetZ);
	}

	public Point3()
	{
		this(0, 0, 0);
	}

	public Point3 dup()
	{
		return new Point3(x, y, z);
	}

	public Point3 identity()
	{
		return dup();
	}

	public Point3 invert()
	{
		return new Point3(-x, -y, -z);
	}

	public Point3 add(int px, int py, int pz)
	{
		return new Point3(x + px, y + py, z + pz);
	}

	public Point3 add(Point3 point)
	{
		return add(point.x, point.y, point.z);
	}

	public Point3 add(ForgeDirection dir)
	{
		return add(dir.offsetX, dir.offsetY, dir.offsetZ);
	}

	public Point3 sub(int px, int py, int pz)
	{
		return new Point3(x - px, y - py, z - pz);
	}

	public Point3 sub(Point3 point)
	{
		return sub(point.x, point.y, point.z);
	}

	public Point3 sub(ForgeDirection dir)
	{
		return sub(dir.offsetX, dir.offsetY, dir.offsetZ);
	}

	public Point3 mul(int px, int py, int pz)
	{
		return new Point3(x * px, y * py, z * pz);
	}

	public Point3 mul(float px, float py, float pz)
	{
		return new Point3(Math.round(x * px), Math.round(y * py), Math.round(z * pz));
	}

	public Point3 mul(Point3 point)
	{
		return mul(point.x, point.y, point.z);
	}

	public Point3 mul(ForgeDirection dir)
	{
		return mul(dir.offsetX, dir.offsetY, dir.offsetZ);
	}

	@Override
	public String toString()
	{
		return String.format("(%d, %d, %d)", x, y, z);
	}

	public int[] toArray()
	{
		return new int[]{x, y, z};
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj instanceof Point3)
		{
			final Point3 other = (Point3)obj;
			return (x == other.x) && (y == other.y) && (z == other.z);
		}
		return false;
	}
}
