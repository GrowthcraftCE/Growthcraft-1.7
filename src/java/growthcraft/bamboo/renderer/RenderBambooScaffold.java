package growthcraft.bamboo.renderer;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.block.BlockBambooScaffold;
import growthcraft.core.utils.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBambooScaffold implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			Tessellator tes = Tessellator.instance;
			IIcon icon = BlockBambooScaffold.tex[0];
			IIcon icon1 = BlockBambooScaffold.tex[1];
			double d = 0.0625D;
			renderer.setRenderBounds(0.0D, 14*d, 0.0D, 1.0D, 1.0D, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon, tes);
			// columns
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 2*d, 14*d, 2*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.setRenderBounds(14*d, 0.0D, 0.0D, 1.0D, 14*d, 2*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.setRenderBounds(14*d, 0.0D, 14*d, 1.0D, 14*d, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.setRenderBounds(0.0D, 0.0D, 14*d, 2*d, 14*d, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			// beams
			renderer.setRenderBounds(2*d, 6*d, 0.0D, 14*d, 8*d, 2*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.setRenderBounds(2*d, 6*d, 14*d, 14*d, 8*d, 1.0D);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.setRenderBounds(0.0D, 6*d, 2*d, 2*d, 8*d, 14*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.setRenderBounds(14*d, 6*d, 2*d, 1.0D, 8*d, 14*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, icon1, tes);
			renderer.clearOverrideBlockTexture();
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			double d = 0.0625D;
			renderer.setOverrideBlockTexture(BlockBambooScaffold.tex[0]);
			renderer.setRenderBounds(0.0D, 14*d, 0.0D, 1.0D, 1.0D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setOverrideBlockTexture(BlockBambooScaffold.tex[1]);
			// columns
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 2*d, 14*d, 2*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(14*d, 0.0D, 0.0D, 1.0D, 14*d, 2*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(14*d, 0.0D, 14*d, 1.0D, 14*d, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.0D, 14*d, 2*d, 14*d, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			// beams
			renderer.setRenderBounds(2*d, 6*d, 0.0D, 14*d, 8*d, 2*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(2*d, 6*d, 14*d, 14*d, 8*d, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 6*d, 2*d, 2*d, 8*d, 14*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(14*d, 6*d, 2*d, 1.0D, 8*d, 14*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.clearOverrideBlockTexture();
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
