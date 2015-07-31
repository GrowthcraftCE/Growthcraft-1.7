package growthcraft.bamboo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderBambooFence implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			Tessellator tessellator = Tessellator.instance;
			IIcon[] icon = new IIcon[6];
			int color;
			float r;
			float g;
			float b;
			float f2;

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

			for (loop = 0; loop < 4; ++loop)
			{
				f2 = 0.125F;

				if (loop == 0)
				{
					renderer.setRenderBounds((double)(0.5F - f2), 0.0D, 0.0D, (double)(0.5F + f2), 1.0D, (double)(f2 * 2.0F));
				}

				if (loop == 1)
				{
					renderer.setRenderBounds((double)(0.5F - f2), 0.0D, (double)(1.0F - f2 * 2.0F), (double)(0.5F + f2), 1.0D, 1.0D);
				}

				f2 = 0.0625F;

				if (loop == 2)
				{
					renderer.setRenderBounds((double)(0.5F - f2), (double)(1.0F - f2 * 3.0F), (double)(-f2 * 2.0F), (double)(0.5F + f2), (double)(1.0F - f2), (double)(1.0F + f2 * 2.0F));
				}

				if (loop == 3)
				{
					renderer.setRenderBounds((double)(0.5F - f2), (double)(0.5F - f2 * 3.0F), (double)(-f2 * 2.0F), (double)(0.5F + f2), (double)(0.5F - f2), (double)(1.0F + f2 * 2.0F));
				}

				if (loop == 0 || loop == 1)
				{
					renderer.uvRotateTop = 1;
					renderer.uvRotateBottom = 1;
					icon[0] = BlockBambooFence.tex[0];
					icon[1] = BlockBambooFence.tex[0];
					icon[2] = BlockBambooFence.tex[1];
					icon[3] = BlockBambooFence.tex[1];
					icon[4] = BlockBambooFence.tex[1];
					icon[5] = BlockBambooFence.tex[1];
				}

				if (loop == 2 || loop == 3)
				{
					renderer.uvRotateTop = 0;
					renderer.uvRotateBottom = 0;
					for (int l = 0; l < 6; ++l)
					{
						icon[l] = BlockBambooFence.tex[2];
					}
				}

				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon[0]);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon[1]);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon[2]);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon[3]);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon[4]);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon[5]);
				tessellator.draw();
				renderer.uvRotateBottom = 0;
				renderer.uvRotateTop = 0;
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			}

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			boolean flag = false;
			float f = 0.375F;
			float f1 = 0.625F;
			renderer.setRenderBounds((double)f, 0.0D, (double)f, (double)f1, 1.0D, (double)f1);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setOverrideBlockTexture(BlockBambooFence.tex[2]);
			boolean flag1 = false;
			boolean flag2 = false;
			BlockBambooFence blk = (BlockBambooFence) block;

			Block idXneg = world.getBlock(x - 1, y, z);
			Block idXpos = world.getBlock(x + 1, y, z);
			Block idZneg = world.getBlock(x, y, z - 1);
			Block idZpos = world.getBlock(x, y, z + 1);

			int metaXneg = world.getBlockMetadata(x - 1, y, z);
			int metaXpos = world.getBlockMetadata(x + 1, y, z);
			int metaZneg = world.getBlockMetadata(x, y, z - 1);
			int metaZpos = world.getBlockMetadata(x, y, z + 1);

			if ((blk.canConnectFenceTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0)) || (blk.canConnectFenceTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1)))
			{
				flag1 = true;
			}

			if ((blk.canConnectFenceTo(world, x, y, z - 1)|| (idZneg instanceof BlockStairs && world.getBlockMetadata(x, y, z - 1) == 2)) || (blk.canConnectFenceTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && world.getBlockMetadata(x, y, z + 1) == 3)))
			{
				flag2 = true;
			}

			boolean flagXneg = blk.canConnectFenceTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
			boolean flagXpos = blk.canConnectFenceTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
			boolean flagZneg = blk.canConnectFenceTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
			boolean flagZpos = blk.canConnectFenceTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);

			f = 0.4375F;
			f1 = 0.5625F;
			float f2 = 0.75F;
			float f3 = 0.9375F;
			float f4 = flagXneg ? 0.0F : f;
			float f5 = flagXpos ? 1.0F : f1;
			float f6 = flagZneg ? 0.0F : f;
			float f7 = flagZpos ? 1.0F : f1;

			if (flag1)
			{
				renderer.setRenderBounds((double)f4, (double)f2, (double)f, (double)f5, (double)f3, (double)f1);
				renderer.renderStandardBlock(block, x, y, z);
			}

			if (flag2)
			{
				renderer.setRenderBounds((double)f, (double)f2, (double)f6, (double)f1, (double)f3, (double)f7);
				renderer.renderStandardBlock(block, x, y, z);
			}

			f2 = 0.375F;
			f3 = 0.5625F;

			if (flag1)
			{
				renderer.setRenderBounds((double)f4, (double)f2, (double)f, (double)f5, (double)f3, (double)f1);
				renderer.renderStandardBlock(block, x, y, z);
			}

			if (flag2)
			{
				renderer.setRenderBounds((double)f, (double)f2, (double)f6, (double)f1, (double)f3, (double)f7);
				renderer.renderStandardBlock(block, x, y, z);
			}

			renderer.clearOverrideBlockTexture();
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
