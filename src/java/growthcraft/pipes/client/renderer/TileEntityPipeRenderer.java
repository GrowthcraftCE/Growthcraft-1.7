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
package growthcraft.pipes.client.renderer;

import growthcraft.api.core.GrcColour;
import growthcraft.pipes.client.resource.GrcPipesResources;
import growthcraft.pipes.common.tileentity.TileEntityPipeBase;
import growthcraft.pipes.util.PipeConsts;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntityPipeRenderer extends TileEntitySpecialRenderer
{
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick)
	{
		final TileEntityPipeBase pipeBase = (TileEntityPipeBase)te;
		final GrcColour colour = pipeBase.getColour();
		final int renderState = pipeBase.getPipeRenderState();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			bindTexture(GrcPipesResources.INSTANCE.texturePipeBase);
			//if (colour != GrcColour.Transparent)
			//{
			//	final int c = colour.blackVariant;
			//	final float r = ((c >> 16) & 0xFF) / 255.0f;
			//	final float g = ((c >> 8) & 0xFF) / 255.0f;
			//	final float b = (c & 0xFF) / 255.0f;
			//	GL11.glColor4f(r, g, b, 1.0f);
			//}
			GrcPipesResources.INSTANCE.modelPipe.render(renderState, PipeConsts.RENDER_SCALE);
			bindTexture(GrcPipesResources.INSTANCE.texturePipeMask);
			if (colour != GrcColour.Transparent)
			{
				final int c = colour.mediumVariant;
				final float r = ((c >> 16) & 0xFF) / 255.0f;
				final float g = ((c >> 8) & 0xFF) / 255.0f;
				final float b = (c & 0xFF) / 255.0f;
				GL11.glColor4f(r, g, b, 1.0f);
			}
			GrcPipesResources.INSTANCE.modelPipe.render(renderState, PipeConsts.RENDER_SCALE);
		}
		GL11.glPopMatrix();
	}
}
