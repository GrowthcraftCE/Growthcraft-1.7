package growthcraft.cellar.client.render;

import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.block.BlockFermentJar;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderFermentJar implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

	public int getRenderId()
	{
		return RENDER_ID;
	}

	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	private void renderFermentJarModel()
	{
		GrcCellarResources.INSTANCE.modelFermentJar.render(0.0625f);
	}

	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return;
		if (block instanceof BlockFermentJar)
		{
			final BlockFermentJar fermentJar = (BlockFermentJar)block;
			GL11.glPushMatrix();
			{
				Minecraft.getMinecraft().renderEngine.bindTexture(GrcCellarResources.INSTANCE.textureFermentJar);
				renderFermentJarModel();
			}
			GL11.glPopMatrix();
		}
	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return false;
		return true;
	}
}
