package growthcraft.cellar.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelCultureJar - Undefined
 * Created using Tabula 4.1.1
 */
public class ModelCultureJar extends ModelBase
{
	public static final float SCALE = 1f / 16f;
	public ModelRenderer Side4;
	public ModelRenderer Neck;
	public ModelRenderer Side1;
	public ModelRenderer Side2;
	public ModelRenderer Side3;
	public ModelRenderer Base;

	public ModelCultureJar()
	{
		this.textureWidth = 32;
		this.textureHeight = 32;
		this.Side4 = new ModelRenderer(this, 0, 10);
		this.Side4.setRotationPoint(3.0F, 11.0F, -1.0F);
		this.Side4.addBox(-2.0F, 8.0F, 0.0F, 1, 4, 2, 0.0F);
		this.Neck = new ModelRenderer(this, 0, 17);
		this.Neck.setRotationPoint(0.0F, 15.0F, -2.0F);
		this.Neck.addBox(-2.0F, 8.0F, 0.0F, 4, 1, 4, 0.0F);
		this.Side2 = new ModelRenderer(this, 0, 4);
		this.Side2.setRotationPoint(0.0F, 11.0F, 1.0F);
		this.Side2.addBox(-2.0F, 8.0F, 0.0F, 4, 4, 1, 0.0F);
		this.Side1 = new ModelRenderer(this, 0, 4);
		this.Side1.setRotationPoint(0.0F, 11.0F, -2.0F);
		this.Side1.addBox(-2.0F, 8.0F, 0.0F, 4, 4, 1, 0.0F);
		this.Base = new ModelRenderer(this, 0, 0);
		this.Base.setRotationPoint(1.0F, 10.0F, -1.0F);
		this.Base.addBox(-2.0F, 8.0F, 0.0F, 2, 1, 2, 0.0F);
		this.Side3 = new ModelRenderer(this, 0, 10);
		this.Side3.setRotationPoint(0.0F, 11.0F, -1.0F);
		this.Side3.addBox(-2.0F, 8.0F, 0.0F, 1, 4, 2, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale)
	{
		this.Side4.render(scale);
		this.Neck.render(scale);
		this.Side2.render(scale);
		this.Side1.render(scale);
		this.Base.render(scale);
		this.Side3.render(scale);
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
