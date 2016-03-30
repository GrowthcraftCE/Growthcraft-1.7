package growthcraft.milk.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * pancheon - TechneToTabulaImporter
 * Created using Tabula 4.1.1
 */
public class ModelPancheon extends ModelBase
{
	public static final float SCALE = 1.0f / 16.0f;
	public ModelRenderer side2;
	public ModelRenderer base;
	public ModelRenderer side3;
	public ModelRenderer side1;
	public ModelRenderer side4;

	public ModelPancheon()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.side1 = new ModelRenderer(this, 0, 54);
		this.side1.setRotationPoint(1.0F, 23.0F, 0.0F);
		this.side1.addBox(-8.0F, -4.0F, -8.0F, 14, 4, 1, 0.0F);
		this.side3 = new ModelRenderer(this, 30, 30);
		this.side3.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.side3.addBox(7.0F, -4.0F, -8.0F, 1, 4, 16, 0.0F);
		this.side2 = new ModelRenderer(this, 0, 54);
		this.side2.setRotationPoint(1.0F, 23.0F, -1.0F);
		this.side2.addBox(-8.0F, -4.0F, 8.0F, 14, 4, 1, 0.0F);
		this.base = new ModelRenderer(this, 3, 2);
		this.base.setRotationPoint(1.0F, 23.0F, 1.0F);
		this.base.addBox(-8.0F, 0.0F, -8.0F, 14, 1, 14, 0.0F);
		this.side4 = new ModelRenderer(this, 30, 30);
		this.side4.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.side4.addBox(-8.0F, -4.0F, -8.0F, 1, 4, 16, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale)
	{
		this.side1.render(scale);
		this.side3.render(scale);
		this.side2.render(scale);
		this.base.render(scale);
		this.side4.render(scale);
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
