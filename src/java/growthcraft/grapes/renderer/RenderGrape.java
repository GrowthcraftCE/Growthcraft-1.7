package growthcraft.grapes.renderer;

import growthcraft.core.Utils;
import growthcraft.grapes.block.BlockGrapeBlock;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderGrape implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			IIcon icon = BlockGrapeBlock.tex[0];
			double d = 0.0625D;

			long random = (long)(x * 3129871) ^ (long)y * 116129781L ^ (long)z;
			random = random * random * 42317861L + random * 11L;

			double minX = (double)x + 3*d;
			minX += ((double)((float)(random >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			double maxX = minX + 10*d;
			double minY = (double)y + 9*d;
			minY += ((double)((float)(random >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			double maxY = minY + 10*d;
			double minZ = (double)z + 3*d;
			minZ += ((double)((float)(random >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
			double maxZ = minZ + 10*d;

			double minU = (double)icon.getMinU();
			double maxU = (double)icon.getMaxU();
			double minV = (double)icon.getMinV();
			double maxV = (double)icon.getMaxV();

			Utils.drawCrossSquaresAlongY(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) {return false;}

	@Override
	public int getRenderId()
	{
		return id;
	}

}
