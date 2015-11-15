package growthcraft.cellar.client.renderer;

import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.client.render.model.ModelFermentJar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityFermentJarRenderer extends TileEntitySpecialRenderer
{
	private void renderTileEntityFermentJarAt(TileEntityFermentJar te, double x, double y, double z, float f)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		{
			//final float f2 = te.getTranslation();
			GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
			//GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			//GL11.glScalef(0.9F, 1.0F, 0.9F);
			this.bindTexture(GrcCellarResources.INSTANCE.textureFermentJar);
			GrcCellarResources.INSTANCE.modelFermentJar.render((Entity)null, 0.0F, 0.0F, 0.0F, f, 0.0F, ModelFermentJar.SCALE);
		}
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
	{
		this.renderTileEntityFermentJarAt((TileEntityFermentJar)te, x, y, z, f);
	}
}
