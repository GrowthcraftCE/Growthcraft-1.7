package growthcraft.milk.client.model;

import growthcraft.api.core.util.GrcColor;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * HangingCheese - Undefined
 * Created using Tabula 4.1.1
 */
public class ModelHangingCurds extends ModelBase
{
	public static final float SCALE = 1.0f / 16.0f;
	public ModelRenderer stick;
	public ModelRenderer curdPart1;
	public ModelRenderer curdPart2;
	public ModelRenderer curdPart3;
	public ModelRenderer curdPart4;
	public ModelRenderer curdPart5;
	public ModelRenderer curdPart6;
	public ModelRenderer curdPart7;
	public ModelRenderer curdPart8;
	public ModelRenderer curdPart9;
	public ModelRenderer curdPart10;
	public ModelRenderer curdPart11;
	public ModelRenderer curdPart12;
	private GrcColor curdColor = GrcColor.newWhite();

	public ModelHangingCurds()
	{
		this.textureWidth = 32;
		this.textureHeight = 64;

		this.stick = new ModelRenderer(this, 10, 13);
		this.stick.setRotationPoint(-1.0F, 8.0F, -1.0F);
		this.stick.addBox(0.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F);
		this.curdPart9 = new ModelRenderer(this, 9, 28);
		this.curdPart9.setRotationPoint(-2.0F, 21.0F, -3.0F);
		this.curdPart9.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
		this.curdPart8 = new ModelRenderer(this, 9, 35);
		this.curdPart8.setRotationPoint(-2.0F, 15.0F, 2.0F);
		this.curdPart8.addBox(0.0F, 0.0F, 0.0F, 4, 2, 1, 0.0F);
		this.curdPart6 = new ModelRenderer(this, 11, 23);
		this.curdPart6.setRotationPoint(-1.0F, 14.0F, 1.0F);
		this.curdPart6.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
		this.curdPart3 = new ModelRenderer(this, 0, 54);
		this.curdPart3.setRotationPoint(-5.0F, 17.0F, -3.0F);
		this.curdPart3.addBox(1.0F, 0.0F, 0.0F, 8, 4, 6, 0.0F);
		this.curdPart5 = new ModelRenderer(this, 11, 21);
		this.curdPart5.setRotationPoint(-1.0F, 14.0F, -2.0F);
		this.curdPart5.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
		this.curdPart4 = new ModelRenderer(this, 4, 38);
		this.curdPart4.setRotationPoint(-3.0F, 15.0F, -2.0F);
		this.curdPart4.addBox(0.0F, 0.0F, 0.0F, 6, 2, 4, 0.0F);
		this.curdPart7 = new ModelRenderer(this, 9, 32);
		this.curdPart7.setRotationPoint(-2.0F, 15.0F, -3.0F);
		this.curdPart7.addBox(0.0F, 0.0F, 0.0F, 4, 2, 1, 0.0F);
		this.curdPart2 = new ModelRenderer(this, 4, 44);
		this.curdPart2.setRotationPoint(-3.0F, 21.0F, -2.0F);
		this.curdPart2.addBox(0.0F, 0.0F, 0.0F, 6, 1, 4, 0.0F);
		this.curdPart1 = new ModelRenderer(this, 8, 25);
		this.curdPart1.setRotationPoint(-2.0F, 14.0F, -1.0F);
		this.curdPart1.addBox(0.0F, 0.0F, 0.0F, 4, 1, 2, 0.0F);
		this.curdPart12 = new ModelRenderer(this, 7, 49);
		this.curdPart12.setRotationPoint(-4.0F, 17.0F, 3.0F);
		this.curdPart12.addBox(1.0F, 0.0F, 0.0F, 6, 4, 1, 0.0F);
		this.curdPart11 = new ModelRenderer(this, 7, 49);
		this.curdPart11.setRotationPoint(-4.0F, 17.0F, -4.0F);
		this.curdPart11.addBox(1.0F, 0.0F, 0.0F, 6, 4, 1, 0.0F);
		this.curdPart10 = new ModelRenderer(this, 9, 30);
		this.curdPart10.setRotationPoint(-2.0F, 21.0F, 2.0F);
		this.curdPart10.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
	}

	public ModelHangingCurds setCurdColor(int color)
	{
		curdColor.setHexRGB(color);
		return this;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale)
	{
		this.stick.render(scale);
		GL11.glPushMatrix();
		{
			GL11.glColor4f(curdColor.r, curdColor.g, curdColor.b, curdColor.a);
			this.curdPart1.render(scale);
			this.curdPart2.render(scale);
			this.curdPart3.render(scale);
			this.curdPart4.render(scale);
			this.curdPart5.render(scale);
			this.curdPart6.render(scale);
			this.curdPart7.render(scale);
			this.curdPart8.render(scale);
			this.curdPart9.render(scale);
			this.curdPart10.render(scale);
			this.curdPart11.render(scale);
			this.curdPart12.render(scale);
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
