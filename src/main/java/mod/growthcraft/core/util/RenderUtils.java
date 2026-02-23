package growthcraft.core.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class RenderUtils
{
	public static enum Face
	{
		XPOS,
		XNEG,
		YPOS,
		YNEG,
		ZPOS,
		ZNEG;

		public static final Face[] FACES = { YNEG, YPOS, ZNEG, ZPOS, XNEG, XPOS };
	}

	private RenderUtils() {}

	public static void resetColor()
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void resetScale()
	{
		GL11.glScalef(1.0f, 1.0f, 1.0f);
	}

	public static void startInventoryRender()
	{
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		resetColor();
		resetScale();
	}

	public static void endInventoryRender()
	{
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	public static void drawCubeFace(Face face, Block block, RenderBlocks renderer, Tessellator tes, IIcon icon, double i, double j, double k)
	{
		final float f = 0.0F;
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
	}

	public static void drawFace(Face face, Block block, RenderBlocks renderer, Tessellator tessellator, IIcon icon, double i, double j, double k)
	{
		tessellator.startDrawingQuads();
		drawCubeFace(face, block, renderer, tessellator, icon, i, j, k);
		tessellator.draw();
	}

	public static void renderInventoryBlockOverride(Block block, RenderBlocks renderer, IIcon[] icon, Tessellator tessellator)
	{
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		drawCubeFace(Face.YNEG, block, renderer, tessellator, icon[0], 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.YPOS, block, renderer, tessellator, icon[1], 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.ZNEG, block, renderer, tessellator, icon[2], 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.ZPOS, block, renderer, tessellator, icon[3], 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.XNEG, block, renderer, tessellator, icon[4], 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.XPOS, block, renderer, tessellator, icon[5], 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void renderInventoryBlockFaces(Block block, int meta, RenderBlocks renderer, Tessellator tessellator)
	{
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		drawCubeFace(Face.YNEG, block, renderer, tessellator, block.getIcon(0, meta), 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.YPOS, block, renderer, tessellator, block.getIcon(1, meta), 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.ZNEG, block, renderer, tessellator, block.getIcon(2, meta), 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.ZPOS, block, renderer, tessellator, block.getIcon(3, meta), 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.XNEG, block, renderer, tessellator, block.getIcon(4, meta), 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.XPOS, block, renderer, tessellator, block.getIcon(5, meta), 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void renderInventoryBlock(Block block, int meta, RenderBlocks renderer, Tessellator tessellator)
	{
		renderInventoryBlockFaces(block, meta, renderer, tessellator);
	}

	public static void drawInventoryBlock_icon(Block block, RenderBlocks renderer, IIcon icon, Tessellator tessellator)
	{
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		drawCubeFace(Face.YNEG, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.YPOS, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.ZNEG, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.ZPOS, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.XNEG, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		drawCubeFace(Face.XPOS, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
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
