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

public class NumUtils
{
	private NumUtils() {}

	public static boolean isFlagged(int src, int flag)
	{
		return (src & flag) == flag;
	}

	public static int roundedBlocks(int num, int blocksize)
	{
		int n = num / blocksize;
		if (n < num) n += blocksize;
		return n;
	}

	public static int closestPowerOf2(int num)
	{
		int start = 1;
		while (start < num)
		{
			start *= 2;
		}
		return start;
	}

	public static boolean between(int num, int bot, int top)
	{
		return num >= bot && num <= top;
	}

	public static int[] newIntRangeArray(int start, int length)
	{
		final int[] result = new int[length];
		for (int i = 0; i < result.length; ++i)
		{
			result[i] = start + i;
		}
		return result;
	}

	public static int[] newIndexArray(int length)
	{
		return newIntRangeArray(0, length);
	}
}
