package growthcraft.hops;

import growthcraft.core.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderHops implements ISimpleBlockRenderingHandler 
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		if (modelId == id)
		{
			int meta = world.getBlockMetadata(x, y, z);
			double d = 0.0625D;

			double minX;
			double maxX;
			double minY;
			double maxY;
			double minZ;
			double maxZ;

			double minU;
			double maxU;
			double minV;
			double maxV;

			IIcon icon;

			Tessellator tessellator = Tessellator.instance;
			if (meta == 0 || meta == 1)
			{
				renderer.setOverrideBlockTexture(BlockHops.tex[2]);
				renderer.renderCrossedSquares(block, x, y, z);
				renderer.clearOverrideBlockTexture();
			}

			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			if (meta != 0)
			{
				if (meta == 3)
				{
					icon = BlockHops.tex[3];

					minU = (double)icon.getMinU();
					maxU = (double)icon.getMaxU();
					minV = (double)icon.getMinV();
					maxV = (double)icon.getMaxV();

					minX = (double)x + 0.5D - 0.25D;
					maxX = (double)x + 0.5D + 0.25D;
					minZ = (double)z + 0.5D - 0.5D;
					maxZ = (double)z + 0.5D + 0.5D;
					minY = (double)y;
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, maxU, minV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, maxU, minV);
					minX = (double)x + 0.5D - 0.5D;
					maxX = (double)x + 0.5D + 0.5D;
					minZ = (double)z + 0.5D - 0.25D;
					maxZ = (double)z + 0.5D + 0.25D;
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, maxU, minV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, maxU, minV);
				}

				icon = ((BlockHops)block).getIcon(0, meta);

				minU = (double)icon.getMinU();
				maxU = (double)icon.getMaxU();
				minV = (double)icon.getMinV();
				maxV = (double)icon.getMaxV();

				int color = ((BlockHops)block).colorMultiplier(world, x, y, z);
				float r = (float)(color >> 16 & 255) / 255.0F;
				float g = (float)(color >> 8 & 255) / 255.0F;
				float b = (float)(color & 255) / 255.0F;
				tessellator.setColorOpaque_F(r * 1.0F, g * 1.0F, b * 1.0F);

				if (meta == 1)
				{					
					minX = (double)x + 0.5D - 0.125D;
					maxX = (double)x + 0.5D + 0.125D;
					minZ = (double)z + 0.5D - 0.25D;
					maxZ = (double)z + 0.5D + 0.25D;
					minY = (double)y;
					tessellator.addVertexWithUV(minX, minY + 8*d, minZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 8*d, maxZ, maxU, minV);
					tessellator.addVertexWithUV(minX, minY + 8*d, maxZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 8*d, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, maxZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, minZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, maxZ, maxU, minV);
					minX = (double)x + 0.5D - 0.25D;
					maxX = (double)x + 0.5D + 0.25D;
					minZ = (double)z + 0.5D - 0.125D;
					maxZ = (double)z + 0.5D + 0.125D;
					tessellator.addVertexWithUV(minX, minY + 8*d, minZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, minZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 8*d, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, maxZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 8*d, maxZ, maxU, minV);
					tessellator.addVertexWithUV(minX, minY + 8*d, maxZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 8*d, maxZ, maxU, minV);
				}
				else
				{					
					minX = (double)x + 0.5D - 0.25D;
					maxX = (double)x + 0.5D + 0.25D;
					minZ = (double)z + 0.5D - 0.5D;
					maxZ = (double)z + 0.5D + 0.5D;
					minY = (double)y;
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, maxU, minV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, maxU, minV);
					minX = (double)x + 0.5D - 0.5D;
					maxX = (double)x + 0.5D + 0.5D;
					minZ = (double)z + 0.5D - 0.25D;
					maxZ = (double)z + 0.5D + 0.25D;
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, maxU, minV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, maxU, minV);
					tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, minU, minV);
					tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
					tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, maxU, minV);
				}
			}

			//Render Ropes
			renderer.renderAllFaces = true;

			//Render Ropes
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			icon = BlockHops.tex[4];

			boolean flag = ((BlockHops)block).canConnectRopeTo(world, x, y, z - 1);
			boolean flag1 = ((BlockHops)block).canConnectRopeTo(world, x, y, z + 1);
			boolean flag2 = ((BlockHops)block).canConnectRopeTo(world, x - 1, y, z);
			boolean flag3 = ((BlockHops)block).canConnectRopeTo(world, x + 1, y, z);
			boolean flag4 = ((BlockHops)block).canConnectRopeTo(world, x, y - 1, z);
			boolean flag5 = ((BlockHops)block).canConnectRopeTo(world, x, y + 1, z);

			minV = (double)icon.getInterpolatedV(14);
			maxV = (double)icon.getMaxV();

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

			renderer.renderAllFaces = false;
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) { return false; }

	@Override
	public int getRenderId() 
	{
		return id;
	}

}
