package growthcraft.grapes;

import growthcraft.core.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderGrapeVine1 implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			Tessellator tes = Tessellator.instance;
			double d = 0.0625D;
			renderer.setRenderBounds(6*d, 0.0D, 6*d, 10*d, 0.75D, 10*d);
			Utils.drawInventoryBlock_icon(block, renderer, BlockGrapeVine1.tex[0], tes);
			if (renderer.useInventoryTint)
			{
				int color = ColorizerFoliage.getFoliageColorBasic();

				float f1 = (float)(color >> 16 & 255) / 255.0F;
				float f2 = (float)(color >> 8 & 255) / 255.0F;
				float f3 = (float)(color & 255) / 255.0F;
				GL11.glColor4f(f1, f2, f3, 1.0F);
			}
			renderer.setRenderBounds(4*d, 0.5D, 4*d, 12*d, 1.0D, 12*d);
			Utils.drawInventoryBlock_icon(block, renderer, ((BlockGrapeVine1)block).getLeafTexture(), tes);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			int meta = world.getBlockMetadata(x, y, z);
			double d = 0.0625D;

			//render trunk
			if (meta == 0)
			{
				renderer.setRenderBounds(6*d, 0.0D, 6*d, 10*d, 0.75D, 10*d);
			}
			else if (meta == 1)
			{
				renderer.setRenderBounds(6*d, 0.0D, 6*d, 10*d, 1.0D, 10*d);
			}
			renderer.renderStandardBlock(block, x, y, z);

			//render leaves
			if (meta == 0)
			{
				int color;
				int r = 0;
				int g = 0;
				int b = 0;

				for (int l1 = -1; l1 <= 1; ++l1)
				{
					for (int i2 = -1; i2 <= 1; ++i2)
					{
						int j2 = world.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
						r += (j2 & 16711680) >> 16;
					g += (j2 & 65280) >> 8;
					b += j2 & 255;
					}
				}

				color = (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
				float red = (float)(color >> 16 & 255) / 255.0F;
				float gre = (float)(color >> 8 & 255) / 255.0F;
				float blu = (float)(color & 255) / 255.0F;

				renderer.setOverrideBlockTexture(((BlockGrapeVine1)block).getLeafTexture());
				renderer.setRenderBounds(4*d, 0.5D, 4*d, 12*d, 1.0D, 12*d);
				renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, red, gre, blu);
				renderer.clearOverrideBlockTexture();
			}
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) { return true; }

	@Override
	public int getRenderId()
	{
		return id;
	}
}
