package growthcraft.pipes.client.model;

import growthcraft.pipes.util.PipeFlag;
import growthcraft.pipes.util.PipeConsts;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

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
		this.textureWidth  = 128;
		this.textureHeight = 128;

		final float scale = 16.0f;

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
			innerSides[i] = new ModelRenderer(this, 0, 8 * (i + 1));
			generatePipeModel(innerSides[i], PipeConsts.INNER_SIDES_CENT[i], PipeConsts.INNER_SIDES_DIM[i], scale);
			innerSides[i].setTextureSize(textureWidth, textureHeight);
			innerSides[i].mirror = true;
			setRotation(innerSides[i], 0f, 0f, 0f);

			busSides[i] = new ModelRenderer(this, 16, 32 + (12 * i));
			generatePipeModel(busSides[i], PipeConsts.BUS_SIDES_CENT[i], PipeConsts.BUS_SIDES_DIM[i], scale);
			busSides[i].setTextureSize(textureWidth, textureHeight);
			busSides[i].mirror = true;
			setRotation(busSides[i], 0f, 0f, 0f);

			pipeSides[i] = new ModelRenderer(this, 64, 32 + (8 * i));
			generatePipeModel(pipeSides[i], PipeConsts.PIPE_SIDES_CENT[i], PipeConsts.PIPE_SIDES_DIM[i], scale);
			pipeSides[i].setTextureSize(textureWidth, textureHeight);
			pipeSides[i].mirror = true;
			setRotation(pipeSides[i], 0f, 0f, 0f);
		}
	}

	private void generatePipeModel(ModelRenderer model, float[] pos, float[] dim, float scale)
	{
		final float x = pos[0] * scale;
		final float y = pos[1] * scale;
		final float z = pos[2] * scale;
		final int w = (int)(dim[0] * scale);
		final int h = (int)(dim[1] * scale);
		final int l = (int)(dim[2] * scale);

		model.addBox(x, y, z, w, h, l);
		model.setRotationPoint(-w / 2.0f, -h / 2.0f, -l / 2.0f);
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
