package growthcraft.core.render;

import growthcraft.core.Utils;
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
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			double d = 0.0625D;
			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			IIcon icon = BlockRope.tex[0];

			boolean flag = ((BlockRope)block).canConnectRopeTo(world, x, y, z - 1);
			boolean flag1 = ((BlockRope)block).canConnectRopeTo(world, x, y, z + 1);
			boolean flag2 = ((BlockRope)block).canConnectRopeTo(world, x - 1, y, z);
			boolean flag3 = ((BlockRope)block).canConnectRopeTo(world, x + 1, y, z);
			boolean flag4 = ((BlockRope)block).canConnectRopeTo(world, x, y - 1, z);
			boolean flag5 = ((BlockRope)block).canConnectRopeTo(world, x, y + 1, z);

			double minX;
			double maxX;
			double minY;
			double maxY;
			double minZ;
			double maxZ;

			double minU;
			double maxU;
			double minV = (double)icon.getInterpolatedV(14);
			double maxV = (double)icon.getMaxV();

			if (flag && flag1)
			{
				minX = ((double)x + 7*d);
				maxX = ((double)x + 9*d);
				minY = ((double)y + 7*d);
				maxY = ((double)y + 9*d);
				minZ = ((double)z);
				maxZ = ((double)z + 16*d);

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();

				Utils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
			else
			{
				if (flag)
				{
					minX = ((double)x + 7*d);
					maxX = ((double)x + 9*d);
					minY = ((double)y + 7*d);
					maxY = ((double)y + 9*d);
					minZ = ((double)z);
					maxZ = ((double)z + 8*d);

					minU = (double)icon.getInterpolatedU(8);
					maxU = (double)icon.getMaxU();

					Utils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}

				if (flag1)
				{
					minX = ((double)x + 7*d);
					maxX = ((double)x + 9*d);
					minY = ((double)y + 7*d);
					maxY = ((double)y + 9*d);
					minZ = ((double)z + 8*d);
					maxZ = ((double)z + 16*d);

					minU = (double)icon.getMinU();
					maxU = (double)icon.getInterpolatedU(8);

					Utils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}
			}

			if (flag2 && flag3)
			{
				minX = ((double)x);
				maxX = ((double)x + 16*d);
				minY = ((double)y + 7*d);
				maxY = ((double)y + 9*d);
				minZ = ((double)z + 7*d);
				maxZ = ((double)z + 9*d);

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();

				Utils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
			else
			{
				if (flag2)
				{
					minX = ((double)x);
					maxX = ((double)x + 8*d);
					minY = ((double)y + 7*d);
					maxY = ((double)y + 9*d);
					minZ = ((double)z + 7*d);
					maxZ = ((double)z + 9*d);

					minU = (double)icon.getInterpolatedU(8);
					maxU = (double)icon.getMaxU();

					Utils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}

				if (flag3)
				{
					minX = ((double)x + 8*d);
					maxX = ((double)x + 16*d);
					minY = ((double)y + 7*d);
					maxY = ((double)y + 9*d);
					minZ = ((double)z + 7*d);
					maxZ = ((double)z + 9*d);

					minU = (double)icon.getMinU();
					maxU = (double)icon.getInterpolatedU(8);

					Utils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}
			}

			if (flag4 && flag5)
			{
				minX = ((double)x + 7*d);
				maxX = ((double)x + 9*d);
				minY = ((double)y);
				maxY = ((double)y + 16*d);
				minZ = ((double)z + 7*d);
				maxZ = ((double)z + 9*d);

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();

				Utils.drawCrossSquaresAlongYRotated(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
			else
			{
				if (flag4)
				{
					minX = ((double)x + 7*d);
					maxX = ((double)x + 9*d);
					minY = ((double)y);
					maxY = ((double)y + 8*d);
					minZ = ((double)z + 7*d);
					maxZ = ((double)z + 9*d);

					minU = (double)icon.getInterpolatedU(8);
					maxU = (double)icon.getMaxU();

					Utils.drawCrossSquaresAlongYRotated(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}

				if (flag5)
				{
					minX = ((double)x + 7*d);
					maxX = ((double)x + 9*d);
					minY = ((double)y + 8*d);
					maxY = ((double)y + 16*d);
					minZ = ((double)z + 7*d);
					maxZ = ((double)z + 9*d);

					minU = (double)icon.getMinU();
					maxU = (double)icon.getInterpolatedU(8);

					Utils.drawCrossSquaresAlongYRotated(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
				}
			}
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) {return false;}

	@Override
	public int getRenderId()
	{
		return id;
	}
}
