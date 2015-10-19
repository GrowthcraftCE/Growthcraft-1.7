package growthcraft.cellar.renderer;

import growthcraft.cellar.block.BlockFruitPress;
import growthcraft.core.utils.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderFruitPress implements ISimpleBlockRenderingHandler
{
	public static final int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			final BlockFruitPress fruitPress = (BlockFruitPress)block;
			final Tessellator tes = Tessellator.instance;
			final IIcon icon = fruitPress.getIconByIndex(0);
			final IIcon icon2 = fruitPress.getIconByIndex(1);
			final double d = 0.0625D;

			renderer.setRenderBounds(1*d, 0.0D, 1*d, 3*d, 3*d, 3*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			renderer.setRenderBounds(13*d, 0.0D, 1*d, 15*d, 3*d, 3*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			renderer.setRenderBounds(1*d, 0.0D, 13*d, 3*d, 3*d, 15*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			renderer.setRenderBounds(13*d, 0.0D, 13*d, 15*d, 3*d, 15*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);

			renderer.setRenderBounds(1*d, 3*d, 1*d, 15*d, 5*d, 15*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);

			renderer.setRenderBounds(0.0D, 3*d, 0.0D, 1.0D, 7*d, 1*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			renderer.setRenderBounds(0.0D, 3*d, 15*d, 1.0D, 7*d, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			renderer.setRenderBounds(0.0D, 3*d, 1*d, 1*d, 7*d, 15*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			renderer.setRenderBounds(15*d, 3*d, 1*d, 1.0D, 7*d, 15*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);

			renderer.uvRotateEast   = 1;
			renderer.uvRotateWest   = 1;
			renderer.uvRotateSouth  = 1;
			renderer.uvRotateNorth  = 1;
			renderer.uvRotateTop    = 1;
			renderer.uvRotateBottom = 1;

			renderWoodSlatsInv(renderer, block, icon, tes,  2.0*d,  5.0*d,  2.0*d,  3.5*d);
			renderWoodSlatsInv(renderer, block, icon, tes,  5.5*d,  8.5*d,  2.0*d,  3.5*d);
			renderWoodSlatsInv(renderer, block, icon, tes,  9.0*d, 12.0*d,  2.0*d,  3.5*d);
			//
			renderWoodSlatsInv(renderer, block, icon, tes, 12.5*d, 14.0*d,  2.0*d,  5.0*d);
			renderWoodSlatsInv(renderer, block, icon, tes, 12.5*d, 14.0*d,  5.5*d,  8.5*d);
			renderWoodSlatsInv(renderer, block, icon, tes, 12.5*d, 14.0*d,  9.0*d, 12.0*d);
			//
			renderWoodSlatsInv(renderer, block, icon, tes, 11.0*d, 14.0*d, 12.5*d, 14.0*d);
			renderWoodSlatsInv(renderer, block, icon, tes,  7.5*d, 10.5*d, 12.5*d, 14.0*d);
			renderWoodSlatsInv(renderer, block, icon, tes,  4.0*d,  7.0*d, 12.5*d, 14.0*d);
			//
			renderWoodSlatsInv(renderer, block, icon, tes,  2.0*d,  3.5*d, 11.0*d, 14.0*d);
			renderWoodSlatsInv(renderer, block, icon, tes,  2.0*d,  3.5*d,  7.5*d, 10.5*d);
			renderWoodSlatsInv(renderer, block, icon, tes,  2.0*d,  3.5*d,  4.0*d,  7.0*d);

			renderer.uvRotateEast   = 0;
			renderer.uvRotateWest   = 0;
			renderer.uvRotateSouth  = 0;
			renderer.uvRotateNorth  = 0;
			renderer.uvRotateTop    = 0;
			renderer.uvRotateBottom = 0;

			renderMetalRingsInv(renderer, block, icon2, tes,  7.5*d,  9.5*d);
			renderMetalRingsInv(renderer, block, icon2, tes, 12.5*d, 14.5*d);

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	private void renderWoodSlatsInv(RenderBlocks r, Block b, IIcon i, Tessellator t, double x1, double x2, double z1, double z2)
	{
		r.setRenderBounds(x1, 0.375D, z1, x2, 1.0D, z2);
		RenderUtils.drawInventoryBlock_icon(b, r, i, t);
	}

	private void renderMetalRingsInv(RenderBlocks r, Block b, IIcon i, Tessellator t, double y1, double y2)
	{
		final double d = 0.0625D;
		r.setRenderBounds(1.5*d, y1, 1.5*d, 2.5*d,  y2, 14.5*d);
		RenderUtils.drawInventoryBlock_icon(b, r, i, t);
		r.setRenderBounds(13.5*d, y1, 1.5*d, 14.5*d,  y2, 14.5*d);
		RenderUtils.drawInventoryBlock_icon(b, r, i, t);
		r.setRenderBounds(2.5*d, y1, 1.5*d, 13.5*d,  y2, 2.5*d);
		RenderUtils.drawInventoryBlock_icon(b, r, i, t);
		r.setRenderBounds(2.5*d, y1, 13.5*d, 13.5*d,  y2, 14.5*d);
		RenderUtils.drawInventoryBlock_icon(b, r, i, t);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			final BlockFruitPress fruitPress = (BlockFruitPress)block;
			final int    m    = world.getBlockMetadata(x, y, z);
			final double d    = 0.0625D;

			final Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			final float f = 1.0F;
			final int color = block.colorMultiplier(world, x, y, z);
			float r = (float)(color >> 16 & 255) / 255.0F;
			float g = (float)(color >> 8 & 255) / 255.0F;
			float b = (float)(color & 255) / 255.0F;

			if (EntityRenderer.anaglyphEnable)
			{
				final float f5 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
				final float f4 = (r * 30.0F + g * 70.0F) / 100.0F;
				final float f6 = (r * 30.0F + b * 70.0F) / 100.0F;
				r = f5;
				g = f4;
				b = f6;
			}

			tessellator.setColorOpaque_F(f * r, f * g, f * b);

			// Render Legs
			// upper left - upper right - lower left - lower right
			renderer.setRenderBounds(1*d, 0.0D, 1*d, 3*d, 3*d, 3*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(13*d, 0.0D, 1*d, 15*d, 3*d, 3*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(1*d, 0.0D, 13*d, 3*d, 3*d, 15*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(13*d, 0.0D, 13*d, 15*d, 3*d, 15*d);
			renderer.renderStandardBlock(block, x, y, z);

			// Render Base - Main Plate
			renderer.setRenderBounds(1*d, 3*d, 1*d, 15*d, 5*d, 15*d);
			renderer.renderStandardBlock(block, x, y, z);

			// Render Base - Side Plates
			if (m == 0)
			{
				renderer.setRenderBounds( 0*d,  3*d,  0*d, 16*d,  7*d,  1*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 0*d,  3*d, 15*d, 16*d,  7*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				//
				renderer.setRenderBounds( 0*d,  3*d,  1*d,  1*d,  7*d,  7*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 0*d,  3*d,  9*d,  1*d,  7*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				//
				renderer.setRenderBounds(15*d,  3*d,  1*d, 16*d,  7*d,  7*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(15*d,  3*d,  9*d, 16*d,  7*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (m == 1)
			{
				renderer.setRenderBounds( 0*d,  3*d,  0*d,  1*d,  7*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(15*d,  3*d,  0*d, 16*d,  7*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				//
				renderer.setRenderBounds( 1*d,  3*d,  0*d,  7*d,  7*d,  1*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 9*d,  3*d,  0*d, 15*d,  7*d,  1*d);
				renderer.renderStandardBlock(block, x, y, z);
				//
				renderer.setRenderBounds( 1*d,  3*d, 15*d,  7*d,  7*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 9*d,  3*d, 15*d, 15*d,  7*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
			}

			renderer.uvRotateEast   = 1;
			renderer.uvRotateWest   = 1;
			renderer.uvRotateSouth  = 1;
			renderer.uvRotateNorth  = 1;
			renderer.uvRotateTop    = 1;
			renderer.uvRotateBottom = 1;

			// Render Piston Supports
			if (m == 0)
			{
				renderer.setRenderBounds( 0*d,  3*d,  7*d,  2*d, 16*d,  9*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d,  3*d,  7*d, 16*d, 16*d,  9*d);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (m == 1)
			{
				renderer.setRenderBounds( 7*d,  3*d,  0*d,  9*d, 16*d,  2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 7*d,  3*d, 14*d,  9*d, 16*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
			}

			// Render Bin - Wood Slats
			//
			renderWoodSlats(renderer, block, x, y, z,  2.0*d,  5.0*d,  2.0*d,  3.5*d);
			renderWoodSlats(renderer, block, x, y, z,  5.5*d,  8.5*d,  2.0*d,  3.5*d);
			renderWoodSlats(renderer, block, x, y, z,  9.0*d, 12.0*d,  2.0*d,  3.5*d);
			//
			renderWoodSlats(renderer, block, x, y, z, 12.5*d, 14.0*d,  2.0*d,  5.0*d);
			renderWoodSlats(renderer, block, x, y, z, 12.5*d, 14.0*d,  5.5*d,  8.5*d);
			renderWoodSlats(renderer, block, x, y, z, 12.5*d, 14.0*d,  9.0*d, 12.0*d);
			//
			renderWoodSlats(renderer, block, x, y, z, 11.0*d, 14.0*d, 12.5*d, 14.0*d);
			renderWoodSlats(renderer, block, x, y, z,  7.5*d, 10.5*d, 12.5*d, 14.0*d);
			renderWoodSlats(renderer, block, x, y, z,  4.0*d,  7.0*d, 12.5*d, 14.0*d);
			//
			renderWoodSlats(renderer, block, x, y, z,  2.0*d,  3.5*d, 11.0*d, 14.0*d);
			renderWoodSlats(renderer, block, x, y, z,  2.0*d,  3.5*d,  7.5*d, 10.5*d);
			renderWoodSlats(renderer, block, x, y, z,  2.0*d,  3.5*d,  4.0*d,  7.0*d);

			renderer.uvRotateEast   = 0;
			renderer.uvRotateWest   = 0;
			renderer.uvRotateSouth  = 0;
			renderer.uvRotateNorth  = 0;
			renderer.uvRotateTop    = 0;
			renderer.uvRotateBottom = 0;

			renderer.setOverrideBlockTexture(fruitPress.getIconByIndex(1));

			// Render Bin - Metal Rings
			renderMetalRings(renderer, block, x, y, z,  7.5*d,  9.5*d);
			renderMetalRings(renderer, block, x, y, z, 12.5*d, 14.5*d);

			renderer.clearOverrideBlockTexture();

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
		return true;
	}

	private void renderWoodSlats(RenderBlocks r, Block b, int x, int y, int z, double x1, double x2, double z1, double z2)
	{
		r.setRenderBounds(x1, 0.375D, z1, x2, 1.0D, z2);
		r.renderStandardBlock(b, x, y, z);
	}

	private void renderMetalRings(RenderBlocks r, Block b, int x, int y, int z, double y1, double y2)
	{
		final double d = 0.0625D;
		r.setRenderBounds(1.5*d, y1, 1.5*d, 2.5*d,  y2, 14.5*d);
		r.renderStandardBlock(b, x, y, z);
		r.setRenderBounds(13.5*d, y1, 1.5*d, 14.5*d,  y2, 14.5*d);
		r.renderStandardBlock(b, x, y, z);
		r.setRenderBounds(2.5*d, y1, 1.5*d, 13.5*d,  y2, 2.5*d);
		r.renderStandardBlock(b, x, y, z);
		r.setRenderBounds(2.5*d, y1, 13.5*d, 13.5*d,  y2, 14.5*d);
		r.renderStandardBlock(b, x, y, z);
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
