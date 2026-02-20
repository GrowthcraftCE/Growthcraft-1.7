package growthcraft.milk.client.model;

import growthcraft.api.core.util.Easing;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * cheesewheel.tcn - TechneToTabulaImporter
 * Created using Tabula 4.1.1
 */
public class ModelCheesePress extends ModelBase
{
	public static final float SCALE = 1.0f / 16.0f;

	public ModelRenderer base;
	public ModelRenderer barrelSide1;
	public ModelRenderer barrelSide9;
	public ModelRenderer barrelSide10;
	public ModelRenderer barrelSide4;
	public ModelRenderer barrelSide6;
	public ModelRenderer barrelSide8;
	public ModelRenderer barrelSide3;
	public ModelRenderer barrelSide7;
	public ModelRenderer barrelSide12;
	public ModelRenderer barrelSide11;
	public ModelRenderer barrelSide2;
	public ModelRenderer barrelSide5;
	public ModelRenderer standLeg1;
	public ModelRenderer standLeg2;
	public ModelRenderer standFoot2;
	public ModelRenderer standFoot1;
	public ModelRenderer barrelTop1;
	public ModelRenderer barrelTop3;
	public ModelRenderer barrelTop2;
	public ModelRenderer barrelTop4;
	public ModelRenderer barrelTop5;
	public ModelRenderer screwShaft;
	public ModelRenderer handle;
	public ModelRenderer crossbar;

