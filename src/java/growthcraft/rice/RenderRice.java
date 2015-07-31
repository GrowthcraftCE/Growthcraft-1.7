package growthcraft.rice;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderRice implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			int meta = world.getBlockMetadata(x, y, z);
			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

			IIcon tex[] = new IIcon[5];
			for (int loop = 0; loop < 5; loop++)
			{
				tex[loop] = BlockRice.tex[loop];
			}

			switch (meta)
			{
			case 0: case 1: renderer.overrideBlockTexture = tex[0]; break;
			case 2: case 3: renderer.overrideBlockTexture = tex[1]; break;
			case 4: case 5: case 7: renderer.overrideBlockTexture = tex[2]; break;
			case 6: renderer.overrideBlockTexture = tex[3]; break;
			}
			this.renderSquareRice(block, x, y, z, world, renderer);
			renderer.clearOverrideBlockTexture();

			if (meta == 7)
			{
				renderer.overrideBlockTexture = tex[4];
				this.renderCrossedRice(block, x, y, z, world, renderer);
				renderer.clearOverrideBlockTexture();
			}
		}
		return true;
	}

	private void renderSquareRice(Block block, int x, int y, int z, IBlockAccess world, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderer.renderBlockCropsImpl(block, world.getBlockMetadata(x, y, z), (double)x, (double)((float)y - 0.125F), (double)z);
	}

	private void renderCrossedRice(Block block, int x, int y, int z, IBlockAccess world, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		float f = 1.0F;
		int l = block.colorMultiplier(world, x, y, z);
		float f1 = (float)(l >> 16 & 255) / 255.0F;
		float f2 = (float)(l >> 8 & 255) / 255.0F;
		float f3 = (float)(l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable)
		{
			float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		double d0 = (double)x;
		double d1 = (double)y - 0.125D;
		double d2 = (double)z;

		renderer.drawCrossedSquares(renderer.getBlockIconFromSideAndMetadata(block, 0, world.getBlockMetadata(x, y, z)), d0, d1, d2, 1.0F);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) { return false; }

	@Override
	public int getRenderId()
	{
		return id;
	}

}
