package growthcraft.bees;

import growthcraft.core.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderBeeHive implements ISimpleBlockRenderingHandler 
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
		if (modelID == id)
		{
			Tessellator tes = Tessellator.instance;
			IIcon[] icon  = {BlockBeeHive.tex[1], BlockBeeHive.tex[1], BlockBeeHive.tex[1], BlockBeeHive.tex[1], BlockBeeHive.tex[1], BlockBeeHive.tex[0] };
			double d = 0.0625D;

			renderer.setRenderBounds(4*d, 0.0D, 4*d, 12*d, 14*d, 12*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(3*d, 1*d, 3*d, 13*d, 13*d, 13*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(2*d, 4*d, 2*d, 14*d, 10*d, 14*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.setRenderBounds(7*d, 14*d, 7*d, 9*d, 1.0D, 9*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		if (modelId == id)
		{
			double d = 0.0625D;
			renderer.setRenderBounds(4*d, 0.0D, 4*d, 12*d, 14*d, 12*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(3*d, 1*d, 3*d, 13*d, 13*d, 13*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(2*d, 4*d, 2*d, 14*d, 10*d, 14*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(7*d, 14*d, 7*d, 9*d, 1.0D, 9*d);
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
