package growthcraft.milk.client.model;

import growthcraft.api.core.util.Easing;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Butter Churn - PitchBright
 * Created using Tabula 4.1.1
 */
public class ModelButterChurn extends ModelBase
{
	public static final float SCALE = 1.0f / 16.0f;
	public ModelRenderer shape1;
	public ModelRenderer shape1_1;
	public ModelRenderer shape1_2;
	public ModelRenderer shape1_3;
	public ModelRenderer shape1_4;
	public ModelRenderer shape1_5;
	public ModelRenderer shape1_6;
	public ModelRenderer shape1_7;
	public ModelRenderer shape17;
	public ModelRenderer shape17_1;
	public ModelRenderer shape17_2;
	public ModelRenderer shape17_3;
	public ModelRenderer shape17_4;
	public ModelRenderer shape17_5;
	public ModelRenderer plunger;

	public ModelButterChurn()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.shape17_4 = new ModelRenderer(this, 18, 17);
		this.shape17_4.setRotationPoint(-2.0F, 15.0F, -3.0F);
		this.shape17_4.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
		this.shape1_4 = new ModelRenderer(this, 34, 0);
		this.shape1_4.setRotationPoint(-3.0F, 15.0F, 2.0F);
		this.shape1_4.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1, 0.0F);
		this.shape1 = new ModelRenderer(this, 34, 16);
		this.shape1.setRotationPoint(-2.0F, 15.0F, -4.0F);
		this.shape1.addBox(0.0F, 0.0F, 0.0F, 4, 9, 1, 0.0F);
		this.shape17_3 = new ModelRenderer(this, 18, 27);
		this.shape17_3.setRotationPoint(-3.0F, 15.0F, -2.0F);
		this.shape17_3.addBox(0.0F, 0.0F, 0.0F, 6, 1, 4, 0.0F);
		this.shape17_2 = new ModelRenderer(this, 18, 17);
		this.shape17_2.setRotationPoint(-2.0F, 23.0F, 2.0F);
		this.shape17_2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
		this.shape17 = new ModelRenderer(this, 18, 27);
		this.shape17.setRotationPoint(-3.0F, 23.0F, -2.0F);
		this.shape17.addBox(0.0F, 0.0F, 0.0F, 6, 1, 4, 0.0F);
		this.shape1_2 = new ModelRenderer(this, 45, 13);
		this.shape1_2.setRotationPoint(-4.0F, 15.0F, -2.0F);
		this.shape1_2.addBox(0.0F, 0.0F, 0.0F, 1, 9, 4, 0.0F);
		this.shape1_1 = new ModelRenderer(this, 34, 16);
		this.shape1_1.setRotationPoint(-2.0F, 15.0F, 3.0F);
		this.shape1_1.addBox(0.0F, 0.0F, 0.0F, 4, 9, 1, 0.0F);
		this.shape1_5 = new ModelRenderer(this, 34, 0);
		this.shape1_5.setRotationPoint(2.0F, 15.0F, 2.0F);
		this.shape1_5.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1, 0.0F);
		this.shape1_6 = new ModelRenderer(this, 34, 0);
		this.shape1_6.setRotationPoint(2.0F, 15.0F, -3.0F);
		this.shape1_6.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1, 0.0F);
		this.shape17_1 = new ModelRenderer(this, 18, 17);
		this.shape17_1.setRotationPoint(-2.0F, 23.0F, -3.0F);
		this.shape17_1.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
		this.shape1_3 = new ModelRenderer(this, 45, 13);
		this.shape1_3.setRotationPoint(3.0F, 15.0F, -2.0F);
		this.shape1_3.addBox(0.0F, 0.0F, 0.0F, 1, 9, 4, 0.0F);
		this.shape1_7 = new ModelRenderer(this, 34, 0);
		this.shape1_7.setRotationPoint(-3.0F, 15.0F, -3.0F);
		this.shape1_7.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1, 0.0F);
		this.plunger = new ModelRenderer(this, 0, 19);
		this.plunger.setRotationPoint(-1.0F, 8.0F, -1.0F);
		this.plunger.addBox(0.0F, 0.0F, 0.0F, 2, 11, 2, 0.0F);
		this.shape17_5 = new ModelRenderer(this, 18, 17);
		this.shape17_5.setRotationPoint(-2.0F, 15.0F, 2.0F);
		this.shape17_5.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
	}

	@Override
	public void render(Entity entity, float progress, float progressDir, float f2, float f3, float f4, float scale)
	{
		final float eased = (float)((progressDir < 0) ? Easing.d.cubicIn : Easing.d.cubicOut).call(progress);

		this.shape17_4.render(scale);
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1_4.offsetX, this.shape1_4.offsetY, this.shape1_4.offsetZ);
			GL11.glTranslatef(this.shape1_4.rotationPointX * scale, this.shape1_4.rotationPointY * scale, this.shape1_4.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1_4.offsetX, -this.shape1_4.offsetY, -this.shape1_4.offsetZ);
			GL11.glTranslatef(-this.shape1_4.rotationPointX * scale, -this.shape1_4.rotationPointY * scale, -this.shape1_4.rotationPointZ * scale);
			this.shape1_4.render(scale);
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1.offsetX, this.shape1.offsetY, this.shape1.offsetZ);
			GL11.glTranslatef(this.shape1.rotationPointX * scale, this.shape1.rotationPointY * scale, this.shape1.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1.offsetX, -this.shape1.offsetY, -this.shape1.offsetZ);
			GL11.glTranslatef(-this.shape1.rotationPointX * scale, -this.shape1.rotationPointY * scale, -this.shape1.rotationPointZ * scale);
			this.shape1.render(scale);
		}
		GL11.glPopMatrix();

		this.shape17_3.render(scale);
		this.shape17_2.render(scale);
		this.shape17.render(scale);

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1_2.offsetX, this.shape1_2.offsetY, this.shape1_2.offsetZ);
			GL11.glTranslatef(this.shape1_2.rotationPointX * scale, this.shape1_2.rotationPointY * scale, this.shape1_2.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1_2.offsetX, -this.shape1_2.offsetY, -this.shape1_2.offsetZ);
			GL11.glTranslatef(-this.shape1_2.rotationPointX * scale, -this.shape1_2.rotationPointY * scale, -this.shape1_2.rotationPointZ * scale);
			this.shape1_2.render(scale);
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1_1.offsetX, this.shape1_1.offsetY, this.shape1_1.offsetZ);
			GL11.glTranslatef(this.shape1_1.rotationPointX * scale, this.shape1_1.rotationPointY * scale, this.shape1_1.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1_1.offsetX, -this.shape1_1.offsetY, -this.shape1_1.offsetZ);
			GL11.glTranslatef(-this.shape1_1.rotationPointX * scale, -this.shape1_1.rotationPointY * scale, -this.shape1_1.rotationPointZ * scale);
			this.shape1_1.render(scale);
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1_5.offsetX, this.shape1_5.offsetY, this.shape1_5.offsetZ);
			GL11.glTranslatef(this.shape1_5.rotationPointX * scale, this.shape1_5.rotationPointY * scale, this.shape1_5.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1_5.offsetX, -this.shape1_5.offsetY, -this.shape1_5.offsetZ);
			GL11.glTranslatef(-this.shape1_5.rotationPointX * scale, -this.shape1_5.rotationPointY * scale, -this.shape1_5.rotationPointZ * scale);
			this.shape1_5.render(scale);
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1_6.offsetX, this.shape1_6.offsetY, this.shape1_6.offsetZ);
			GL11.glTranslatef(this.shape1_6.rotationPointX * scale, this.shape1_6.rotationPointY * scale, this.shape1_6.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1_6.offsetX, -this.shape1_6.offsetY, -this.shape1_6.offsetZ);
			GL11.glTranslatef(-this.shape1_6.rotationPointX * scale, -this.shape1_6.rotationPointY * scale, -this.shape1_6.rotationPointZ * scale);
			this.shape1_6.render(scale);
		}
		GL11.glPopMatrix();

		this.shape17_1.render(scale);
		this.shape1_3.render(scale);

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(this.shape1_7.offsetX, this.shape1_7.offsetY, this.shape1_7.offsetZ);
			GL11.glTranslatef(this.shape1_7.rotationPointX * scale, this.shape1_7.rotationPointY * scale, this.shape1_7.rotationPointZ * scale);
			GL11.glScaled(1.0D, 0.99D, 1.0D);
			GL11.glTranslatef(-this.shape1_7.offsetX, -this.shape1_7.offsetY, -this.shape1_7.offsetZ);
			GL11.glTranslatef(-this.shape1_7.rotationPointX * scale, -this.shape1_7.rotationPointY * scale, -this.shape1_7.rotationPointZ * scale);
			this.shape1_7.render(scale);
		}
		GL11.glPopMatrix();

		this.shape17_5.render(scale);

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(0.0f, eased * scale * 4, 0.0f);
			this.plunger.render(scale);
		}
		GL11.glPopMatrix();
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
