package growthcraft.cellar.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelFermentJar extends ModelBase
{
	public static final float SCALE = 1f / 32f;

	ModelRenderer outerJar;
	ModelRenderer innerJar;
	ModelRenderer lid;

	public ModelFermentJar()
	{
		this.textureWidth  = 256;
		this.textureHeight = 256;

		this.outerJar = new ModelRenderer(this, 0, 0);
		outerJar.addBox(-12F, 0, -12F, 24, 24, 24);
		outerJar.setRotationPoint(0F, 0F, 0F);
		outerJar.setTextureSize(textureWidth, textureHeight);
		outerJar.mirror = true;
		setRotation(outerJar, 0F, 0F, 0F);

		this.innerJar = new ModelRenderer(this, 128, 0);
		innerJar.addBox(11F, 23, 11F, -22, -22, -22);
		innerJar.setRotationPoint(0F, 0F, 0F);
		innerJar.setTextureSize(textureWidth, textureHeight);
		innerJar.mirror = true;
		setRotation(innerJar, 0F, 0F, 0F);

		this.lid = new ModelRenderer(this, 0, 128);
		lid.addBox(-8F, 24, -8F, 16, 8, 16);
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

	public void renderForInventory(float scale)
	{
		render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
