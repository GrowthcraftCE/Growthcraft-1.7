// This has been stolen from Botania by IceDragon (blame him if anything goes wrong)
package growthcraft.core.client.util;

import java.lang.reflect.Field;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.AnimationMetadataSection;

// This is all vanilla code from 1.8, thanks to ganymedes01 porting it to 1.7 :D
@SideOnly(Side.CLIENT)
public class InterpolatedIcon extends TextureAtlasSprite
{
	private static final String[] obfuscationAnimationMetadata = new String[] { "animationMetadata", "field_110982_k", "j" };

	protected int[][] interpolatedFrameData;
	private Field fanimationMetadata;

	public InterpolatedIcon(String name)
	{
		super(name);
		this.fanimationMetadata = ReflectionHelper.findField(TextureAtlasSprite.class, obfuscationAnimationMetadata);
		this.fanimationMetadata.setAccessible(true);
	}

	private void updateAnimationInterpolated() throws IllegalArgumentException, IllegalAccessException
	{
		final AnimationMetadataSection animationMetadata = (AnimationMetadataSection) fanimationMetadata.get(this);

		final double d0 = 1.0D - tickCounter / (double) animationMetadata.getFrameTimeSingle(frameCounter);
		final int i = animationMetadata.getFrameIndex(frameCounter);
		final int j = animationMetadata.getFrameCount() == 0 ? framesTextureData.size() : animationMetadata.getFrameCount();
		final int k = animationMetadata.getFrameIndex((frameCounter + 1) % j);

		if (i != k && k >= 0 && k < framesTextureData.size())
		{
			final int[][] aint = (int[][]) framesTextureData.get(i);
			final int[][] aint1 = (int[][]) framesTextureData.get(k);

			if(interpolatedFrameData == null || interpolatedFrameData.length != aint.length)
			{
				interpolatedFrameData = new int[aint.length][];
			}

			for(int l = 0; l < aint.length; l++)
			{
				if (interpolatedFrameData[l] == null)
				{
					interpolatedFrameData[l] = new int[aint[l].length];
				}

				if (l < aint1.length && aint1[l].length == aint[l].length)
				{
					for (int i1 = 0; i1 < aint[l].length; ++i1)
					{
						final int j1 = aint[l][i1];
						final int k1 = aint1[l][i1];
						final int l1 = (int) (((j1 & 16711680) >> 16) * d0 + ((k1 & 16711680) >> 16) * (1.0D - d0));
						final int i2 = (int) (((j1 & 65280) >> 8) * d0 + ((k1 & 65280) >> 8) * (1.0D - d0));
						final int j2 = (int) ((j1 & 255) * d0 + (k1 & 255) * (1.0D - d0));
						interpolatedFrameData[l][i1] = j1 & -16777216 | l1 << 16 | i2 << 8 | j2;
					}
				}
			}
			TextureUtil.uploadTextureMipmap(interpolatedFrameData, width, height, originX, originY, false, false);
		}
	}

	@Override
	public void updateAnimation()
	{
		super.updateAnimation();
		try
		{
			updateAnimationInterpolated();
		}
		catch (Exception e)
		{
			// NO-OP
		}
	}
}
