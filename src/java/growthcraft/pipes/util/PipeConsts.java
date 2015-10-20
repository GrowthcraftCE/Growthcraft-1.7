package growthcraft.pipes.util;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public final class PipeConsts
{
	// core is (4 / 16) ^ 3
	public static final float BASE_CORE_WIDTH = 4.0f / 16.0f;
	public static final float BASE_CORE_HEIGHT = 4.0f / 16.0f;
	public static final float BASE_CORE_LENGTH = 4.0f / 16.0f;
	public static final float BASE_CORE_MIN_X = (1.0f - BASE_CORE_WIDTH) / 2;
	public static final float BASE_CORE_MIN_Y = (1.0f - BASE_CORE_HEIGHT) / 2;
	public static final float BASE_CORE_MIN_Z = (1.0f - BASE_CORE_LENGTH) / 2;
	public static final float BASE_CORE_MAX_X = BASE_CORE_MIN_X + BASE_CORE_WIDTH;
	public static final float BASE_CORE_MAX_Y = BASE_CORE_MIN_Y + BASE_CORE_HEIGHT;
	public static final float BASE_CORE_MAX_Z = BASE_CORE_MIN_Z + BASE_CORE_LENGTH;

	public static final float VACUUM_CORE_WIDTH = 10.0f / 16.0f;
	public static final float VACUUM_CORE_HEIGHT = 10.0f / 16.0f;
	public static final float VACUUM_CORE_LENGTH = 10.0f / 16.0f;
	public static final float VACUUM_CORE_MIN_X = (1.0f - VACUUM_CORE_WIDTH) / 2;
	public static final float VACUUM_CORE_MIN_Y = (1.0f - VACUUM_CORE_HEIGHT) / 2;
	public static final float VACUUM_CORE_MIN_Z = (1.0f - VACUUM_CORE_LENGTH) / 2;
	public static final float VACUUM_CORE_MAX_X = VACUUM_CORE_MIN_X + VACUUM_CORE_WIDTH;
	public static final float VACUUM_CORE_MAX_Y = VACUUM_CORE_MIN_Y + VACUUM_CORE_HEIGHT;
	public static final float VACUUM_CORE_MAX_Z = VACUUM_CORE_MIN_Z + VACUUM_CORE_LENGTH;

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
		for (int i = 0; i < 6; ++i)
		{
			final ForgeDirection dir = ForgeDirection.getOrientation(i);
			final float absX = MathHelper.abs(dir.offsetX);
			final float absY = MathHelper.abs(dir.offsetY);
			final float absZ = MathHelper.abs(dir.offsetZ);

			INNER_SIDES_CENT[i][0] = 3.5f * dir.offsetX;
			INNER_SIDES_CENT[i][1] = 3.5f * dir.offsetY;
			INNER_SIDES_CENT[i][2] = 3.5f * dir.offsetZ;
			INNER_SIDES_CENT[i][3] = INNER_SIDES_CENT[i][0] + 4f - 1f * absX;
			INNER_SIDES_CENT[i][4] = INNER_SIDES_CENT[i][1] + 4f - 1f * absY;
			INNER_SIDES_CENT[i][5] = INNER_SIDES_CENT[i][2] + 4f - 1f * absZ;

			BUS_SIDES_CENT[i][0] = 6.5f * dir.offsetX;
			BUS_SIDES_CENT[i][1] = 6.5f * dir.offsetY;
			BUS_SIDES_CENT[i][2] = 6.5f * dir.offsetZ;
			BUS_SIDES_CENT[i][3] = BUS_SIDES_CENT[i][0] + 6f - 3f * absX;
			BUS_SIDES_CENT[i][4] = BUS_SIDES_CENT[i][1] + 6f - 3f * absY;
			BUS_SIDES_CENT[i][5] = BUS_SIDES_CENT[i][2] + 6f - 3f * absZ;

			PIPE_SIDES_CENT[i][0] = 6.5f * dir.offsetX;
			PIPE_SIDES_CENT[i][1] = 6.5f * dir.offsetY;
			PIPE_SIDES_CENT[i][2] = 6.5f * dir.offsetZ;
			PIPE_SIDES_CENT[i][3] = PIPE_SIDES_CENT[i][0] + 4f - 1f * absX;
			PIPE_SIDES_CENT[i][4] = PIPE_SIDES_CENT[i][1] + 4f - 1f * absY;
			PIPE_SIDES_CENT[i][5] = PIPE_SIDES_CENT[i][2] + 4f - 1f * absZ;

			for (int j = 0; j < 6; ++j)
			{
				// downscale by 16
				INNER_SIDES_CENT[i][j] /= 16.0f;
				BUS_SIDES_CENT[i][j] /= 16.0f;
				PIPE_SIDES_CENT[i][j] /= 16.0f;
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
