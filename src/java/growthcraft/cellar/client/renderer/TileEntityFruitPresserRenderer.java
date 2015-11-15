package growthcraft.cellar.client.renderer;

import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.client.resource.GrcCellarResources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityFruitPresserRenderer extends TileEntitySpecialRenderer
{
	private void renderTileEntityFruitPresserAt(TileEntityFruitPresser te, double x, double y, double z, float f)
	{
		GL11.glPushMatrix();
		{
			final float f2 = te.getTranslation();
			GL11.glTranslatef((float)x + 0.5F, (float)y + 1.75F - f2, (float)z + 0.5F);
			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(0.9F, 1.0F, 0.9F);
			this.bindTexture(GrcCellarResources.INSTANCE.textureFruitPresser);
			GrcCellarResources.INSTANCE.modelFruitPresser.render((Entity)null, 0.0F, 0.0F, 0.0F, f, 0.0F, 0.0625F);
		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
	{
		this.renderTileEntityFruitPresserAt((TileEntityFruitPresser)te, x, y, z, f);
	}
}
