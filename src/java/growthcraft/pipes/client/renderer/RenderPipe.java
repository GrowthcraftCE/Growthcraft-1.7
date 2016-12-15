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

import org.lwjgl.opengl.GL11;

import growthcraft.api.core.GrcColour;
import growthcraft.pipes.client.resource.GrcPipesResources;
import growthcraft.pipes.common.block.IPipeBlock;
import growthcraft.pipes.util.PipeConsts;
import growthcraft.pipes.util.PipeFlag;
import growthcraft.pipes.util.PipeType;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderPipe implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

	private void renderPipeModel(PipeType type)
	{
		if (type == PipeType.VACUUM)
		{
			GrcPipesResources.INSTANCE.modelPipe.render(PipeFlag.PIPE_VACUUM_CORE | PipeFlag.PIPE_BUSES, PipeConsts.RENDER_SCALE);
		}
		else
		{
			GrcPipesResources.INSTANCE.modelPipe.render(PipeFlag.PIPE_CORE | PipeFlag.PIPES, PipeConsts.RENDER_SCALE);
		}
	}

	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return;
		if (block instanceof IPipeBlock)
		{
			final IPipeBlock pipeBlock = (IPipeBlock)block;
			GL11.glPushMatrix();
			{
				final GrcColour colour = GrcColour.toColour(metadata);
				final Tessellator tessellator = Tessellator.instance;
				Minecraft.getMinecraft().renderEngine.bindTexture(GrcPipesResources.INSTANCE.texturePipeBase);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				//if (colour != GrcColour.Transparent)
				//{
				//	final int c = colour.blackVariant;
				//	final float r = ((c >> 16) & 0xFF) / 255.0f;
				//	final float g = ((c >> 8) & 0xFF) / 255.0f;
				//	final float b = (c & 0xFF) / 255.0f;
				//	GL11.glColor4f(r, g, b, 1.0f);
				//}
				renderPipeModel(pipeBlock.getPipeType());
				Minecraft.getMinecraft().renderEngine.bindTexture(GrcPipesResources.INSTANCE.texturePipeMask);
				if (colour != GrcColour.Transparent)
				{
					final int c = colour.mediumVariant;
					final float r = ((c >> 16) & 0xFF) / 255.0f;
					final float g = ((c >> 8) & 0xFF) / 255.0f;
					final float b = (c & 0xFF) / 255.0f;
					GL11.glColor4f(r, g, b, 1.0f);
				}
				renderPipeModel(pipeBlock.getPipeType());
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}
			GL11.glPopMatrix();
		}
	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return true;
		return false;
	}

	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	public int getRenderId()
	{
		return RENDER_ID;
	}
}
