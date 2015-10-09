package growthcraft.pipes.client.render;

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

	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick)
	{
		final TileEntityPipeBase pipeBase = (TileEntityPipeBase)te;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		//GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		bindTexture(res);
		ModelPipe.INSTANCE.render(pipeBase.getPipeRenderState(), 0.0625F);
		GL11.glPopMatrix();
	}
}
