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

// Same as the minecraft AxisAlignedBB, except it has #scale and works with BoundUtils
public class BBox
{
	public float[] boundsData;

	public BBox()
	{
		this.boundsData = new float[6];
	}

	public BBox(float[] bounds)
	{
		this();
		assert bounds.length == 6;
		this.boundsData[0] = bounds[0];
		this.boundsData[1] = bounds[1];
		this.boundsData[2] = bounds[2];
		this.boundsData[3] = bounds[3];
		this.boundsData[4] = bounds[4];
		this.boundsData[5] = bounds[5];
	}

	public BBox(float px0, float py0, float pz0, float px1, float py1, float pz1)
	{
		this();
		this.boundsData[0] = px0;
		this.boundsData[1] = py0;
		this.boundsData[2] = pz0;
		this.boundsData[3] = px1;
		this.boundsData[4] = py1;
		this.boundsData[5] = pz1;
	}

	/**
	 * @param x - translate the `x` coord of the bounding box
	 * @param y - translate the `y` coord of the bounding box
	 * @param z - translate the `z` coord of the bounding box
	 * @return this
	 */
	public BBox translate(float x, float y, float z)
	{
		BoundUtils.translateBounds(boundsData, x, y, z, boundsData);
		return this;
	}

	public BBox scale(float px0, float py0, float pz0, float px1, float py1, float pz1)
	{
		boundsData[0] *= px0;
		boundsData[1] *= py0;
		boundsData[2] *= pz0;
		boundsData[3] *= px1;
		boundsData[4] *= py1;
		boundsData[5] *= pz1;
		return this;
	}

	public BBox scale(float x, float y, float z)
	{
		return scale(x, y, z, x, y, z);
	}

	public BBox scale(float ps)
	{
		return scale(ps, ps, ps);
	}

	public float x0() { return boundsData[0]; }
	public float y0() { return boundsData[1]; }
	public float z0() { return boundsData[2]; }
	public float x1() { return boundsData[3]; }
	public float y1() { return boundsData[4]; }
	public float z1() { return boundsData[5]; }

	public float w() { return x1() - x0(); }
	public float h() { return y1() - y0(); }
	public float l() { return z1() - z0(); }

	public boolean contains(float px, float py, float pz)
	{
		return x0() >= px && x1() <= px &&
			y0() >= py && y1() <= py &&
			z0() >= pz && z1() <= pz;
	}

	public static BBox newCube(float x, float y, float z, float w, float h, float l)
	{
		return new BBox(x, y, z, x + w, y + h, z + l);
	}

	public static BBox newCentered(float pw, float ph, float pl)
	{
		final BBox result = new BBox();
		BoundUtils.centeredCubeBounds(result.boundsData, pw, ph, pl);
		return result;
	}
}
