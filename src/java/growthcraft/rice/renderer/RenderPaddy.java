package growthcraft.rice.renderer;

import growthcraft.core.Utils;
import growthcraft.rice.ClientProxy;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.block.BlockPaddy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderPaddy implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		Tessellator tes = Tessellator.instance;
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Utils.drawFace("yneg", block, renderer, tes, BlockPaddy.tex[0], 0.0D, 0.0D, 0.0D);
		Utils.drawFace("ypos", block, renderer, tes, BlockPaddy.tex[1], 0.0D, 0.0D, 0.0D);
		Utils.drawFace("zneg", block, renderer, tes, BlockPaddy.tex[0], 0.0D, 0.0D, 0.0D);
		Utils.drawFace("zpos", block, renderer, tes, BlockPaddy.tex[0], 0.0D, 0.0D, 0.0D);
		Utils.drawFace("xneg", block, renderer, tes, BlockPaddy.tex[0], 0.0D, 0.0D, 0.0D);
		Utils.drawFace("xpos", block, renderer, tes, BlockPaddy.tex[0], 0.0D, 0.0D, 0.0D);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

		double i1 = 0.0D;
		double i2 = 1.0D;
		double j1 = 0.875D;
		double j2 = 1.0D;
		double k1 = 0.0D;
		double k2 = 1.0D;

		double thick = 0.125D;

		i1 = 1.0D - thick;
		i2 = 1.0D;
		k1 = 0.0D + thick;
		k2 = 1.0D - thick;

		renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		i1 = 0.0D;
		i2 = 0.0D + thick;
		k1 = 0.0D + thick;
		k2 = 1.0D - thick;

		renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		i1 = 0.0D + thick;
		i2 = 1.0D - thick;
		k1 = 1.0D - thick;
		k2 = 1.0D;

		renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		i1 = 0.0D + thick;
		i2 = 1.0D - thick;
		k1 = 0.0D;
		k2 = 0.0D + thick;

		renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		//corners
		renderer.setRenderBounds(0.0D, j1, 0.0D, 0.0D + thick, j2, 0.0D + thick);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		renderer.setRenderBounds(1.0D - thick, j1, 0.0D, 1.0D, j2, 0.0D + thick);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		renderer.setRenderBounds(0.0D, j1, 1.0D - thick, 0.0D + thick, j2, 1.0D);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);

		renderer.setRenderBounds(1.0D - thick, j1, 1.0D - thick, 1.0D, j2, 1.0D);
		Utils.drawInventoryBlock_icon(block, renderer, BlockPaddy.tex[0], tes);
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			int meta = world.getBlockMetadata(x, y, z);
			// temporary fix
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
			renderer.renderStandardBlock(block, x, y, z);
			// temporary fix

			renderer.renderAllFaces = true;

			if(ClientProxy.renderPass == 0)
			{
				// main block
				renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
				renderer.renderStandardBlock(block, x, y, z);

				// borders
				renderer.setOverrideBlockTexture(BlockPaddy.tex[0]);

				double i1 = 0.0D;
				double i2 = 1.0D;
				double j1 = 0.875D;
				double j2 = 1.0D;
				double k1 = 0.0D;
				double k2 = 1.0D;

				double thick = 0.125D;

				boolean boolXPos = ((BlockPaddy)block).canConnectPaddyTo(world, x + 1, y, z, meta);
				boolean boolXNeg = ((BlockPaddy)block).canConnectPaddyTo(world, x - 1, y, z, meta);
				boolean boolYPos = ((BlockPaddy)block).canConnectPaddyTo(world, x, y, z + 1, meta);
				boolean boolYNeg = ((BlockPaddy)block).canConnectPaddyTo(world, x, y, z - 1, meta);

				if (boolXPos == false)
				{
					i1 = 1.0D - thick;
					i2 = 1.0D;
					k1 = 0.0D + thick;
					k2 = 1.0D - thick;

					renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if (boolXNeg == false)
				{
					i1 = 0.0D;
					i2 = 0.0D + thick;
					k1 = 0.0D + thick;
					k2 = 1.0D - thick;

					renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if (boolYPos == false)
				{
					i1 = 0.0D + thick;
					i2 = 1.0D - thick;
					k1 = 1.0D - thick;
					k2 = 1.0D;

					renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if (boolYNeg == false)
				{
					i1 = 0.0D + thick;
					i2 = 1.0D - thick;
					k1 = 0.0D;
					k2 = 0.0D + thick;

					renderer.setRenderBounds(i1, j1, k1, i2, j2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				//corners
				if ((((BlockPaddy)block).canConnectPaddyTo(world, x - 1, y, z - 1, meta) == false) || (boolXNeg == false) || (boolYNeg == false))
				{
					renderer.setRenderBounds(0.0D, j1, 0.0D, 0.0D + thick, j2, 0.0D + thick);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if ((((BlockPaddy)block).canConnectPaddyTo(world, x + 1, y, z - 1, meta) == false) || (boolXPos == false) || (boolYNeg == false))
				{
					renderer.setRenderBounds(1.0D - thick, j1, 0.0D, 1.0D, j2, 0.0D + thick);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if ((((BlockPaddy)block).canConnectPaddyTo(world, x - 1, y, z + 1, meta) == false) || (boolXNeg == false) || (boolYPos == false))
				{
					renderer.setRenderBounds(0.0D, j1, 1.0D - thick, 0.0D + thick, j2, 1.0D);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if ((((BlockPaddy)block).canConnectPaddyTo(world, x + 1, y, z + 1, meta) == false) || (boolXPos == false) || (boolYPos == false))
				{
					renderer.setRenderBounds(1.0D - thick, j1, 1.0D - thick, 1.0D, j2, 1.0D);
					renderer.renderStandardBlock(block, x, y, z);
				}

				renderer.clearOverrideBlockTexture();
			}
			else
			{
				// water
				if (meta > 0)
				{
					Tessellator tessellator = Tessellator.instance;
					//tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
					tessellator.setBrightness(this.mixedBrightness(world, x, y, z));

					float f = 1.0F;
					//int color = block.colorMultiplier(world, x, y, z);
					int color = this.colorMultiplier(world, x, y, z);
					//int l = 5461345; //6973773;//16752000;//12345183;//12326936;//16758432;
					float r = (float)(color >> 16 & 255) / 255.0F;
					float g = (float)(color >> 8 & 255) / 255.0F;
					float b = (float)(color & 255) / 255.0F;
					float f4;

					if (EntityRenderer.anaglyphEnable)
					{
						float f5 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
						f4 = (r * 30.0F + g * 70.0F) / 100.0F;
						float f6 = (r * 30.0F + b * 70.0F) / 100.0F;
						r = f5;
						g = f4;
						b = f6;
					}

					tessellator.setColorOpaque_F(f * r, f * g, f * b);
					//Icon icon = GrowthCraftCore.liquidSmoothTexture;
					IIcon icon = Blocks.water.getBlockTextureFromSide(1);
					renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
					renderer.renderFaceYPos(block, (double)x, (double)((float)y - 0.0625F), (double)z, icon);
				}
			}

			renderer.renderAllFaces = false;
		}
		return true;
	}

	private int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		int l = 0;
		int i1 = 0;
		int j1 = 0;

		for (int k1 = -1; k1 <= 1; ++k1)
		{
			for (int l1 = -1; l1 <= 1; ++l1)
			{
				int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getWaterColorMultiplier();
				l += (i2 & 16711680) >> 16;
			i1 += (i2 & 65280) >> 8;
			j1 += i2 & 255;
			}
		}

		return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
	}

	private int mixedBrightness(IBlockAccess world, int x, int y, int z)
	{
		int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int i1 = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
		int j1 = l & 255;
		int k1 = i1 & 255;
		int l1 = l >> 16 & 255;
		int i2 = i1 >> 16 & 255;
		return (j1 > k1 ? j1 : k1) | (l1 > i2 ? l1 : i2) << 16;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) { return true; }

	@Override
	public int getRenderId()
	{
		return id;
	}

}
