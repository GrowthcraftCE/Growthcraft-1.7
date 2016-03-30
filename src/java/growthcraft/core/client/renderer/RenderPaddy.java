package growthcraft.core.client.renderer;

import growthcraft.core.client.ClientProxy;
import growthcraft.core.common.block.BlockPaddyBase;
import growthcraft.core.util.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

public class RenderPaddy implements ISimpleBlockRenderingHandler
{
	public static final int id = RenderingRegistry.getNextAvailableRenderId();

	private int mixedBrightness(IBlockAccess world, int x, int y, int z)
	{
		final int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		final int i1 = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
		final int j1 = l & 255;
		final int k1 = i1 & 255;
		final int l1 = l >> 16 & 255;
		final int i2 = i1 >> 16 & 255;
		return (j1 > k1 ? j1 : k1) | (l1 > i2 ? l1 : i2) << 16;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (id != modelID) return;

		if (block instanceof BlockPaddyBase)
		{
			final Tessellator tes = Tessellator.instance;
			final BlockPaddyBase paddyBlock = (BlockPaddyBase)block;
			final IIcon bottomFaceIcon = paddyBlock.getIcon(0, metadata);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			RenderUtils.drawFace(RenderUtils.Face.YNEG, block, renderer, tes, bottomFaceIcon, 0.0D, 0.0D, 0.0D);
			RenderUtils.drawFace(RenderUtils.Face.YPOS, block, renderer, tes, paddyBlock.getIcon(1, metadata), 0.0D, 0.0D, 0.0D);
			RenderUtils.drawFace(RenderUtils.Face.ZNEG, block, renderer, tes, paddyBlock.getIcon(2, metadata), 0.0D, 0.0D, 0.0D);
			RenderUtils.drawFace(RenderUtils.Face.ZPOS, block, renderer, tes, paddyBlock.getIcon(3, metadata), 0.0D, 0.0D, 0.0D);
			RenderUtils.drawFace(RenderUtils.Face.XNEG, block, renderer, tes, paddyBlock.getIcon(4, metadata), 0.0D, 0.0D, 0.0D);
			RenderUtils.drawFace(RenderUtils.Face.XPOS, block, renderer, tes, paddyBlock.getIcon(5, metadata), 0.0D, 0.0D, 0.0D);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			final double thick = 0.125D;
			final double y1 = 0.875D;
			final double y2 = 1.0D;
			double i1 = 0.0D;
			double i2 = 1.0D;
			double k1 = 0.0D;
			double k2 = 1.0D;

			i1 = 1.0D - thick;
			i2 = 1.0D;
			k1 = 0.0D + thick;
			k2 = 1.0D - thick;

			renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			i1 = 0.0D;
			i2 = 0.0D + thick;
			k1 = 0.0D + thick;
			k2 = 1.0D - thick;

			renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			i1 = 0.0D + thick;
			i2 = 1.0D - thick;
			k1 = 1.0D - thick;
			k2 = 1.0D;

			renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			i1 = 0.0D + thick;
			i2 = 1.0D - thick;
			k1 = 0.0D;
			k2 = 0.0D + thick;

			renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			//corners
			renderer.setRenderBounds(0.0D, y1, 0.0D, 0.0D + thick, y2, 0.0D + thick);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			renderer.setRenderBounds(1.0D - thick, y1, 0.0D, 1.0D, y2, 0.0D + thick);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			renderer.setRenderBounds(0.0D, y1, 1.0D - thick, 0.0D + thick, y2, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);

			renderer.setRenderBounds(1.0D - thick, y1, 1.0D - thick, 1.0D, y2, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, bottomFaceIcon, tes);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			final int meta = world.getBlockMetadata(x, y, z);
			final BlockPaddyBase paddyBlock = (BlockPaddyBase)block;
			// temporary fix
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
			renderer.renderStandardBlock(block, x, y, z);
			// temporary fix

			renderer.renderAllFaces = true;
			final double y1 = 0.875D;
			final double y2 = 1.0D;

			if (ClientProxy.paddyRenderPass == 0)
			{
				// main block
				renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
				renderer.renderStandardBlock(block, x, y, z);

				// borders
				renderer.setOverrideBlockTexture(block.getIcon(0, meta));

				double i1 = 0.0D;
				double i2 = 1.0D;
				double k1 = 0.0D;
				double k2 = 1.0D;

				final double thick = 0.125D;

				final boolean boolXPos = paddyBlock.canConnectPaddyTo(world, x + 1, y, z, meta);
				final boolean boolXNeg = paddyBlock.canConnectPaddyTo(world, x - 1, y, z, meta);
				final boolean boolYPos = paddyBlock.canConnectPaddyTo(world, x, y, z + 1, meta);
				final boolean boolYNeg = paddyBlock.canConnectPaddyTo(world, x, y, z - 1, meta);

				if (!boolXPos)
				{
					i1 = 1.0D - thick;
					i2 = 1.0D;
					k1 = 0.0D + thick;
					k2 = 1.0D - thick;

					renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if (!boolXNeg)
				{
					i1 = 0.0D;
					i2 = 0.0D + thick;
					k1 = 0.0D + thick;
					k2 = 1.0D - thick;

					renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if (!boolYPos)
				{
					i1 = 0.0D + thick;
					i2 = 1.0D - thick;
					k1 = 1.0D - thick;
					k2 = 1.0D;

					renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if (!boolYNeg)
				{
					i1 = 0.0D + thick;
					i2 = 1.0D - thick;
					k1 = 0.0D;
					k2 = 0.0D + thick;

					renderer.setRenderBounds(i1, y1, k1, i2, y2, k2);
					renderer.renderStandardBlock(block, x, y, z);
				}

				//corners
				if ((!paddyBlock.canConnectPaddyTo(world, x - 1, y, z - 1, meta)) || (!boolXNeg) || (!boolYNeg))
				{
					renderer.setRenderBounds(0.0D, y1, 0.0D, 0.0D + thick, y2, 0.0D + thick);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if ((!paddyBlock.canConnectPaddyTo(world, x + 1, y, z - 1, meta)) || (!boolXPos) || (!boolYNeg))
				{
					renderer.setRenderBounds(1.0D - thick, y1, 0.0D, 1.0D, y2, 0.0D + thick);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if ((!paddyBlock.canConnectPaddyTo(world, x - 1, y, z + 1, meta)) || (!boolXNeg) || (!boolYPos))
				{
					renderer.setRenderBounds(0.0D, y1, 1.0D - thick, 0.0D + thick, y2, 1.0D);
					renderer.renderStandardBlock(block, x, y, z);
				}

				if ((!paddyBlock.canConnectPaddyTo(world, x + 1, y, z + 1, meta)) || (!boolXPos) || (!boolYPos))
				{
					renderer.setRenderBounds(1.0D - thick, y1, 1.0D - thick, 1.0D, y2, 1.0D);
					renderer.renderStandardBlock(block, x, y, z);
				}

				renderer.clearOverrideBlockTexture();
			}
			else
			{
				// fluid
				if (meta > 0)
				{
					final Tessellator tessellator = Tessellator.instance;
					tessellator.setBrightness(mixedBrightness(world, x, y, z));

					final float f = 1.0F;
					final int color = this.colorMultiplier(world, x, y, z);
					float r = (float)(color >> 16 & 255) / 255.0F;
					float g = (float)(color >> 8 & 255) / 255.0F;
					float b = (float)(color & 255) / 255.0F;
					float f4;

					if (EntityRenderer.anaglyphEnable)
					{
						final float f5 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
						f4 = (r * 30.0F + g * 70.0F) / 100.0F;
						final float f6 = (r * 30.0F + b * 70.0F) / 100.0F;
						r = f5;
						g = f4;
						b = f6;
					}

					tessellator.setColorOpaque_F(f * r, f * g, f * b);
					final IIcon icon = paddyBlock.getFluidBlock().getBlockTextureFromSide(1);
					renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
					final double fy = (double)y - (1d / 32d) - (y2 - y1) * ((double)(7 - meta) / 7d);
					renderer.renderFaceYPos(block, (double)x, fy, (double)z, icon);
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
				final int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getWaterColorMultiplier();
				l += (i2 & 16711680) >> 16;
				i1 += (i2 & 65280) >> 8;
				j1 += i2 & 255;
			}
		}

		return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return id;
	}
}
