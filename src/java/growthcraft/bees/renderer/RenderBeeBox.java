package growthcraft.bees.renderer;

import growthcraft.bees.block.BlockBeeBox;
import growthcraft.core.util.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class RenderBeeBox implements ISimpleBlockRenderingHandler
{
	public static final int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			final Tessellator tes = Tessellator.instance;
			final int offset = MathHelper.clamp_int(metadata, 0, 5) * 4;
			final BlockBeeBox beeBox = (BlockBeeBox)block;
			final IIcon[] icon  = {
				beeBox.getIcon(0, metadata),
				beeBox.getIcon(1, metadata),
				beeBox.getIcon(2, metadata),
				beeBox.getIcon(3, metadata),
				beeBox.getIcon(4, metadata),
				beeBox.getIcon(5, metadata)
			};
			final double d = 0.0625D;
			// LEGS
			renderer.setRenderBounds(3*d, 0.0D, 3*d, 5*d, 3*d, 5*d);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(11*d, 0.0D, 3*d, 13*d, 3*d, 5*d);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(3*d, 0.0D, 11*d, 5*d, 3*d, 13*d);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(11*d, 0.0D, 11*d, 13*d, 3*d, 13*d);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
			// BODY
			renderer.setRenderBounds(1*d, 3*d, 1*d, 15*d, 10*d, 15*d);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
			// ROOF
			renderer.setRenderBounds(0.0D, 10*d, 0.0D, 1.0D, 13*d, 1.0D);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(2*d, 13*d, 2*d, 14*d, 1.0D, 14*d);
			RenderUtils.drawInventoryBlock(block, renderer, icon, tes);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			final double d = 0.0625D;
			// LEGS
			renderer.setRenderBounds(3*d, 0.0D, 3*d, 5*d, 3*d, 5*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(11*d, 0.0D, 3*d, 13*d, 3*d, 5*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(3*d, 0.0D, 11*d, 5*d, 3*d, 13*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(11*d, 0.0D, 11*d, 13*d, 3*d, 13*d);
			renderer.renderStandardBlock(block, x, y, z);
			// BODY
			renderer.setRenderBounds(1*d, 3*d, 1*d, 15*d, 10*d, 15*d);
			renderer.renderStandardBlock(block, x, y, z);
			// ROOF
			renderer.setRenderBounds(0.0D, 10*d, 0.0D, 1.0D, 13*d, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(2*d, 13*d, 2*d, 14*d, 1.0D, 14*d);
			renderer.renderStandardBlock(block, x, y, z);
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
