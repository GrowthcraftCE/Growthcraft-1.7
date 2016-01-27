package growthcraft.cellar.client.renderer;

import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.client.model.ModelCultureJar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityCultureJarRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5f, (float)z + 0.5F);
			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			this.bindTexture(GrcCellarResources.INSTANCE.textureCultureJar);
			GrcCellarResources.INSTANCE.modelCultureJar.render(null, 0f, 0f, 0f, f, 0f, ModelCultureJar.SCALE);
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}
}
