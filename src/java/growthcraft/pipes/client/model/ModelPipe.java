package growthcraft.pipes.client.model;

import growthcraft.pipes.utils.PipeFlag;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.util.ForgeDirection;

public class ModelPipe extends ModelBase
{
	public static ModelPipe INSTANCE = new ModelPipe();

	ModelRenderer pipeCore;
	ModelRenderer vacuumCore;
	// small inner cubes when using the pipeCore, these are not rendered if
	// using the vacuumCore
	ModelRenderer[] innerSides = new ModelRenderer[6];
	// panels placed against other fluid handlers
	ModelRenderer[] busSides = new ModelRenderer[6];
	// placed against other pipes
	ModelRenderer[] pipeSides = new ModelRenderer[6];

	public ModelPipe()
	{
		textureWidth  = 128;
		textureHeight = 128;

		pipeCore = new ModelRenderer(this, 0, 0);
		pipeCore.addBox(0f, 0f, 0f, 4, 4, 4);
		pipeCore.setRotationPoint(-2f, -2f, -2f);
		pipeCore.setTextureSize(textureWidth, textureHeight);
		pipeCore.mirror = true;
		setRotation(pipeCore, 0f, 0f, 0f);

		vacuumCore = new ModelRenderer(this, 16, 0);
		vacuumCore.addBox(0f, 0f, 0f, 10, 10, 10);
		vacuumCore.setRotationPoint(-5f, -5f, -5f);
		vacuumCore.setTextureSize(textureWidth, textureHeight);
		vacuumCore.mirror = true;
		setRotation(pipeCore, 0f, 0f, 0f);

		for (int i = 0; i < 6; ++i)
		{
			final ForgeDirection dir = ForgeDirection.getOrientation(i);
			final float absX = MathHelper.abs(dir.offsetX);
			final float absY = MathHelper.abs(dir.offsetY);
			final float absZ = MathHelper.abs(dir.offsetZ);
			innerSides[i] = new ModelRenderer(this, 0, 8 * (i + 1));
			innerSides[i].addBox(3.5f * dir.offsetX, 3.5f * dir.offsetY, 3.5f * dir.offsetZ,
				(int)(4f - (1f * absX)), (int)(4f - (1f * absY)), (int)(4f - (1f * absZ)));
			innerSides[i].setRotationPoint(-(2f - (0.5f * absX)), -(2f - (0.5f * absY)), -(2f - (0.5f * absZ)));
			innerSides[i].setTextureSize(textureWidth, textureHeight);
			innerSides[i].mirror = true;
			setRotation(innerSides[i], 0f, 0f, 0f);

			busSides[i] = new ModelRenderer(this, 16, 32 + (12 * i));
			busSides[i].addBox(6.5f * dir.offsetX, 6.5f * dir.offsetY, 6.5f * dir.offsetZ,
				(int)(6f - (3f * absX)), (int)(6f - (3f * absY)), (int)(6f - (3f * absZ)));
			busSides[i].setRotationPoint(-(3f - (1.5f * absX)), -(3f - (1.5f * absY)), -(3f - (1.5f * absZ)));
			busSides[i].setTextureSize(textureWidth, textureHeight);
			busSides[i].mirror = true;
			setRotation(busSides[i], 0f, 0f, 0f);

			pipeSides[i] = new ModelRenderer(this, 64, 32 + (8 * i));
			pipeSides[i].addBox(6.5f * dir.offsetX, 6.5f * dir.offsetY, 6.5f * dir.offsetZ,
				(int)(4f - (1f * absX)), (int)(4f - (1f * absY)), (int)(4f - (1f * absZ)));
			pipeSides[i].setRotationPoint(-(2f - (0.5f * absX)), -(2f - (0.5f * absY)), -(2f - (0.5f * absZ)));
			pipeSides[i].setTextureSize(textureWidth, textureHeight);
			pipeSides[i].mirror = true;
			setRotation(pipeSides[i], 0f, 0f, 0f);
		}
	}

	public void render(int flags, float scale)
	{
		boolean vacuum = false;
		if ((flags & PipeFlag.PIPE_CORE) == PipeFlag.PIPE_CORE)
		{
			pipeCore.render(scale);
		}
		else if ((flags & PipeFlag.PIPE_VACUUM_CORE) == PipeFlag.PIPE_VACUUM_CORE)
		{
			vacuum = true;
			vacuumCore.render(scale);
		}

		for (int i = 0; i < 6; ++i)
		{
			final int testFlag = 1 << i;
			final int testFlagBus = 1 << (i + 6);
			final boolean pipe = (flags & testFlag) == testFlag;
			final boolean bus = (flags & testFlagBus) == testFlagBus;
			if (!vacuum && (pipe || bus))
			{
				innerSides[i].render(scale);
			}
			if (bus)
			{
				busSides[i].render(scale);
			}
			else if (pipe)
			{
				pipeSides[i].render(scale);
			}
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
