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

public class ModelCuboid
{
	public float x;
	public float y;
	public float z;
	public float w;
	public float h;
	public float l;

	public ModelCuboid(float ix, float iy, float iz, float iw, float ih, float il)
	{
		this.x = ix;
		this.y = iy;
		this.z = iz;
		this.w = iw;
		this.h = ih;
		this.l = il;
	}

	public ModelCuboid(ModelCuboid other)
	{
		this(other.x, other.y, other.z, other.w, other.h, other.l);
	}

	public ModelCuboid() {}

	public ModelCuboid copy()
	{
		return new ModelCuboid(this);
	}

	public ModelCuboid translate(float tx, float ty, float tz)
	{
		this.x += tx;
		this.y += ty;
		this.z += tz;
		return this;
	}

	public ModelCuboid moveto(float tx, float ty, float tz)
	{
		this.x = tx;
		this.y = ty;
		this.z = tz;
		return this;
	}

	public ModelCuboid scale(float tx, float ty, float tz)
	{
		this.w *= tx;
		this.h *= ty;
		this.l *= tz;
		return this;
	}

	public ModelCuboid scaleAll(float s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		this.w *= s;
		this.h *= s;
		this.l *= s;
		return this;
	}

	public ModelCuboid resize(float tx, float ty, float tz)
	{
		this.w = tx;
		this.h = ty;
		this.l = tz;
		return this;
	}

	public void writeBounds(float[] bounds)
	{
		assert bounds.length == 6;
		bounds[0] = x;
		bounds[1] = y;
		bounds[2] = z;
		bounds[3] = x + w;
		bounds[4] = y + h;
		bounds[5] = z + l;
	}
}
