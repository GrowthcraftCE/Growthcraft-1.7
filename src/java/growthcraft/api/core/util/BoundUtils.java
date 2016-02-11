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
 * Utility class for defining block bounds, cut from IceDragon200/YATM
 * This class has 2 styles of bounding boxes:
 *   Point bb (x, x2. etc..)
 *   Cuboid bb (x, y, z, width, depth, height)
 * Minecraft requires the former, while the latter is convient if you know the
 * position and the size.
 */
public class BoundUtils
{
	private BoundUtils() {}

	/**
	 * Creates a new 6 element float array for use as a Bounding Box
	 *
	 * @return bounding box
	 */
	public static float[] newBoundsArray()
	{
		return new float[6];
	}

	/**
	 * Scales the provided bounding box
	 *
	 * @param target - target bounding box
	 * @param x - translation x
	 * @param y - translation y
	 * @param z - translation z
	 * @param src - source bounding box
	 * @return target
	 */
	public static float[] translateBounds(float[] target, float x, float y, float z, float[] src)
	{
		assert target.length == 6;
		target[0] = src[0] + x;
		target[1] = src[1] + y;
		target[2] = src[2] + z;
		target[3] = src[3] + x;
		target[4] = src[4] + y;
		target[5] = src[5] + z;
		return target;
	}

	/**
	 * Scales the provided bounding box
	 *
	 * @param target - target bounding box
	 * @param scale - box scale
	 * @param x - source x
	 * @param y - source y
	 * @param z - source z
	 * @param x2 - source x2
	 * @param y2 - source y2
	 * @param z2 - source z2
	 * @return target
	 */
	public static float[] scaleBounds(float[] target, float scale, float x, float y, float z, float x2, float y2, float z2)
	{
		assert target.length == 6;
		target[0] = x * scale;
		target[1] = y * scale;
		target[2] = z * scale;
		target[3] = x2 * scale;
		target[4] = y2 * scale;
		target[5] = z2 * scale;
		return target;
	}

	/**
	 * Scales the provided bounding box
	 *
	 * @param target - target bounding box
	 * @param scale - box scale
	 * @param src - src bounding box
	 * @return target
	 */
	public static float[] scaleBounds(float[] target, float scale, float[] src)
	{
		assert src.length == 6;
		return scaleBounds(target, scale, src[0], src[1], src[2], src[3], src[4], src[5]);
	}

	/**
	 * Creates a new scaled bounding box
	 *
	 * @param scale - box scale
	 * @param x - source x
	 * @param y - source y
	 * @param z - source z
	 * @param x2 - source x2
	 * @param y2 - source y2
	 * @param z2 - source z2
	 * @return bounding box
	 */
	public static float[] newScaledBounds(float scale, float x, float y, float z, float x2, float y2, float z2)
	{
		final float[] result = newBoundsArray();
		return scaleBounds(result, scale, x, y, z, x2, y2, z2);
	}

	/**
	 * Converts a cuboid like bounding box to an actual bounding box.
	 *
	 * @param target - target bounding box
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @param w - width
	 * @param d - depth
	 * @param h - height
	 * @return target
	 */
	public static float[] cubeToBounds(float[] target, float x, float y, float z, float w, float d, float h)
	{
		assert target.length == 6;
		target[0] = x;
		target[1] = y;
		target[2] = z;
		target[3] = x + w;
		target[4] = y + d;
		target[5] = z + h;
		return target;
	}
	/**
	 * Converts a cuboid like bounding box to an actual bounding box.
	 *
	 * @param target - target bounding box
	 * @param src - source bounding box
	 * @return target
	 */
	public static float[] cubeToBounds(float[] target, float[] src)
	{
		assert src.length == 6;
		return cubeToBounds(target, src[0], src[1], src[2], src[3], src[4], src[5]);
	}

	/**
	 * Creates a new Cuboid bounding box
	 *
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @param w - width
	 * @param d - depth
	 * @param h - height
	 * @return cuboid bounding box
	 */
	public static float[] newCubeToBounds(float x, float y, float z, float w, float d, float h)
	{
		final float[] result = newBoundsArray();
		return cubeToBounds(result, x, y, z, w, d, h);
	}

	/**
	 * Initialize a Cuboid bounding box placing it the center
	 *
	 * @param w - width
	 * @param d - depth
	 * @param h - height
	 * @return target
	 */
	public static float[] centeredCubeBounds(float[] target, float w, float d, float h)
	{
		final float x = (1 - w) / 2;
		final float y = (1 - d) / 2;
		final float z = (1 - h) / 2;
		return cubeToBounds(target, x, y, z, w, d, h);
	}

	/**
	 * Creates a new Cuboid bounding box, centered
	 *
	 * @param w - width
	 * @param d - depth
	 * @param h - height
	 * @return cuboid bounding box
	 */
	public static float[] newCenteredCubeBounds(float w, float d, float h)
	{
		return centeredCubeBounds(newBoundsArray(), w, d, h);
	}
}
