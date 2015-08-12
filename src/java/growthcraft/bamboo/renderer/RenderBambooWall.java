package growthcraft.bamboo.renderer;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.block.BlockBambooWall;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBambooWall implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			Tessellator tessellator = Tessellator.instance;
			int color;
			float r;
			float g;
			float b;

			if (renderer.useInventoryTint)
			{
				color = block.getRenderColor(metadata);

				r = (float)(color >> 16 & 255) / 255.0F;
				g = (float)(color >> 8 & 255) / 255.0F;
				b = (float)(color & 255) / 255.0F;
				GL11.glColor4f(r * 1.0F, g * 1.0F, b * 1.0F, 1.0F);
			}

			renderer.setRenderBoundsFromBlock(block);
			int loop;

			renderer.setRenderBounds(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 0));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 1));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 3));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 4));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(block, 5));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			double x1 = 0.375D;
			double x2 = 0.625D;
			double y1 = 0.0D;
			double y2 = 1.0D;
			double z1 = 0.375D;
			double z2 = 0.625D;
			renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
			renderer.renderStandardBlock(block, x, y, z);

			BlockBambooWall blk = (BlockBambooWall) block;

			int tm;

			Block idXneg = world.getBlock(x - 1, y, z);
			Block idXpos = world.getBlock(x + 1, y, z);
			Block idZneg = world.getBlock(x, y, z - 1);
			Block idZpos = world.getBlock(x, y, z + 1);

			int metaXneg = world.getBlockMetadata(x - 1, y, z);
			int metaXpos = world.getBlockMetadata(x + 1, y, z);
			int metaZneg = world.getBlockMetadata(x, y, z - 1);
			int metaZpos = world.getBlockMetadata(x, y, z + 1);

			boolean flagXneg = blk.canConnectWallTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
			boolean flagXpos = blk.canConnectWallTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
			boolean flagZneg = blk.canConnectWallTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
			boolean flagZpos = blk.canConnectWallTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);


			//XNEG
			if (flagXneg)
			{
				x1 = 0.0D;
				x2 = 0.375D;
				z1 = 0.375D;
				z2 = 0.625D;

				renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (idXneg instanceof BlockDoor)
			{
				if ((metaXneg & 8) > 7)
				{
					metaXneg = world.getBlockMetadata(x - 1, y - 1, z);
				}

				tm = metaXneg & 3;

				if (tm == 1 || tm == 3)
				{
					x1 = 0.0D;
					x2 = 0.375D;
					z1 = 0.375D;
					z2 = 0.625D;

					renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
					renderer.renderStandardBlock(block, x, y, z);

					if (tm == 1)
					{

						x1 = 0.0D;
						x2 = 0.25D;
						z1 = 0.0D;
						z2 = 0.375D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}

					if (tm == 3)
					{

						x1 = 0.0D;
						x2 = 0.25D;
						z1 = 0.625D;
						z2 = 1.0D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}
				}
			}

			//XPOS
			if (flagXpos)
			{
				x1 = 0.625D;
				x2 = 1.0D;
				z1 = 0.375D;
				z2 = 0.625D;

				renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (idXpos instanceof BlockDoor)
			{
				if ((metaXpos & 8) > 7)
				{
					metaXpos = world.getBlockMetadata(x + 1, y - 1, z);
				}

				tm = metaXpos & 3;

				if (tm == 1 || tm == 3)
				{
					x1 = 0.625D;
					x2 = 1.0D;
					z1 = 0.375D;
					z2 = 0.625D;

					renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
					renderer.renderStandardBlock(block, x, y, z);

					if (tm == 1)
					{
						x1 = 0.75D;
						x2 = 1.0D;
						z1 = 0.0D;
						z2 = 0.375D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}

					if (tm == 3)
					{
						x1 = 0.75D;
						x2 = 1.0D;
						z1 = 0.625D;
						z2 = 1.0D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}
				}
			}

			//ZNEG
			if (flagZneg)
			{
				x1 = 0.375D;
				x2 = 0.625D;
				z1 = 0.0D;
				z2 = 0.375D;

				renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (idZneg instanceof BlockDoor)
			{
				if ((metaZneg & 8) > 7)
				{
					metaZneg = world.getBlockMetadata(x, y - 1, z - 1);
				}

				tm = metaZneg & 3;

				if (tm == 0 || tm == 2)
				{
					x1 = 0.375D;
					x2 = 0.625D;
					z1 = 0.0D;
					z2 = 0.375D;

					renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
					renderer.renderStandardBlock(block, x, y, z);

					if (tm == 0)
					{
						x1 = 0.0D;
						x2 = 0.375D;
						z1 = 0.0D;
						z2 = 0.25D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}

					if (tm == 2)
					{
						x1 = 0.625D;
						x2 = 1.0D;
						z1 = 0.0D;
						z2 = 0.25D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}
				}
			}

			//ZPOS
			if (flagZpos)
			{
				x1 = 0.375D;
				x2 = 0.625D;
				z1 = 0.625D;
				z2 = 1.0D;

				renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (idZpos instanceof BlockDoor)
			{
				if ((metaZpos & 8) > 7)
				{
					metaZpos = world.getBlockMetadata(x, y - 1, z + 1);
				}

				tm = metaZpos & 3;

				if (tm == 0 || tm == 2)
				{
					x1 = 0.375D;
					x2 = 0.625D;
					z1 = 0.625D;
					z2 = 1.0D;

					renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
					renderer.renderStandardBlock(block, x, y, z);

					if (tm == 0)
					{
						x1 = 0.0D;
						x2 = 0.375D;
						z1 = 0.75D;
						z2 = 1.0D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}

					if (tm == 2)
					{
						x1 = 0.625D;
						x2 = 1.0D;
						z1 = 0.75D;
						z2 = 1.0D;

						renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
						renderer.renderStandardBlock(block, x, y, z);
					}
				}
			}

			blk.setBlockBoundsBasedOnState(world, x, y, z);
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return id;
	}
}
