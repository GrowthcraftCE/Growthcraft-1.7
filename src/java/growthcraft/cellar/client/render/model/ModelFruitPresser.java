package growthcraft.cellar.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFruitPresser extends ModelBase
{
	ModelRenderer head;
	ModelRenderer rod;

	public ModelFruitPresser()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.head = new ModelRenderer(this, 0, 0);
		head.addBox(-5F, -1.5F, -5F, 10, 3, 10);
		head.setRotationPoint(0F, 22.5F, 0F);
		head.setTextureSize(64, 32);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		this.rod = new ModelRenderer(this, 0, 15);
		rod.addBox(-1.5F, -8F, -1.5F, 3, 7, 3);
		rod.setRotationPoint(0F, 22F, 0F);
		rod.setTextureSize(64, 32);
		rod.mirror = true;
		setRotation(rod, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		head.render(f5);
		rod.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
