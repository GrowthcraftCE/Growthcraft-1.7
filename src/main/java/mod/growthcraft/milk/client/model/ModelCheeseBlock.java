package growthcraft.milk.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCheeseBlock extends ModelBase
{
	public static final float SCALE = 1.0f / 16.0f;
	public ModelRenderer Shape2;
	public ModelRenderer Shape3;
	public ModelRenderer Shape4;
	public ModelRenderer Shape5;
	public ModelRenderer Shape1;

	public ModelCheeseBlock()
	{
		this.textureWidth = 24;
		this.textureHeight = 42;
		this.Shape5 = new ModelRenderer(this, 7, 0);
		this.Shape5.setRotationPoint(3.0F, 22.0F, -2.0F);
		this.Shape5.addBox(0.0F, -6.0F, 0.0F, 1, 8, 4, 0.0F);

		this.Shape4 = new ModelRenderer(this, 7, 0);
		this.Shape4.setRotationPoint(-6.0F, 24.0F, 1.0F);
		this.Shape4.addBox(2.0F, -8.0F, -3.0F, 1, 8, 4, 0.0F);

		this.Shape2 = new ModelRenderer(this, 0, 26);
		this.Shape2.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Shape2.addBox(-2.0F, -8.0F, -4.0F, 4, 8, 8, 0.0F);

		this.Shape1 = new ModelRenderer(this, 5, 12);
		this.Shape1.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Shape1.addBox(2.0F, -8.0F, -3.0F, 1, 8, 6, 0.0F);

		this.Shape3 = new ModelRenderer(this, 5, 12);
		this.Shape3.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Shape3.addBox(-3.0F, -8.0F, -3.0F, 1, 8, 6, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale)
	{
		this.Shape5.render(scale);
		this.Shape4.render(scale);
		this.Shape2.render(scale);
		this.Shape1.render(scale);
		this.Shape3.render(scale);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
