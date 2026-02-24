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
package growthcraft.core.client.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class GrcGuiRenderHelper
{
	private GrcGuiRenderHelper() {}

	public static void drawTexturedModelRectFromIcon(int x, int y, float z, IIcon icon, int w, int h)
	{
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)z, (double)icon.getMinU(), (double)icon.getMaxV());
		tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)z, (double)icon.getMaxU(), (double)icon.getMaxV());
		tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)z, (double)icon.getMaxU(), (double)icon.getMinV());
		tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z, (double)icon.getMinU(), (double)icon.getMinV());
		tessellator.draw();
	}
}
