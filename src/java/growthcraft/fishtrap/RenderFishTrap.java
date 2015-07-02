package growthcraft.fishtrap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderFishTrap implements ISimpleBlockRenderingHandler 
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) 
	{
		if (modelId == id)
		{

		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		if (modelId == id)
		{
			int meta = world.getBlockMetadata(x, y, z);
			renderer.renderStandardBlock(block, x, y, z);
			Tessellator tes = Tessellator.instance;
			tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			float f = 1.0F;
			int color = block.colorMultiplier(world, x, y, z);
			float r = (float)(color >> 16 & 255) / 255.0F;
			float g = (float)(color >> 8 & 255) / 255.0F;
			float b = (float)(color & 255) / 255.0F;

			if (EntityRenderer.anaglyphEnable)
			{
				float f5 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
				float f4 = (r * 30.0F + g * 70.0F) / 100.0F;
				float f6 = (r * 30.0F + b * 70.0F) / 100.0F;
				r = f5;
				g = f4;
				b = f6;
			}

			tes.setColorOpaque_F(f * r, f * g, f * b);
			float f2 = 1.0F - 0.125F;
			renderer.renderFaceXPos(block, (double)((float)x - f2), (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			renderer.renderFaceXNeg(block, (double)((float)x + f2), (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			renderer.renderFaceYPos(block, (double)x, (double)((float)y - f2), (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			renderer.renderFaceYNeg(block, (double)x, (double)((float)y + f2), (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			renderer.renderFaceZPos(block, (double)x, (double)y, (double)((float)z - f2), renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			renderer.renderFaceZNeg(block, (double)x, (double)y, (double)((float)z + f2), renderer.getBlockIconFromSideAndMetadata(block, 0, meta));

			renderer.setOverrideBlockTexture(BlockFishTrap.tex[5]);
			renderer.renderCrossedSquares(block, x, y, z);
			renderer.clearOverrideBlockTexture();

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
		return true;
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
