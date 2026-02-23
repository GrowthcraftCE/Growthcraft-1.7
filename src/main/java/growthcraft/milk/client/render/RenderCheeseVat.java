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
import growthcraft.api.core.util.ColorUtils;
import growthcraft.milk.client.model.ModelCheeseVat;
import growthcraft.milk.client.resource.GrcMilkResources;
import growthcraft.milk.common.block.BlockCheeseVat;
import growthcraft.milk.common.tileentity.TileEntityCheeseVat;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderCheeseVat implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	private static final BBox fluidBBox = BBox.newCube(1, 1, 1, 14, 14, 14).scale(ModelCheeseVat.SCALE);

	@Override
	public int getRenderId()
	{
		return RENDER_ID;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == RENDER_ID)
		{
			GL11.glPushMatrix();
			{
				Minecraft.getMinecraft().renderEngine.bindTexture(GrcMilkResources.INSTANCE.textureCheeseVat);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.0f, -1.0f, 0.0f);
				GrcMilkResources.INSTANCE.modelCheeseVat.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ModelCheeseVat.SCALE);
			}
			GL11.glPopMatrix();
		}
	}

	private void renderFluidLayer(Block block, RenderBlocks renderer, Fluid fluid, double y0, double y1, int x, int y, int z)
	{
		if (fluid == null) return;
		final IIcon icon = fluid.getIcon();
		if (icon == null) return;
		final float[] colorAry = new float[3];
		final int color = fluid.getColor();
		ColorUtils.rgb24FloatArray(colorAry, color);
		Tessellator.instance.setColorOpaque_F(colorAry[0], colorAry[1], colorAry[2]);

		renderer.setRenderBounds(fluidBBox.x0(), y0, fluidBBox.z0(), fluidBBox.x1(), y1, fluidBBox.z1());
		renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, icon);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == RENDER_ID)
		{
			if (block instanceof BlockCheeseVat)
			{
				final BlockCheeseVat pancheonBlock = (BlockCheeseVat)block;
				final TileEntityCheeseVat cheeseVatTile = pancheonBlock.getTileEntity(world, x, y, z);
				if (cheeseVatTile != null)
				{
					double y0 = ModelCheeseVat.SCALE;
					for (int i = 0; i < cheeseVatTile.getTankCount(); ++i)
					{
						final FluidStack fluid = cheeseVatTile.getFluidStack(i);
						if (fluid != null)
						{
							final float fluidHeight = fluid.amount * fluidBBox.h() / cheeseVatTile.getVatFluidCapacity();
							renderFluidLayer(block, renderer, fluid.getFluid(), y0, y0 + fluidHeight, x, y, z);
							y0 += fluidHeight;
						}
					}
				}
			}
		}
		return true;
	}
}
