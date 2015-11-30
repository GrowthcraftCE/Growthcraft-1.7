package growthcraft.pipes.client.render;

import growthcraft.api.core.GrcColour;
import growthcraft.core.utils.RenderUtils;
import growthcraft.pipes.client.model.ModelPipe;
import growthcraft.pipes.tileentity.TileEntityPipeBase;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityPipeRenderer extends TileEntitySpecialRenderer
{
	public static final ResourceLocation res = new ResourceLocation("grcpipes" , "textures/blocks/model_pipe.png");
	public static final ResourceLocation resColorMask = new ResourceLocation("grcpipes" , "textures/blocks/model_pipe_color_mask.png");
	private static final float modelScale = 0.0625F;

	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick)
	{
		final TileEntityPipeBase pipeBase = (TileEntityPipeBase)te;
		final GrcColour colour = pipeBase.getColour();
		final int renderState = pipeBase.getPipeRenderState();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);

			bindTexture(res);
			ModelPipe.INSTANCE.render(renderState, modelScale);

			if (colour != GrcColour.Transparent)
			{
				bindTexture(resColorMask);
				GL11.glScalef(1.01F, 1.01F, 1.01F);
				final int c = colour.mediumVariant;
				final float r = ((c >> 16) & 0xFF) / 255.0f;
				final float g = ((c >> 8) & 0xFF) / 255.0f;
				final float b = (c & 0xFF) / 255.0f;
				GL11.glColor4f(r, g, b, 1.0f);
				ModelPipe.INSTANCE.render(renderState, modelScale);
			}
		}
		GL11.glPopMatrix();
	}
}
