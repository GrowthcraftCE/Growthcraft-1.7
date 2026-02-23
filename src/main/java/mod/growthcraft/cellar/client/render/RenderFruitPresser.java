package growthcraft.cellar.client.render;

import growthcraft.cellar.common.block.BlockFruitPresser;
import growthcraft.core.util.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderFruitPresser implements ISimpleBlockRenderingHandler
{
	public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

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
			final BlockFruitPresser fruitPresser = (BlockFruitPresser)block;
			final Tessellator tes = Tessellator.instance;
			final IIcon[] icons = { fruitPresser.getIconByIndex(0), fruitPresser.getIconByIndex(1), fruitPresser.getIconByIndex(2), fruitPresser.getIconByIndex(2), fruitPresser.getIconByIndex(2), fruitPresser.getIconByIndex(2) };
			final double d    = 0.0625D;
			// Render Machine
			renderer.setRenderBounds( 3*d,  7*d,  3*d, 13*d, 15*d, 13*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == RENDER_ID)
		{
			final BlockFruitPresser fruitPresser = (BlockFruitPresser)block;
			final IIcon[] icon = {fruitPresser.getIconByIndex(0), fruitPresser.getIconByIndex(1), fruitPresser.getIconByIndex(2), fruitPresser.getIconByIndex(3)};
			final int    m    = world.getBlockMetadata(x, y, z);
			final double d    = 0.0625D;

			// Render Machine
			renderer.setRenderBounds( 3*d,  7*d,  3*d, 13*d, 15*d, 13*d);
			renderer.renderStandardBlock(block, x, y, z);

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

			// Render Supports
			renderer.setOverrideBlockTexture(icon[3]);

			if (m == 0 || m == (0 | 2))
			{
				renderer.setRenderBounds( 2*d, 12.25*d, 7.25*d,  3*d, 13.75*d, 8.75*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 2*d,  8.25*d, 7.25*d,  3*d,  9.75*d, 8.75*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 12.25*d, 7.25*d, 14*d, 13.75*d, 8.75*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d,  8.25*d, 7.25*d, 14*d,  9.75*d, 8.75*d);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (m == 1 || m == (1 | 2))
			{
				renderer.setRenderBounds( 7.25*d, 12.25*d,  2*d, 8.75*d, 13.75*d,  3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 7.25*d,  8.25*d,  2*d, 8.75*d,  9.75*d,  3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 7.25*d, 12.25*d, 13*d, 8.75*d, 13.75*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 7.25*d,  8.25*d, 13*d, 8.75*d,  9.75*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
			}


			renderer.uvRotateEast   = 1;
			renderer.uvRotateWest   = 1;
			renderer.uvRotateSouth  = 1;
			renderer.uvRotateNorth  = 1;
			renderer.uvRotateTop    = 1;
			renderer.uvRotateBottom = 1;

			if (m == 0 || m == (0 | 2))
			{
				renderer.setRenderBounds( 0*d,  0*d,  7*d,  2*d, 16*d,  9*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d,  0*d,  7*d, 16*d, 16*d,  9*d);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (m == 1 || m == (1 | 2))
			{
				renderer.setRenderBounds( 7*d,  0*d,  0*d,  9*d, 16*d,  2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 7*d,  0*d, 14*d,  9*d, 16*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
			}

			renderer.uvRotateEast   = 0;
			renderer.uvRotateWest   = 0;
			renderer.uvRotateSouth  = 0;
			renderer.uvRotateNorth  = 0;
			renderer.uvRotateTop    = 0;
			renderer.uvRotateBottom = 0;
			renderer.clearOverrideBlockTexture();

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			return true;
		}
		return false;
	}
}
