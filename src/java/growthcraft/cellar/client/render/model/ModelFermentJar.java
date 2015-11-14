package growthcraft.cellar.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFermentJar extends ModelBase
{
	ModelRenderer outerJar;
	ModelRenderer innerJar;
	ModelRenderer lid;

	public ModelFermentJar()
	{
		this.textureWidth  = 64;
		this.textureHeight = 64;

		this.outerJar = new ModelRenderer(this, 0, 0);
		outerJar.addBox(-4F, 0, -4F, 8, 12, 8);
		outerJar.setRotationPoint(0F, 0F, 0F);
		outerJar.setTextureSize(textureWidth, textureHeight);
		outerJar.mirror = true;
		setRotation(outerJar, 0F, 0F, 0F);

		this.innerJar = new ModelRenderer(this, 0, 0);
		innerJar.addBox(4F, 11, 4F, -8, -10, -8);
		innerJar.setRotationPoint(0F, 0F, 0F);
		innerJar.setTextureSize(textureWidth, textureHeight);
		innerJar.mirror = true;
		setRotation(innerJar, 0F, 0F, 0F);

		this.lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-4F, 12, -4F, 8, 4, 8);
		lid.setRotationPoint(0F, 0F, 0F);
		lid.setTextureSize(textureWidth, textureHeight);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
	}

	public void render(float scale)
	{
		innerJar.render(scale);
		outerJar.render(scale);
		lid.render(scale);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale)
	{
		super.render(entity, f, f1, f2, f3, f4, scale);
		render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
