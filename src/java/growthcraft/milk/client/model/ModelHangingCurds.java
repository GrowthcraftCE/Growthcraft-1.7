package growthcraft.milk.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * HangingCheese - Undefined
 * Created using Tabula 4.1.1
 */
public class ModelHangingCurds extends ModelBase
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
	public ModelRenderer shape1_8;
	public ModelRenderer shape1_9;
	public ModelRenderer shape1_10;
	public ModelRenderer shape1_11;
	public ModelRenderer shape1_12;

	public ModelHangingCurds()
	{
		this.textureWidth = 32;
		this.textureHeight = 64;
		this.shape1_9 = new ModelRenderer(this, 9, 28);
		this.shape1_9.setRotationPoint(-2.0F, 21.0F, -3.0F);
		this.shape1_9.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
		this.shape1_8 = new ModelRenderer(this, 9, 35);
		this.shape1_8.setRotationPoint(-2.0F, 15.0F, 2.0F);
		this.shape1_8.addBox(0.0F, 0.0F, 0.0F, 4, 2, 1, 0.0F);
		this.shape1_6 = new ModelRenderer(this, 11, 23);
		this.shape1_6.setRotationPoint(-1.0F, 14.0F, 1.0F);
		this.shape1_6.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
		this.shape1_3 = new ModelRenderer(this, 0, 54);
		this.shape1_3.setRotationPoint(-5.0F, 17.0F, -3.0F);
		this.shape1_3.addBox(1.0F, 0.0F, 0.0F, 8, 4, 6, 0.0F);
		this.shape1_5 = new ModelRenderer(this, 11, 21);
		this.shape1_5.setRotationPoint(-1.0F, 14.0F, -2.0F);
		this.shape1_5.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
		this.shape1_4 = new ModelRenderer(this, 4, 38);
		this.shape1_4.setRotationPoint(-3.0F, 15.0F, -2.0F);
		this.shape1_4.addBox(0.0F, 0.0F, 0.0F, 6, 2, 4, 0.0F);
		this.shape1_7 = new ModelRenderer(this, 9, 32);
		this.shape1_7.setRotationPoint(-2.0F, 15.0F, -3.0F);
		this.shape1_7.addBox(0.0F, 0.0F, 0.0F, 4, 2, 1, 0.0F);
		this.shape1_2 = new ModelRenderer(this, 4, 44);
		this.shape1_2.setRotationPoint(-3.0F, 21.0F, -2.0F);
		this.shape1_2.addBox(0.0F, 0.0F, 0.0F, 6, 1, 4, 0.0F);
		this.shape1_1 = new ModelRenderer(this, 8, 25);
		this.shape1_1.setRotationPoint(-2.0F, 14.0F, -1.0F);
		this.shape1_1.addBox(0.0F, 0.0F, 0.0F, 4, 1, 2, 0.0F);
		this.shape1_12 = new ModelRenderer(this, 7, 49);
		this.shape1_12.setRotationPoint(-4.0F, 17.0F, 3.0F);
		this.shape1_12.addBox(1.0F, 0.0F, 0.0F, 6, 4, 1, 0.0F);
		this.shape1 = new ModelRenderer(this, 10, 13);
		this.shape1.setRotationPoint(-1.0F, 8.0F, -1.0F);
		this.shape1.addBox(0.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F);
		this.shape1_11 = new ModelRenderer(this, 7, 49);
		this.shape1_11.setRotationPoint(-4.0F, 17.0F, -4.0F);
		this.shape1_11.addBox(1.0F, 0.0F, 0.0F, 6, 4, 1, 0.0F);
		this.shape1_10 = new ModelRenderer(this, 9, 30);
		this.shape1_10.setRotationPoint(-2.0F, 21.0F, 2.0F);
		this.shape1_10.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.shape1_9.render(f5);
		this.shape1_8.render(f5);
		this.shape1_6.render(f5);
		this.shape1_3.render(f5);
		this.shape1_5.render(f5);
		this.shape1_4.render(f5);
		this.shape1_7.render(f5);
		this.shape1_2.render(f5);
		this.shape1_1.render(f5);
		this.shape1_12.render(f5);
		this.shape1.render(f5);
		this.shape1_11.render(f5);
		this.shape1_10.render(f5);
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