	public ModelCheesePress()
	{
		this.textureWidth = 74;
		this.textureHeight = 64;
		this.crossbar = new ModelRenderer(this, 37, 31);
		this.crossbar.setRotationPoint(-1.0F, 11.0F, -7.0F);
		this.crossbar.addBox(0.0F, 0.0F, 0.0F, 2, 1, 14, 0.0F);
		this.barrelTop4 = new ModelRenderer(this, 13, 39);
		this.barrelTop4.setRotationPoint(-3.0F, 19.0F, 2.0F);
		this.barrelTop4.addBox(0.0F, -6.0F, 0.0F, 6, 1, 1, 0.0F);
		this.standFoot1 = new ModelRenderer(this, 0, 13);
		this.standFoot1.setRotationPoint(-4.0F, 22.0F, -8.0F);
		this.standFoot1.addBox(0.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
		this.handle = new ModelRenderer(this, 39, 48);
		this.handle.setRotationPoint(-0.1F, 8.0F, 0.0F);
		this.handle.addBox(-1.0F, 0.0F, -6.0F, 2, 1, 12, 0.0F);
		this.barrelSide12 = new ModelRenderer(this, 10, 3);
		this.barrelSide12.setRotationPoint(0.0F, 23.0F, -1.0F);
		this.barrelSide12.addBox(-3.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.barrelSide3 = new ModelRenderer(this, 64, 3);
		this.barrelSide3.setRotationPoint(1.0F, 23.0F, 0.0F);
		this.barrelSide3.addBox(2.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.standFoot2 = new ModelRenderer(this, 0, 13);
		this.standFoot2.setRotationPoint(-4.0F, 22.0F, 7.0F);
		this.standFoot2.addBox(0.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
		this.barrelTop3 = new ModelRenderer(this, 0, 43);
		this.barrelTop3.setRotationPoint(-4.0F, 19.0F, -2.0F);
		this.barrelTop3.addBox(0.0F, -6.0F, 0.0F, 8, 1, 4, 0.0F);
		this.barrelSide9 = new ModelRenderer(this, 28, 3);
		this.barrelSide9.setRotationPoint(0.0F, 23.0F, 5.0F);
		this.barrelSide9.addBox(-4.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.barrelSide1 = new ModelRenderer(this, 0, 3);
		this.barrelSide1.setRotationPoint(0.0F, 23.0F, -1.0F);
		this.barrelSide1.addBox(-2.0F, -8.0F, -4.0F, 4, 8, 1, 0.0F);
		this.barrelSide4 = new ModelRenderer(this, 54, 0);
		this.barrelSide4.setRotationPoint(4.0F, 21.0F, -2.0F);
		this.barrelSide4.addBox(0.0F, -6.0F, 0.0F, 1, 8, 4, 0.0F);
		this.screwShaft = new ModelRenderer(this, 0, 52);
		this.screwShaft.setRotationPoint(0.0F, 9.0F, 0.0F);
		this.screwShaft.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.barrelSide7 = new ModelRenderer(this, 36, 3);
		this.barrelSide7.setRotationPoint(0.0F, 23.0F, 8.0F);
		this.barrelSide7.addBox(-2.0F, -8.0F, -4.0F, 4, 8, 1, 0.0F);
		this.barrelSide2 = new ModelRenderer(this, 68, 3);
		this.barrelSide2.setRotationPoint(0.0F, 23.0F, -1.0F);
		this.barrelSide2.addBox(2.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.barrelSide10 = new ModelRenderer(this, 18, 0);
		this.barrelSide10.setRotationPoint(-7.0F, 23.0F, 1.0F);
		this.barrelSide10.addBox(2.0F, -8.0F, -3.0F, 1, 8, 4, 0.0F);
		this.standLeg2 = new ModelRenderer(this, 0, 15);
		this.standLeg2.setRotationPoint(-1.0F, 10.0F, 7.0F);
		this.standLeg2.addBox(0.0F, 0.0F, 0.0F, 2, 12, 1, 0.0F);
		this.base = new ModelRenderer(this, 9, 12);
		this.base.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.base.addBox(-8.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
		this.barrelSide8 = new ModelRenderer(this, 32, 3);
		this.barrelSide8.setRotationPoint(0.0F, 23.0F, 6.0F);
		this.barrelSide8.addBox(-3.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.barrelSide11 = new ModelRenderer(this, 14, 3);
		this.barrelSide11.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.barrelSide11.addBox(-4.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.barrelTop5 = new ModelRenderer(this, 10, 35);
		this.barrelTop5.setRotationPoint(-2.0F, 19.0F, 3.0F);
		this.barrelTop5.addBox(0.0F, -6.0F, 0.0F, 4, 1, 1, 0.0F);
		this.barrelTop1 = new ModelRenderer(this, 0, 35);
		this.barrelTop1.setRotationPoint(-2.0F, 19.0F, -4.0F);
		this.barrelTop1.addBox(0.0F, -6.0F, 0.0F, 4, 1, 1, 0.0F);
		this.standLeg1 = new ModelRenderer(this, 0, 15);
		this.standLeg1.setRotationPoint(-1.0F, 10.0F, -8.0F);
		this.standLeg1.addBox(0.0F, 0.0F, 0.0F, 2, 12, 1, 0.0F);
		this.barrelTop2 = new ModelRenderer(this, 0, 39);
		this.barrelTop2.setRotationPoint(-3.0F, 19.0F, -3.0F);
		this.barrelTop2.addBox(0.0F, -6.0F, 0.0F, 6, 1, 1, 0.0F);
		this.barrelSide6 = new ModelRenderer(this, 46, 3);
		this.barrelSide6.setRotationPoint(0.0F, 23.0F, 6.0F);
		this.barrelSide6.addBox(2.0F, -8.0F, -3.0F, 1, 8, 1, 0.0F);
		this.barrelSide5 = new ModelRenderer(this, 50, 3);
		this.barrelSide5.setRotationPoint(1.0F, 23.0F, 6.0F);
		this.barrelSide5.addBox(2.0F, -8.0F, -4.0F, 1, 8, 1, 0.0F);
	}

	@Override
	public void render(Entity entity, float progress, float progressDir, float f2, float f3, float f4, float scale)
	{
		final float eased = (float)((progressDir < 0) ? Easing.d.cubicIn : Easing.d.cubicOut).call(progress);

		this.base.render(scale);

		this.standFoot1.render(scale);
		this.standFoot2.render(scale);
		this.standLeg1.render(scale);
		this.standLeg2.render(scale);
		this.crossbar.render(scale);

		this.barrelSide1.render(scale);
		this.barrelSide2.render(scale);
		this.barrelSide3.render(scale);
		this.barrelSide4.render(scale);
		this.barrelSide5.render(scale);
		this.barrelSide6.render(scale);
		this.barrelSide7.render(scale);
		this.barrelSide8.render(scale);
		this.barrelSide9.render(scale);
		this.barrelSide10.render(scale);
		this.barrelSide11.render(scale);
		this.barrelSide12.render(scale);

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(0.0f, eased * SCALE * 2, 0.0f);
			this.barrelTop4.render(scale);
			this.barrelTop3.render(scale);
			this.barrelTop5.render(scale);
			this.barrelTop1.render(scale);
			this.barrelTop2.render(scale);

			GL11.glRotatef(eased * 360.0f, 0.0f, 1.0f, 0.0f);

			this.handle.render(scale);
			this.screwShaft.render(scale);
		}
		GL11.glPopMatrix();
	}
}
