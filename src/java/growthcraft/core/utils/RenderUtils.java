package growthcraft.core.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class RenderUtils
{
	private RenderUtils() {}

	public static enum Faces
	{
		XPOS,
		XNEG,
		YPOS,
		YNEG,
		ZPOS,
		ZNEG;
	}

	public static void drawFace(Faces face, Block block, RenderBlocks renderer, Tessellator tes, IIcon icon, double i, double j, double k)
	{
		final float f = 0.0F;
		tes.startDrawingQuads();
		switch (face)
		{
			case XPOS:
			{
				tes.addTranslation(-f, 0.0F, 0.0F);
				tes.setNormal(1.0F, 0.0F, 0.0F);
				tes.addTranslation(f, 0.0F, 0.0F);
				renderer.renderFaceXPos(block, i, j, k, icon);
				break;
			}
			case XNEG:
			{
				tes.addTranslation(f, 0.0F, 0.0F);
				tes.setNormal(-1.0F, 0.0F, 0.0F);
				tes.addTranslation(-f, 0.0F, 0.0F);
				renderer.renderFaceXNeg(block, i, j, k, icon);
				break;
			}
			case YPOS:
			{
				tes.addTranslation(0.0F, -f, 0.0F);
				tes.setNormal(0.0F, 1.0F, 0.0F);
				tes.addTranslation(0.0F, f, 0.0F);
				renderer.renderFaceYPos(block, i, j, k, icon);
				break;
			}
			case YNEG:
			{
				tes.addTranslation(0.0F, f, 0.0F);
				tes.setNormal(0.0F, -1.0F, 0.0F);
				tes.addTranslation(0.0F, -f, 0.0F);
				renderer.renderFaceYNeg(block, i, j, k, icon);
				break;
			}
			case ZPOS:
			{
				tes.addTranslation(0.0F, 0.0F, -f);
				tes.setNormal(0.0F, 0.0F, 1.0F);
				tes.addTranslation(0.0F, 0.0F, f);
				renderer.renderFaceZPos(block, i, j, k, icon);
				break;
			}
			case ZNEG:
			{
				tes.addTranslation(0.0F, 0.0F, f);
				tes.setNormal(0.0F, 0.0F, -1.0F);
				tes.addTranslation(0.0F, 0.0F, -f);
				renderer.renderFaceZNeg(block, i, j, k, icon);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Invalid face value " + face + ".");
			}
		}
		tes.draw();
	}

	public static void drawInventoryBlock(Block block, RenderBlocks renderer, IIcon[] icon, Tessellator tes)
	{
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		drawFace(Faces.YNEG, block, renderer, tes, icon[0], 0.0D, 0.0D, 0.0D);
		drawFace(Faces.YPOS, block, renderer, tes, icon[1], 0.0D, 0.0D, 0.0D);
		drawFace(Faces.ZNEG, block, renderer, tes, icon[2], 0.0D, 0.0D, 0.0D);
		drawFace(Faces.ZPOS, block, renderer, tes, icon[3], 0.0D, 0.0D, 0.0D);
		drawFace(Faces.XNEG, block, renderer, tes, icon[4], 0.0D, 0.0D, 0.0D);
		drawFace(Faces.XPOS, block, renderer, tes, icon[5], 0.0D, 0.0D, 0.0D);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void drawInventoryBlock_icon(Block block, RenderBlocks renderer, IIcon icon, Tessellator tes)
	{
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		drawFace(Faces.YNEG, block, renderer, tes, icon, 0.0D, 0.0D, 0.0D);
		drawFace(Faces.YPOS, block, renderer, tes, icon, 0.0D, 0.0D, 0.0D);
		drawFace(Faces.ZNEG, block, renderer, tes, icon, 0.0D, 0.0D, 0.0D);
		drawFace(Faces.ZPOS, block, renderer, tes, icon, 0.0D, 0.0D, 0.0D);
		drawFace(Faces.XNEG, block, renderer, tes, icon, 0.0D, 0.0D, 0.0D);
		drawFace(Faces.XPOS, block, renderer, tes, icon, 0.0D, 0.0D, 0.0D);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void drawCrossSquaresAlongX(Tessellator tessellator, double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minU, double maxU, double minV, double maxV)
	{
		//
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
		//
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
	}

	public static void drawCrossSquaresAlongY(Tessellator tessellator, double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minU, double maxU, double minV, double maxV)
	{
		//
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
		//
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
	}

	public static void drawCrossSquaresAlongYRotated(Tessellator tessellator, double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minU, double maxU, double minV, double maxV)
	{
		//
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, maxV);
		//
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, maxV);
	}

	public static void drawCrossSquaresAlongZ(Tessellator tessellator, double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minU, double maxU, double minV, double maxV)
	{
		//
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
		//
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
		//
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
	}
}
