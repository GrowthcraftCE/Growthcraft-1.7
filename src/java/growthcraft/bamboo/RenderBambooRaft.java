package growthcraft.bamboo;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderBambooRaft extends Render
{
	private static final ResourceLocation res = new ResourceLocation("grcbamboo", "textures/entity/raft.png");
	protected ModelBase model;

	public RenderBambooRaft()
	{
		this.shadowSize = 0.5F;
		this.model = new ModelBambooRaft();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float f1)
	{
		this.renderBambooRaft((EntityBambooRaft)entity, x, y, z, f, f1);
	}

	public void renderBambooRaft(EntityBambooRaft entity, double x, double y, double z, float par8, float par9)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		float f2 = (float)entity.getTimeSinceHit() - par9;
		float f3 = entity.getDamageTaken() - par9;

		if (f3 < 0.0F)
		{
			f3 = 0.0F;
		}

		if (f2 > 0.0F)
		{
			GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float)entity.getForwardDirection(), 1.0F, 0.0F, 0.0F);
		}

		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		this.bindEntityTexture(entity);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return res;
	}

}
