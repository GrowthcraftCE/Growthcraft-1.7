package growthcraft.milk.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * NewProject - Undefined
 * Created using Tabula 4.1.1
 */
public class ModelCheeseVat extends ModelBase
{
	public static final float SCALE = 1.0f / 16.0f;
	public ModelRenderer base;
	public ModelRenderer side2;
	public ModelRenderer side3;
	public ModelRenderer side4;
	public ModelRenderer side1;

	public ModelCheeseVat()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.side1 = new ModelRenderer(this, 34, 2);
		this.side1.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.side1.addBox(7.0F, -15.0F, -7.0F, 1, 16, 14, 0.0F);
		this.setRotateAngle(side1, 3.141592653589793F, 1.5707963267948966F, 3.141592653589793F);
		this.side4 = new ModelRenderer(this, 34, 2);
		this.side4.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.side4.addBox(-8.0F, -15.0F, -7.0F, 1, 16, 14, 0.0F);
		this.setRotateAngle(side4, 3.141592653589793F, 1.5707963267948966F, 3.141592653589793F);
		this.side2 = new ModelRenderer(this, 0, 0);
		this.side2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.side2.addBox(-8.0F, -15.0F, -8.0F, 1, 16, 16, 0.0F);
		this.base = new ModelRenderer(this, 0, 37);
		this.base.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.base.addBox(-7.0F, 0.0F, -7.0F, 14, 1, 14, 0.0F);
		this.setRotateAngle(base, 3.141592653589793F, 1.5707963267948966F, 3.141592653589793F);
		this.side3 = new ModelRenderer(this, 0, 0);
		this.side3.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.side3.addBox(7.0F, -15.0F, -8.0F, 1, 16, 16, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.side1.render(f5);
		this.side4.render(f5);
		this.side2.render(f5);
		this.base.render(f5);
		this.side3.render(f5);
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
