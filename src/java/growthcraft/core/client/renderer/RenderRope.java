package growthcraft.core.client.renderer;

import growthcraft.core.utils.RenderUtils;
import growthcraft.core.block.BlockRope;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderRope implements ISimpleBlockRenderingHandler
{
	public static final int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			final BlockRope blockRope = (BlockRope)block;
			final double d = 0.0625D;
			final Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			final IIcon icon = blockRope.getIconByIndex(0);

			final boolean flag = ((BlockRope)block).canConnectRopeTo(world, x, y, z - 1);
			final boolean flag1 = ((BlockRope)block).canConnectRopeTo(world, x, y, z + 1);
			final boolean flag2 = ((BlockRope)block).canConnectRopeTo(world, x - 1, y, z);
			final boolean flag3 = ((BlockRope)block).canConnectRopeTo(world, x + 1, y, z);
			final boolean flag4 = ((BlockRope)block).canConnectRopeTo(world, x, y - 1, z);
			final boolean flag5 = ((BlockRope)block).canConnectRopeTo(world, x, y + 1, z);

			double minX;
			double maxX;
			double minY;
			double maxY;
			double minZ;
			double maxZ;

			double minU;
			double maxU;
			final double minV = (double)icon.getInterpolatedV(14);
			final double maxV = (double)icon.getMaxV();

			if (flag && flag1)
			{
				minX = (double)x + 7*d;
				maxX = (double)x + 9*d;
				minY = (double)y + 7*d;
				maxY = (double)y + 9*d;
				minZ = (double)z;
				maxZ = (double)z + 16*d;

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();

				RenderUtils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
			else
			{
				if (flag)
				{
					minX = (double)x + 7*d;
					maxX = (double)x + 9*d;
					minY = (double)y + 7*d;
					maxY = (double)y + 9*d;
					minZ = (double)z;
					maxZ = (double)z + 8*d;

					minU = (double)icon.getInterpolatedU(8);
					maxU = (double)icon.getMaxU();

					RenderUtils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}

				if (flag1)
				{
					minX = (double)x + 7*d;
					maxX = (double)x + 9*d;
					minY = (double)y + 7*d;
					maxY = (double)y + 9*d;
					minZ = (double)z + 8*d;
					maxZ = (double)z + 16*d;

					minU = (double)icon.getMinU();
					maxU = (double)icon.getInterpolatedU(8);

					RenderUtils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}
			}

			if (flag2 && flag3)
			{
				minX = (double)x;
				maxX = (double)x + 16*d;
				minY = (double)y + 7*d;
				maxY = (double)y + 9*d;
				minZ = (double)z + 7*d;
				maxZ = (double)z + 9*d;

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();

				RenderUtils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
			else
			{
				if (flag2)
				{
					minX = (double)x;
					maxX = (double)x + 8*d;
					minY = (double)y + 7*d;
					maxY = (double)y + 9*d;
					minZ = (double)z + 7*d;
					maxZ = (double)z + 9*d;

					minU = (double)icon.getInterpolatedU(8);
					maxU = (double)icon.getMaxU();

					RenderUtils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}

				if (flag3)
				{
					minX = (double)x + 8*d;
					maxX = (double)x + 16*d;
					minY = (double)y + 7*d;
					maxY = (double)y + 9*d;
					minZ = (double)z + 7*d;
					maxZ = (double)z + 9*d;

					minU = (double)icon.getMinU();
					maxU = (double)icon.getInterpolatedU(8);

					RenderUtils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}
			}

			if (flag4 && flag5)
			{
				minX = (double)x + 7*d;
				maxX = (double)x + 9*d;
				minY = (double)y;
				maxY = (double)y + 16*d;
				minZ = (double)z + 7*d;
				maxZ = (double)z + 9*d;

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();

				RenderUtils.drawCrossSquaresAlongYRotated(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
			else
			{
				if (flag4)
				{
					minX = (double)x + 7*d;
					maxX = (double)x + 9*d;
					minY = (double)y;
					maxY = (double)y + 8*d;
					minZ = (double)z + 7*d;
					maxZ = (double)z + 9*d;

					minU = (double)icon.getInterpolatedU(8);
					maxU = (double)icon.getMaxU();

					RenderUtils.drawCrossSquaresAlongYRotated(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}

				if (flag5)
				{
					minX = (double)x + 7*d;
					maxX = (double)x + 9*d;
					minY = (double)y + 8*d;
					maxY = (double)y + 16*d;
					minZ = (double)z + 7*d;
					maxZ = (double)z + 9*d;

					minU = (double)icon.getMinU();
					maxU = (double)icon.getInterpolatedU(8);

					RenderUtils.drawCrossSquaresAlongYRotated(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}
			}
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return id;
	}
}
