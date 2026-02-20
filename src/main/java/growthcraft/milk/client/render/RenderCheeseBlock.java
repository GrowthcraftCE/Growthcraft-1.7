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
package growthcraft.milk.client.render;

import growthcraft.api.core.util.BBox;
import growthcraft.milk.common.block.BlockCheeseBlock;
import growthcraft.milk.common.tileentity.TileEntityCheeseBlock;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderCheeseBlock implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	private static float SCALE = 1f / 16f;
	private static BBox[] boxes = {
		BBox.newCube(5, 0, 5, 6, 8, 6).scale(SCALE),
		BBox.newCube(4, 0, 6, 1, 8, 4).scale(SCALE),
		BBox.newCube(11, 0, 6, 1, 8, 4).scale(SCALE),
		BBox.newCube(6, 0, 4, 4, 8, 1).scale(SCALE),
		BBox.newCube(6, 0, 11, 4, 8, 1).scale(SCALE)
	};

	@Override
	public int getRenderId()
	{
		return RENDER_ID;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return false;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == RENDER_ID)
		{
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == RENDER_ID)
		{
			if (block instanceof BlockCheeseBlock)
			{
				final BlockCheeseBlock cheeseBlock = (BlockCheeseBlock)block;
				final TileEntityCheeseBlock te = cheeseBlock.getTileEntity(world, x, y, z);
				if (te != null)
				{
					final int slices = te.getCheese().getSlices();
					final int slicesMax = te.getCheese().getSlicesMax();
					final float scaleHeight = (float)slices / (float)slicesMax;
					if (scaleHeight > 0f)
					{
						final Tessellator tes = Tessellator.instance;
						tes.setColorOpaque_F(1.0f, 1.0f, 1.0f);
						for (BBox box : boxes)
						{
							final float y1 = scaleHeight * box.y1();
							renderer.setRenderBounds(box.x0(), box.y0(), box.z0(), box.x1(), y1, box.z1());
							renderer.renderStandardBlock(block, x, y, z);
						}
					}
				}
			}
		}
		return true;
	}
}
