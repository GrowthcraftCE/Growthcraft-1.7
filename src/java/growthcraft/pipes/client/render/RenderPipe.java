package growthcraft.pipes.client.render;

import growthcraft.pipes.client.model.ModelPipe;
import growthcraft.pipes.utils.PipeFlag;
import growthcraft.pipes.utils.PipeType;
import growthcraft.pipes.block.IPipeBlock;
import growthcraft.core.utils.RenderUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderPipe implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return;
		if (block instanceof IPipeBlock)
		{
			IPipeBlock pipeBlock = (IPipeBlock)block;
			GL11.glPushMatrix();
			//GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			//GL11.glRotatef(90F, 0.0F, -1.0F, 0.0F);
			//GL11.glTranslated(0.0F, -1.0F, 0.0F);
			Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityPipeRenderer.res);
			if (pipeBlock.getPipeType() == PipeType.VACUUM)
			{
				ModelPipe.INSTANCE.render(PipeFlag.PIPE_VACUUM_CORE | PipeFlag.PIPE_BUSES, 0.0625f);
			}
			else
			{
				ModelPipe.INSTANCE.render(PipeFlag.PIPE_CORE | PipeFlag.PIPES, 0.0625f);
			}

			GL11.glPopMatrix();
		}
	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return true;
		return false;
	}

	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	public int getRenderId()
	{
		return RENDER_ID;
	}
}
