/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.pipes.util;

import growthcraft.core.util.BoundUtils;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public final class PipeConsts
{
	public static final float SCALE = 32.0f;
	public static final float RENDER_SCALE = 1.0f / SCALE;
	// core is (4 / 16) ^ 3
	public static final float[] PIPE_BASE_CORE = new float[6];
	public static final float[] PIPE_VACUUM_CORE = new float[6];
	public static final float BASE_CORE_WIDTH = 6.0f / SCALE;
	public static final float BASE_CORE_HEIGHT = 6.0f / SCALE;
	public static final float BASE_CORE_LENGTH = 6.0f / SCALE;
	public static float BASE_CORE_MIN_X;
	public static float BASE_CORE_MIN_Y;
	public static float BASE_CORE_MIN_Z;
	public static float BASE_CORE_MAX_X;
	public static float BASE_CORE_MAX_Y;
	public static float BASE_CORE_MAX_Z;

	public static final float VACUUM_CORE_WIDTH = 8.0f / SCALE;
	public static final float VACUUM_CORE_HEIGHT = 8.0f / SCALE;
	public static final float VACUUM_CORE_LENGTH = 8.0f / SCALE;
	public static float VACUUM_CORE_MIN_X;
	public static float VACUUM_CORE_MIN_Y;
	public static float VACUUM_CORE_MIN_Z;
	public static float VACUUM_CORE_MAX_X;
	public static float VACUUM_CORE_MAX_Y;
	public static float VACUUM_CORE_MAX_Z;

	public static float[] PIPE_BASE_CORE_CENT;
	public static float[] PIPE_BASE_CORE_DIM;
	public static float[] PIPE_VACUUM_CORE_CENT;
	public static float[] PIPE_VACUUM_CORE_DIM;
	public static final float[][] INNER_SIDES_CENT = new float[6][6];
	public static final float[][] INNER_SIDES_DIM = new float[6][3];
	public static final float[][] BUS_SIDES_CENT = new float[6][6];
	public static final float[][] BUS_SIDES_DIM = new float[6][3];
	public static final float[][] PIPE_SIDES_CENT = new float[6][6];
	public static final float[][] PIPE_SIDES_DIM = new float[6][3];

	public static final float[][] INNER_SIDES = new float[6][6];
	public static final float[][] BUS_SIDES = new float[6][6];
	public static final float[][] PIPE_SIDES = new float[6][6];

	static
	{
		BoundUtils.centeredCubeBounds(PIPE_BASE_CORE, BASE_CORE_WIDTH, BASE_CORE_HEIGHT, BASE_CORE_LENGTH);
		BoundUtils.centeredCubeBounds(PIPE_VACUUM_CORE, VACUUM_CORE_WIDTH, VACUUM_CORE_HEIGHT, VACUUM_CORE_LENGTH);

		BASE_CORE_MIN_X = PIPE_BASE_CORE[0];
		BASE_CORE_MIN_Y = PIPE_BASE_CORE[1];
		BASE_CORE_MIN_Z = PIPE_BASE_CORE[2];
		BASE_CORE_MAX_X = PIPE_BASE_CORE[3];
		BASE_CORE_MAX_Y = PIPE_BASE_CORE[4];
		BASE_CORE_MAX_Z = PIPE_BASE_CORE[5];

		VACUUM_CORE_MIN_X = PIPE_VACUUM_CORE[0];
		VACUUM_CORE_MIN_Y = PIPE_VACUUM_CORE[1];
		VACUUM_CORE_MIN_Z = PIPE_VACUUM_CORE[2];
		VACUUM_CORE_MAX_X = PIPE_VACUUM_CORE[3];
		VACUUM_CORE_MAX_Y = PIPE_VACUUM_CORE[4];
		VACUUM_CORE_MAX_Z = PIPE_VACUUM_CORE[5];

		PIPE_BASE_CORE_CENT = new float[] { BASE_CORE_MIN_X, BASE_CORE_MIN_Y, BASE_CORE_MIN_Z, BASE_CORE_MAX_X, BASE_CORE_MAX_Y, BASE_CORE_MAX_Z };
		PIPE_BASE_CORE_DIM = new float[] { BASE_CORE_MAX_X - BASE_CORE_MIN_X, BASE_CORE_MAX_Y - BASE_CORE_MIN_Y, BASE_CORE_MAX_Z - BASE_CORE_MIN_Z };
		PIPE_VACUUM_CORE_CENT = new float[] { VACUUM_CORE_MIN_X, VACUUM_CORE_MIN_Y, VACUUM_CORE_MIN_Z, VACUUM_CORE_MAX_X, VACUUM_CORE_MAX_Y, VACUUM_CORE_MAX_Z };
		PIPE_VACUUM_CORE_DIM = new float[] { VACUUM_CORE_MAX_X - VACUUM_CORE_MIN_X, VACUUM_CORE_MAX_Y - VACUUM_CORE_MIN_Y, VACUUM_CORE_MAX_Z - VACUUM_CORE_MIN_Z };

		for (int i = 0; i < 6; ++i)
		{
			final ForgeDirection dir = ForgeDirection.getOrientation(i);
			final float absX = MathHelper.abs(dir.offsetX);
			final float absY = MathHelper.abs(dir.offsetY);
			final float absZ = MathHelper.abs(dir.offsetZ);

			INNER_SIDES_CENT[i][0] = 8.0f * dir.offsetX;
			INNER_SIDES_CENT[i][1] = 8.0f * dir.offsetY;
			INNER_SIDES_CENT[i][2] = 8.0f * dir.offsetZ;
			INNER_SIDES_CENT[i][3] = INNER_SIDES_CENT[i][0] + 4f + 6f * absX;
			INNER_SIDES_CENT[i][4] = INNER_SIDES_CENT[i][1] + 4f + 6f * absY;
			INNER_SIDES_CENT[i][5] = INNER_SIDES_CENT[i][2] + 4f + 6f * absZ;

			BUS_SIDES_CENT[i][0] = 13f * dir.offsetX;
			BUS_SIDES_CENT[i][1] = 13f * dir.offsetY;
			BUS_SIDES_CENT[i][2] = 13f * dir.offsetZ;
			BUS_SIDES_CENT[i][3] = BUS_SIDES_CENT[i][0] + 8f - 2f * absX;
			BUS_SIDES_CENT[i][4] = BUS_SIDES_CENT[i][1] + 8f - 2f * absY;
			BUS_SIDES_CENT[i][5] = BUS_SIDES_CENT[i][2] + 8f - 2f * absZ;

			PIPE_SIDES_CENT[i][0] = 14.5f * dir.offsetX;
			PIPE_SIDES_CENT[i][1] = 14.5f * dir.offsetY;
			PIPE_SIDES_CENT[i][2] = 14.5f * dir.offsetZ;
			PIPE_SIDES_CENT[i][3] = PIPE_SIDES_CENT[i][0] + 4f - 1f * absX;
			PIPE_SIDES_CENT[i][4] = PIPE_SIDES_CENT[i][1] + 4f - 1f * absY;
			PIPE_SIDES_CENT[i][5] = PIPE_SIDES_CENT[i][2] + 4f - 1f * absZ;

			for (int j = 0; j < 6; ++j)
			{
				// downscale by 16
				INNER_SIDES_CENT[i][j] /= SCALE;
				BUS_SIDES_CENT[i][j] /= SCALE;
				PIPE_SIDES_CENT[i][j] /= SCALE;
			}

			// calculate dimensions and apply offset
			for (int j = 0; j < 3; ++j)
			{
				INNER_SIDES_DIM[i][j] = INNER_SIDES_CENT[i][3 + j] - INNER_SIDES_CENT[i][j];
				BUS_SIDES_DIM[i][j] = BUS_SIDES_CENT[i][3 + j] - BUS_SIDES_CENT[i][j];
				PIPE_SIDES_DIM[i][j] = PIPE_SIDES_CENT[i][3 + j] - PIPE_SIDES_CENT[i][j];
			}

			for (int j = 0; j < 6; ++j)
			{
				// translate by 0.5f
				INNER_SIDES[i][j] = 0.5f + INNER_SIDES_CENT[i][j] - INNER_SIDES_DIM[i][j % 3] / 2.0f;
				BUS_SIDES[i][j] = 0.5f + BUS_SIDES_CENT[i][j] - BUS_SIDES_DIM[i][j % 3] / 2.0f;
				PIPE_SIDES[i][j] = 0.5f + PIPE_SIDES_CENT[i][j] - PIPE_SIDES_DIM[i][j % 3] / 2.0f;
			}
		}
	}

	private PipeConsts() {}
}
