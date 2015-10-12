package growthcraft.apples.renderer;

import growthcraft.apples.block.BlockApple;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderApple implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		final Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		final int meta = 0;
		final IIcon icon = BlockApple.tex[meta];
		final double d = 0.0625D;
		final double x = 0.0D;
		final double y = 0.0D;
		final double z = 0.0D;

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

		// Size // 0, 1, 2
		final int i0 = 1 * meta;
		// 4, 5, 6
		final double d0 = 4.0D + (double)i0;
		final double d1 = d0*d;

		// Coordinates
		double minX = ((double)x + 8*d) - d1/2;
		double maxX = minX + d1;
		double maxY = (double)y + 15*d;
		double minY = maxY - d1;
		double minZ = ((double)z + 8*d) - d1/2;
		double maxZ = minZ + d1;

		// Sides - UV
		double minU = (double)icon.getMinU();
		double maxU = (double)icon.getInterpolatedU(d0);
		double minV = (double)icon.getMinV();
		double maxV = (double)icon.getInterpolatedV(d0);

		//Sides - Vertices
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
		tessellator.draw();
		//
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
		tessellator.draw();
		//
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
		tessellator.draw();
		//
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
		tessellator.draw();

		// Top - UV
		minU = (double)icon.getInterpolatedU(6);
		maxU = (double)icon.getInterpolatedU(d0 + 6);
		minV = (double)icon.getMinV();
		maxV = (double)icon.getInterpolatedV(d0);

		// Top - Vertices
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);
		tessellator.draw();

		// Bottom - UV
		minU = (double)icon.getMinU();
		maxU = (double)icon.getInterpolatedU(d0);
		minV = (double)icon.getInterpolatedV(6);
		maxV = (double)icon.getInterpolatedV(d0 + 6);

		// Bottom - Vertices
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, minY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
		tessellator.draw();

		// Stem - Coordinates
		minX = (double)x + 7.5*d;
		maxX = (double)x + 8.5*d;
		minY = (double)y + 15*d;
		maxY = (double)y + 17*d;
		minZ = (double)z + 7.5*d;
		maxZ = (double)z + 8.5*d;

		// Stem - UV
		minU = (double)icon.getInterpolatedU(13);
		maxU = (double)icon.getMaxU();
		minV = (double)icon.getMinV();
		maxV = (double)icon.getInterpolatedV(3);

		// Stem - Vertices
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.5F, 0.0F, 0.5F);
		tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
		tessellator.draw();
		//
		tessellator.startDrawingQuads();
		tessellator.setNormal(-0.5F, 0.0F, -0.5F);
		tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
		tessellator.draw();
		//
		tessellator.startDrawingQuads();
		tessellator.setNormal(-0.5F, 0.0F, 0.5F);
		tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
		tessellator.draw();
		//
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.5F, 0.0F, -0.5F);
		tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
		tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			final Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			final int meta = MathHelper.clamp_int(world.getBlockMetadata(x, y, z), 0, 2);
			final IIcon icon = BlockApple.tex[meta];
			final double d = 0.0625D;

			//Size
			// 0, 1, 2
			final int i0 = 1 * meta;
			// 4, 5, 6
			final double d0 = 4.0D + (double)i0;
			final double d1 = d0*d;

			//Coordinates
			double minX = ((double)x + 8*d) - d1/2;
			double maxX = minX + d1;
			double maxY = (double)y + 15*d;
			double minY = maxY - d1;
			double minZ = ((double)z + 8*d) - d1/2;
			double maxZ = minZ + d1;

			//Sides - UV
			double minU = (double)icon.getMinU();
			double maxU = (double)icon.getInterpolatedU(d0);
			double minV = (double)icon.getMinV();
			double maxV = (double)icon.getInterpolatedV(d0);

			//Sides - Vertices
			tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
			tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
			tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
			tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
			//
			tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
			tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
			tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
			tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
			//
			tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
			tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
			tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
			//
			tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
			tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
			tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);

			//Top - UV
			minU = (double)icon.getInterpolatedU(6);
			maxU = (double)icon.getInterpolatedU(d0 + 6);
			minV = (double)icon.getMinV();
			maxV = (double)icon.getInterpolatedV(d0);

			// Top - Vertices
			tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
			tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
			tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);

			//Bottom - UV
			minU = (double)icon.getMinU();
			maxU = (double)icon.getInterpolatedU(d0);
			minV = (double)icon.getInterpolatedV(6);
			maxV = (double)icon.getInterpolatedV(d0 + 6);

			// Bottom - Vertices
			tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
			tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
			tessellator.addVertexWithUV(minX, minY, minZ, minU, minV);
			tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);

			//Stem - Coordinates
			minX = (double)x + 7.5*d;
			maxX = (double)x + 8.5*d;
			minY = (double)y + 15*d;
			maxY = (double)y + 17*d;
			minZ = (double)z + 7.5*d;
			maxZ = (double)z + 8.5*d;

			//Stem - UV
			minU = (double)icon.getInterpolatedU(13);
			maxU = (double)icon.getMaxU();
			minV = (double)icon.getMinV();
			maxV = (double)icon.getInterpolatedV(3);

			//Stem - Vertices
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
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID) { return true; }

	@Override
	public int getRenderId()
	{
		return id;
	}

}
