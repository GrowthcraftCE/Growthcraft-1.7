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

import javax.annotation.Nonnull;

import net.minecraft.util.MathHelper;

public class GrcColor
{
	public float r;
	public float g;
	public float b;
	public float a;

	public GrcColor(float pr, float pg, float pb, float pa)
	{
		set(pr, pg, pb, pa);
	}

	public int hexRGB()
	{
		return (((int)(r * 255) << 16) & 0xFF) |
			(((int)(g * 255) << 8) & 0xFF) |
			(((int)(b * 255)) & 0xFF);
	}

	public int hexRGBA()
	{
		return (((int)(a * 255) << 24) & 0xFF) |
			(((int)(r * 255) << 16) & 0xFF) |
			(((int)(g * 255) << 8) & 0xFF) |
			(((int)(b * 255)) & 0xFF);
	}

	public GrcColor set(float pr, float pg, float pb, float pa)
	{
		this.r = MathHelper.clamp_float(pr, 0f, 1f);
		this.g = MathHelper.clamp_float(pg, 0f, 1f);
		this.b = MathHelper.clamp_float(pb, 0f, 1f);
		this.a = MathHelper.clamp_float(pa, 0f, 1f);
		return this;
	}

	public GrcColor copy()
	{
		return new GrcColor(r, g, b, a);
	}

	public GrcColor add(@Nonnull GrcColor other)
	{
		return new GrcColor(r + other.r * other.a, g + other.g * other.a, b + other.b * other.a, a);
	}

	public GrcColor sub(@Nonnull GrcColor other)
	{
		return new GrcColor(r - other.r * other.a, g - other.g * other.a, b - other.b * other.a, a);
	}

	public GrcColor mul(@Nonnull GrcColor other)
	{
		return new GrcColor(r * other.r * other.a, g * other.g * other.a, b * other.b * other.a, a);
	}

	public GrcColor mul(@Nonnull float other)
	{
		return new GrcColor(r * other, g * other, b * other, a);
	}

	public GrcColor div(@Nonnull GrcColor other)
	{
		return new GrcColor(r / other.r * other.a, g / other.g * other.a, b / other.b * other.a, a);
	}

	public GrcColor alphaBlend(@Nonnull GrcColor other)
	{
		final float alpha = other.a;
		return new GrcColor(
			(alpha >= 1.0f || a <= 0.0f) ? other.r : (alpha > 0 ? ((other.r * alpha) + (r * (1 - alpha))) : r),
			(alpha >= 1.0f || a <= 0.0f) ? other.g : (alpha > 0 ? ((other.g * alpha) + (g * (1 - alpha))) : g),
			(alpha >= 1.0f || a <= 0.0f) ? other.b : (alpha > 0 ? ((other.b * alpha) + (b * (1 - alpha))) : b),
			a < alpha ? alpha : a
		);
	}

	public GrcColor setRGBA(int pr, int pg, int pb, int pa)
	{
		return set(pr / 255.0f, pg / 255.0f, pb / 255.0f, pa / 255.0f);
	}

	public GrcColor setRGB(int pr, int pg, int pb)
	{
		return setRGBA(pr, pg, pb, (int)(a * 255));
	}

	public GrcColor setHexRGBA(int hex)
	{
		return setRGBA((hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF, (hex >> 24) & 0xFF);
	}

	public GrcColor setHexRGB(int hex)
	{
		return setRGB((hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF);
	}

	public static GrcColor newWhite()
	{
		return new GrcColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static GrcColor newBlack()
	{
		return new GrcColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public static GrcColor newTransparent()
	{
		return new GrcColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public static GrcColor newRGBA(int pr, int pg, int pb, int pa)
	{
		return newWhite().setRGBA(pr, pg, pb, pa);
	}

	public static GrcColor newRGB(int pr, int pg, int pb)
	{
		return newWhite().setRGB(pr, pg, pb);
	}

	public static GrcColor newHexRGBA(int hex)
	{
		return newWhite().setHexRGBA(hex);
	}

	public static GrcColor newHexRGB(int hex)
	{
		return newWhite().setHexRGB(hex);
	}

	public static GrcColor lerp(GrcColor a, GrcColor b, float f)
	{
		return new GrcColor(
			a.r + (b.r - a.r) * f,
			a.g + (b.g - a.g) * f,
			a.b + (b.b - a.b) * f,
			a.a + (b.a - a.a) * f
		);
	}
}
