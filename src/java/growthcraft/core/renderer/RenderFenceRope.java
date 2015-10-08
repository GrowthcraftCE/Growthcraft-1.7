package growthcraft.core.renderer;

import growthcraft.core.block.BlockFenceRope;
import growthcraft.core.utils.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderFenceRope implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			Tessellator tes = Tessellator.instance;
			double d = 0.0625D;
			float f = 0.0F;
			renderer.setRenderBounds(6*d, 0.0D, 6*d, 10*d, 1.0D, 10*d);
			RenderUtils.drawInventoryBlock_icon(block, renderer, BlockFenceRope.tex[0], tes);
			tes.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			IIcon icon = BlockFenceRope.tex[1];

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			//Coordinates
			double minX = ((double)5*d);
			double maxX = ((double)11*d);
			double minY = ((double)4*d);
			double maxY = ((double)12*d);
			double minZ = ((double)5*d);
			double maxZ = ((double)11*d);

			//ZPOS - UV
			double minU = (double)icon.getMinU();
			double maxU = (double)icon.getInterpolatedU(6);
			double minV = (double)icon.getMinV();
			double maxV = (double)icon.getInterpolatedV(8);

			tes.startDrawingQuads();
			tes.addTranslation(0.0F, 0.0F, -f); tes.setNormal(0.0F, 0.0F, 1.0F); tes.addTranslation(0.0F, 0.0F, f);
			//ZPOS - Vertices
			//tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? light : block.getMixedBrightnessForBlock(world, x, y, z + 1));
			tes.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
			tes.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
			tes.addVertexWithUV(minX, maxY, maxZ, minU, minV);
			tes.addVertexWithUV(minX, minY, maxZ, minU, maxV);
			tes.draw();

			//XPOS - UV
			minU = (double)icon.getMinU();
			maxU = (double)icon.getInterpolatedU(6);
			minV = (double)icon.getInterpolatedV(8);
			maxV = (double)icon.getMaxV();

			tes.startDrawingQuads();
			tes.addTranslation(-f, 0.0F, 0.0F); tes.setNormal(1.0F, 0.0F, 0.0F); tes.addTranslation(f, 0.0F, 0.0F);
			//XPOS - Vertices
			//tessellator.setBrightness(renderer.renderMaxX < 1.0D ? light : block.getMixedBrightnessForBlock(world, x + 1, y, z));
			tes.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
			tes.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
			tes.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
			tes.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
			tes.draw();

			//ZNEG - UV
			minU = (double)icon.getInterpolatedU(10);
			maxU = (double)icon.getMaxU();
			minV = (double)icon.getMinV();
			maxV = (double)icon.getInterpolatedV(8);

			tes.startDrawingQuads();
			tes.addTranslation(0.0F, 0.0F, f); tes.setNormal(0.0F, 0.0F, -1.0F); tes.addTranslation(0.0F, 0.0F, -f);
			//ZNEG - Vertices
			//tessellator.setBrightness(renderer.renderMinZ > 0.0D ? light : block.getMixedBrightnessForBlock(world, x, y, z - 1));
			tes.addVertexWithUV(minX, minY, minZ, maxU, maxV);
			tes.addVertexWithUV(minX, maxY, minZ, maxU, minV);
			tes.addVertexWithUV(maxX, maxY, minZ, minU, minV);
			tes.addVertexWithUV(maxX, minY, minZ, minU, maxV);
			tes.draw();

			//XNEG - UV
			minU = (double)icon.getInterpolatedU(10);
			maxU = (double)icon.getMaxU();
			minV = (double)icon.getInterpolatedV(8);
			maxV = (double)icon.getMaxV();

			tes.startDrawingQuads();
			tes.addTranslation(f, 0.0F, 0.0F); tes.setNormal(-1.0F, 0.0F, 0.0F); tes.addTranslation(-f, 0.0F, 0.0F);
			//XNEG - Vertices
			//tessellator.setBrightness(renderer.renderMinX > 0.0D ? light : block.getMixedBrightnessForBlock(world, x - 1, y, z));
			tes.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
			tes.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
			tes.addVertexWithUV(minX, maxY, minZ, minU, minV);
			tes.addVertexWithUV(minX, minY, minZ, minU, maxV);
			tes.draw();

			icon = BlockFenceRope.tex[2];

			//Top/Bottom - UV
			minU = (double)icon.getMinU();
			maxU = (double)icon.getInterpolatedU(6);
			minV = (double)icon.getMinV();
			maxV = (double)icon.getInterpolatedV(6);

			tes.startDrawingQuads();
			tes.addTranslation(0.0F, -f, 0.0F); tes.setNormal(0.0F, 1.0F, 0.0F); tes.addTranslation(0.0F, f, 0.0F);
			// Top - Vertices
			//tessellator.setBrightness(renderer.renderMaxY < 1.0D ? light : block.getMixedBrightnessForBlock(world, x, y + 1, z));
			tes.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
			tes.addVertexWithUV(minX, maxY, minZ, maxU, minV);
			tes.addVertexWithUV(minX, maxY, maxZ, minU, minV);
			tes.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);
			tes.draw();

			tes.startDrawingQuads();
			tes.addTranslation(0.0F, f, 0.0F); tes.setNormal(0.0F, -1.0F, 0.0F); tes.addTranslation(0.0F, -f, 0.0F);
			// Bottom - Vertices
			//tessellator.setBrightness(renderer.renderMinY > 0.0D ? light : block.getMixedBrightnessForBlock(world, x, y - 1, z));
			tes.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
			tes.addVertexWithUV(minX, minY, maxZ, maxU, minV);
			tes.addVertexWithUV(minX, minY, minZ, minU, minV);
			tes.addVertexWithUV(maxX, minY, minZ, minU, maxV);
			tes.draw();

			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			double d = 0.0625D;
			renderer.setRenderBounds(6*d, 0.0D, 6*d, 10*d, 1.0D, 10*d);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			IIcon icon = BlockFenceRope.tex[1];

			//Coordinates
			double minX = ((double)x + 5*d);
			double maxX = ((double)x + 11*d);
			double minY = ((double)y + 4*d);
			double maxY = ((double)y + 12*d);
			double minZ = ((double)z + 5*d);
			double maxZ = ((double)z + 11*d);

			//ZPOS - UV
			double minU = (double)icon.getMinU();
			double maxU = (double)icon.getInterpolatedU(6);
			double minV = (double)icon.getMinV();
			double maxV = (double)icon.getInterpolatedV(8);

			//ZPOS - Vertices
			//tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? light : block.getMixedBrightnessForBlock(world, x, y, z + 1));
			tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
			tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
			tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
			tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);

			//XPOS - UV
			minU = (double)icon.getMinU();
			maxU = (double)icon.getInterpolatedU(6);
			minV = (double)icon.getInterpolatedV(8);
			maxV = (double)icon.getMaxV();

			//XPOS - Vertices
			//tessellator.setBrightness(renderer.renderMaxX < 1.0D ? light : block.getMixedBrightnessForBlock(world, x + 1, y, z));
			tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
			tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
			tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
			tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);

			//ZNEG - UV
			minU = (double)icon.getInterpolatedU(10);
			maxU = (double)icon.getMaxU();
			minV = (double)icon.getMinV();
			maxV = (double)icon.getInterpolatedV(8);

			//ZNEG - Vertices
			//tessellator.setBrightness(renderer.renderMinZ > 0.0D ? light : block.getMixedBrightnessForBlock(world, x, y, z - 1));
			tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
			tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
			tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);

			//XNEG - UV
			minU = (double)icon.getInterpolatedU(10);
			maxU = (double)icon.getMaxU();
			minV = (double)icon.getInterpolatedV(8);
			maxV = (double)icon.getMaxV();

			//XNEG - Vertices
			//tessellator.setBrightness(renderer.renderMinX > 0.0D ? light : block.getMixedBrightnessForBlock(world, x - 1, y, z));
			tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
			tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
			tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);

			icon = BlockFenceRope.tex[2];

			//Top/Bottom - UV
			minU = (double)icon.getMinU();
			maxU = (double)icon.getInterpolatedU(6);
			minV = (double)icon.getMinV();
			maxV = (double)icon.getInterpolatedV(6);

			// Top - Vertices
			//tessellator.setBrightness(renderer.renderMaxY < 1.0D ? light : block.getMixedBrightnessForBlock(world, x, y + 1, z));
			tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
			tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
			tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);

			// Bottom - Vertices
			//tessellator.setBrightness(renderer.renderMinY > 0.0D ? light : block.getMixedBrightnessForBlock(world, x, y - 1, z));
			tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
			tessellator.addVertexWithUV(minX, minY, minZ, minU, minV);
			tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);

			// Branches

			boolean flag = ((BlockFenceRope)block).canConnectRopeTo(world, x, y, z - 1);
			boolean flag1 = ((BlockFenceRope)block).canConnectRopeTo(world, x, y, z + 1);
			boolean flag2 = ((BlockFenceRope)block).canConnectRopeTo(world, x - 1, y, z);
			boolean flag3 = ((BlockFenceRope)block).canConnectRopeTo(world, x + 1, y, z);

			minV = (double)icon.getInterpolatedV(14);
			maxV = (double)icon.getMaxV();

			if (flag)
			{
				minX = ((double)x + 7*d);
				maxX = ((double)x + 9*d);
				minY = ((double)y + 7*d);
				maxY = ((double)y + 9*d);
				minZ = ((double)z);
				maxZ = ((double)z + 5*d);

				minU = (double)icon.getInterpolatedU(11);
				maxU = (double)icon.getMaxU();

				RenderUtils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}

			if (flag1)
			{
				minX = ((double)x + 7*d);
				maxX = ((double)x + 9*d);
				minY = ((double)y + 7*d);
				maxY = ((double)y + 9*d);
				minZ = ((double)z + 11*d);
				maxZ = ((double)z + 16*d);

				minU = (double)icon.getMinU();
				maxU = (double)icon.getInterpolatedU(5);

				RenderUtils.drawCrossSquaresAlongZ(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}

			if (flag2)
			{
				minX = ((double)x);
				maxX = ((double)x + 5*d);
				minY = ((double)y + 7*d);
				maxY = ((double)y + 9*d);
				minZ = ((double)z + 7*d);
				maxZ = ((double)z + 9*d);

				minU = (double)icon.getInterpolatedU(11);
				maxU = (double)icon.getMaxU();

				RenderUtils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}

			if (flag3)
			{
				minX = ((double)x + 11*d);
				maxX = ((double)x + 16*d);
				minY = ((double)y + 7*d);
				maxY = ((double)y + 9*d);
				minZ = ((double)z + 7*d);
				maxZ = ((double)z + 9*d);

				minU = (double)icon.getMinU();
				maxU = (double)icon.getInterpolatedU(5);

				RenderUtils.drawCrossSquaresAlongX(tessellator, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV);
			}
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) {return true;}

	@Override
	public int getRenderId()
	{
		return id;
	}

}
