package growthcraft.pipes.client.renderer;

import growthcraft.api.core.GrcColour;
import growthcraft.pipes.client.model.ModelPipe;
import growthcraft.pipes.util.PipeFlag;
import growthcraft.pipes.util.PipeType;
import growthcraft.pipes.common.block.IPipeBlock;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderPipe implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

	private void renderPipeModel(PipeType type)
	{
		if (type == PipeType.VACUUM)
		{
			ModelPipe.INSTANCE.render(PipeFlag.PIPE_VACUUM_CORE | PipeFlag.PIPE_BUSES, 0.0625f);
		}
		else
		{
			ModelPipe.INSTANCE.render(PipeFlag.PIPE_CORE | PipeFlag.PIPES, 0.0625f);
		}
	}

	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return;
		if (block instanceof IPipeBlock)
		{
			final IPipeBlock pipeBlock = (IPipeBlock)block;
			GL11.glPushMatrix();
			{
				final GrcColour colour = GrcColour.toColour(metadata);
				final Tessellator tessellator = Tessellator.instance;
				Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityPipeRenderer.res);
				if (colour != GrcColour.Transparent)
				{
					final int c = colour.blackVariant;
					final float r = ((c >> 16) & 0xFF) / 255.0f;
					final float g = ((c >> 8) & 0xFF) / 255.0f;
					final float b = (c & 0xFF) / 255.0f;
					GL11.glColor4f(r, g, b, 1.0f);
				}
				renderPipeModel(pipeBlock.getPipeType());
				Minecraft.getMinecraft().renderEngine.bindTexture(TileEntityPipeRenderer.resColorMask);
				if (colour != GrcColour.Transparent)
				{
					final int c = colour.mediumVariant;
					final float r = ((c >> 16) & 0xFF) / 255.0f;
					final float g = ((c >> 8) & 0xFF) / 255.0f;
					final float b = (c & 0xFF) / 255.0f;
					GL11.glColor4f(r, g, b, 1.0f);
				}
				renderPipeModel(pipeBlock.getPipeType());
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
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
