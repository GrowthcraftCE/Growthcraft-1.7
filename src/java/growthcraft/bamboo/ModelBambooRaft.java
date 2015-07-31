package growthcraft.bamboo;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBambooRaft extends ModelBase
{
	public ModelRenderer box;

	public ModelBambooRaft()
	{
		this.box = new ModelRenderer(this, 0, 8);
		byte b0 = 24;
		byte b1 = 6;
		byte b2 = 20;
		byte b3 = 4;
		this.box.addBox((float)(-b0 / 2), (float)(-b2 / 2 + 2), -3.0F, b0, b2 - 4, 4, 0.0F);
		this.box.setRotationPoint(0.0F, (float)b3, 0.0F);
		this.box.rotateAngleX = ((float)Math.PI / 2F);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.box.render(par7);
	}
}
