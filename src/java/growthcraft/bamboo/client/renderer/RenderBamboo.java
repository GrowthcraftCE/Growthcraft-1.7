package growthcraft.bamboo.client.renderer;

import growthcraft.bamboo.common.block.BlockBambooStalk;
import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.util.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBamboo implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		final Tessellator tessellator = Tessellator.instance;
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
		IIcon icon = BlockBambooStalk.tex[0];
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		RenderUtils.drawFace(RenderUtils.Face.YPOS, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		RenderUtils.drawFace(RenderUtils.Face.YNEG, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);

		final float d = 0.75F;
		icon = BlockBambooStalk.tex[2];
		renderer.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
		RenderUtils.drawFace(RenderUtils.Face.ZNEG, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		RenderUtils.drawFace(RenderUtils.Face.ZPOS, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		RenderUtils.drawFace(RenderUtils.Face.XNEG, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
		RenderUtils.drawFace(RenderUtils.Face.XPOS, block, renderer, tessellator, icon, 0.0D, 0.0D, 0.0D);
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
			final float f = 1.0F;
			final int meta = world.getBlockMetadata(x, y, z);
			int color = 0xFFFFFF;
			if (meta == 0)
			{
				color = block.colorMultiplier(world, x, y, z);
			}
			float f1 = (float)(color >> 16 & 255) / 255.0F;
			float f2 = (float)(color >> 8 & 255) / 255.0F;
			float f3 = (float)(color & 255) / 255.0F;
			float f4;

			if (EntityRenderer.anaglyphEnable)
			{
				final float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
				f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
				final float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
				f1 = f5;
				f2 = f4;
				f3 = f6;
			}

			tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			IIcon icon = BlockBambooStalk.tex[0];
			renderer.renderFaceYPos(block, (double)x, (double)((float)y), (double)z, icon);
			renderer.renderFaceYNeg(block, (double)x, (double)((float)y), (double)z, icon);

			final float d = 0.75F;
			tessellator.setColorOpaque_F(f * f1 * d, f * f2 * d, f * f3 * d);
			renderer.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
			icon = BlockBambooStalk.tex[2];
			renderer.renderFaceZNeg(block, (double)x, (double)((float)y), (double)z, icon);
			renderer.renderFaceZPos(block, (double)x, (double)((float)y), (double)z, icon);
			renderer.renderFaceXNeg(block, (double)x, (double)((float)y), (double)z, icon);
			renderer.renderFaceXPos(block, (double)x, (double)((float)y), (double)z, icon);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

			//Connections
			if (meta != 0)
			{
				if (this.canFence(world, x, y, z - 1))
				{
					renderFence(renderer, world, block, x, y, z, RenderUtils.Face.ZNEG);
				}
				else if (this.canWall(world, x, y, z - 1))
				{
					renderWall(renderer, world, block, x, y, z, RenderUtils.Face.ZNEG);
				}
				else if (this.canDoor(world, x, y, z - 1))
				{
					renderDoor(renderer, world, block, x, y, z, RenderUtils.Face.ZNEG);
				}

				if (this.canFence(world, x, y, z + 1))
				{
					renderFence(renderer, world, block, x, y, z, RenderUtils.Face.ZPOS);
				}
				else if (this.canWall(world, x, y, z + 1))
				{
					renderWall(renderer, world, block, x, y, z, RenderUtils.Face.ZPOS);
				}
				else if (this.canDoor(world, x, y, z + 1))
				{
					renderDoor(renderer, world, block, x, y, z, RenderUtils.Face.ZPOS);
				}

				if (this.canFence(world, x - 1, y, z))
				{
					renderFence(renderer, world, block, x, y, z, RenderUtils.Face.XNEG);
				}
				else if (this.canWall(world, x - 1, y, z))
				{
					renderWall(renderer, world, block, x, y, z, RenderUtils.Face.XNEG);
				}
				else if (this.canDoor(world, x - 1, y, z))
				{
					renderDoor(renderer, world, block, x, y, z, RenderUtils.Face.XNEG);
				}

				if (this.canFence(world, x + 1, y, z))
				{
					renderFence(renderer, world, block, x, y, z, RenderUtils.Face.XPOS);
				}
				else if (this.canWall(world, x + 1, y, z))
				{
					renderWall(renderer, world, block, x, y, z, RenderUtils.Face.XPOS);
				}
				else if (this.canDoor(world, x + 1, y, z))
				{
					renderDoor(renderer, world, block, x, y, z, RenderUtils.Face.XPOS);
				}
			}

			renderer.clearOverrideBlockTexture();
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
		return true;
	}

	private boolean canFence(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == GrowthCraftBamboo.blocks.bambooFence.getBlock() ||
			world.getBlock(x, y, z) == Blocks.fence_gate ||
			world.getBlock(x, y, z) == GrowthCraftBamboo.blocks.bambooFenceGate.getBlock();
	}

	private boolean canWall(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == GrowthCraftBamboo.blocks.bambooWall.getBlock();
	}

	private boolean canDoor(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) instanceof BlockDoor;
	}

	private void renderFence(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, RenderUtils.Face m)
	{
		renderer.setOverrideBlockTexture(BlockBambooStalk.tex[3]);
		double x1 = x;
		double x2 = x + 1.0D;
		double z1 = z;
		double z2 = z + 1.0D;

		double y1 = 0.75D;
		double y2 = 0.9375D;

		if (m == RenderUtils.Face.ZNEG)
		{
			x1 = 0.4375D;
			x2 = 0.5625D;
			z1 = 0.0D;
			z2 = 0.25D;
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			x1 = 0.4375D;
			x2 = 0.5625D;
			z1 = 0.75D;
			z2 = 1.0D;
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			z1 = 0.4375D;
			z2 = 0.5625D;
			x1 = 0.0D;
			x2 = 0.25D;
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			z1 = 0.4375D;
			z2 = 0.5625D;
			x1 = 0.75D;
			x2 = 1.0D;
		}

		renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
		renderer.renderStandardBlock(block, x, y, z);

		y1 = 0.375D;
		y2 = 0.5625D;

		if (m == RenderUtils.Face.ZNEG)
		{
			x1 = 0.4375D;
			x2 = 0.5625D;
			z1 = 0.0D;
			z2 = 0.25D;
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			x1 = 0.4375D;
			x2 = 0.5625D;
			z1 = 0.75D;
			z2 = 1.0D;
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			z1 = 0.4375D;
			z2 = 0.5625D;
			x1 = 0.0D;
			x2 = 0.25D;
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			z1 = 0.4375D;
			z2 = 0.5625D;
			x1 = 0.75D;
			x2 = 1.0D;
		}

		renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.clearOverrideBlockTexture();
	}

	private void renderWall(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, RenderUtils.Face m)
	{
		renderer.setOverrideBlockTexture(BlockBambooStalk.tex[4]);
		double x1 = x;
		double x2 = x + 1.0D;
		double z1 = z;
		double z2 = z + 1.0D;

		final double y1 = 0.0D;
		final double y2 = 1.0D;

		if (m == RenderUtils.Face.ZNEG)
		{
			x1 = 0.375D;
			x2 = 0.625D;
			z1 = 0.0D;
			z2 = 0.25D;
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			x1 = 0.375D;
			x2 = 0.625D;
			z1 = 0.75D;
			z2 = 1.0D;
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			z1 = 0.375D;
			z2 = 0.625D;
			x1 = 0.0D;
			x2 = 0.25D;
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			z1 = 0.375D;
			z2 = 0.625D;
			x1 = 0.75D;
			x2 = 1.0D;
		}

		renderer.setRenderBounds(x1, y1, z1, x2, y2, z2);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.clearOverrideBlockTexture();
	}

	private void renderDoor(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, RenderUtils.Face m)
	{
		renderer.setOverrideBlockTexture(BlockBambooStalk.tex[4]);
		double x1 = x;
		double x2 = x + 1.0D;
		double z1 = z;
		double z2 = z + 1.0D;

		final double y1 = 0.0D;
		final double y2 = 1.0D;

		int tm0;
		int tm;

		if (m == RenderUtils.Face.ZNEG)
		{
			tm0 = world.getBlockMetadata(x, y, z - 1);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x, y - 1, z - 1);
			}
			tm = tm0 & 3;
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
		else if (m == RenderUtils.Face.ZPOS)
		{
			tm0 = world.getBlockMetadata(x, y, z + 1);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x, y - 1, z + 1);
			}

			tm = tm0 & 3;
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
		else if (m == RenderUtils.Face.XNEG)
		{
			tm0 = world.getBlockMetadata(x - 1, y, z);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x - 1, y - 1, z);
			}
			tm = tm0 & 3;
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
		else if (m == RenderUtils.Face.XPOS)
		{
			tm0 = world.getBlockMetadata(x + 1, y, z);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x + 1, y - 1, z);
			}
			tm = tm0 & 3;
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
		renderer.clearOverrideBlockTexture();
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
