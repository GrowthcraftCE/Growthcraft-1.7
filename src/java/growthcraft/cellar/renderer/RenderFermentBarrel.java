package growthcraft.cellar.renderer;

import growthcraft.cellar.block.BlockFermentBarrel;
import growthcraft.core.Utils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderFermentBarrel implements ISimpleBlockRenderingHandler
{
	public static int id = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == id)
		{
			Tessellator tes = Tessellator.instance;
			IIcon[] icon = {BlockFermentBarrel.tex[0], BlockFermentBarrel.tex[0], BlockFermentBarrel.tex[0], BlockFermentBarrel.tex[0], BlockFermentBarrel.tex[1], BlockFermentBarrel.tex[1]};
			double d = 0.0625D;
			float f = 0.125F;
			renderer.uvRotateEast = 3;
			//inner wall
			setRenderBounds(renderer, 0*d, 16*d, 1*d, 2*d, 5*d, 11*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 2*d, 3*d, 11*d, 13*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 3*d, 5*d, 13*d, 14*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 5*d, 11*d, 14*d, 15*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 11*d, 13*d, 13*d, 14*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 13*d, 14*d, 11*d, 13*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 14*d, 15*d, 5*d, 11*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer,  0*d, 16*d, 13*d, 14*d,3*d, 5*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 11*d, 13*d, 2*d, 3*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 5*d, 11*d, 1*d, 2*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 3*d, 5*d, 2*d, 3*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 0*d, 16*d, 2*d, 3*d, 3*d, 5*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			//outer wall
			setRenderBounds(renderer, 4*d, 12*d, 0*d, 1*d, 5*d, 11*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 1*d, 2*d, 11*d, 13*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 2*d, 3*d, 13*d, 14*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 3*d, 5*d, 14*d, 15*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 5*d, 11*d, 15*d, 16*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 11*d, 13*d, 14*d, 15*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 13*d, 14*d, 13*d, 14*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 14*d, 15*d, 11*d, 13*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 15*d, 16*d, 5*d, 11*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 14*d, 15*d, 3*d, 5*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 13*d, 14*d, 2*d, 3*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 11*d, 13*d, 1*d, 2*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 5*d, 11*d, 0*d, 1*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 3*d, 5*d, 1*d, 2*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 2*d, 3*d, 2*d, 3*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			setRenderBounds(renderer, 4*d, 12*d, 1*d, 2*d, 3*d, 5*d);
			Utils.drawInventoryBlock(block, renderer, icon, tes);
			renderer.uvRotateEast = 0;

			renderer.setRenderBounds(1*d, 0.0D, 0.0D, 15*d, 1.0D, 1.0D);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			Utils.drawFace("xpos", block, renderer, tes, BlockFermentBarrel.tex[2], 0.0D, 0.0D, 0.0D);
			Utils.drawFace("xneg", block, renderer, tes, BlockFermentBarrel.tex[3], 0.0D, 0.0D, 0.0D);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == id)
		{
			IIcon[] icon = {BlockFermentBarrel.tex[0], BlockFermentBarrel.tex[1], BlockFermentBarrel.tex[2], BlockFermentBarrel.tex[3]};
			int meta = world.getBlockMetadata(x, y, z);
			double d = 0.0625D;

			if (meta == 0 || meta == 1)
			{
				if (meta == 0)
				{
					renderer.uvRotateEast = 2;
					renderer.uvRotateWest = 1;
					renderer.uvRotateSouth = 2;
					renderer.uvRotateNorth = 1;
				}
				else if (meta == 1)
				{
					renderer.uvRotateEast = 1;
					renderer.uvRotateWest = 2;
					renderer.uvRotateSouth = 1;
					renderer.uvRotateNorth = 2;
				}
				//inner wall
				setRenderBounds(renderer, 1*d, 2*d, 0*d, 16*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 0*d, 16*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 0*d, 16*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 0*d, 16*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 0*d, 16*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 0*d, 16*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 14*d, 15*d, 0*d, 16*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 0*d, 16*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 0*d, 16*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 0*d, 16*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 0*d, 16*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 0*d, 16*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				//outer wall
				setRenderBounds(renderer, 0*d, 1*d, 4*d, 12*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 1*d, 2*d, 4*d, 12*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 4*d, 12*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 4*d, 12*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 4*d, 12*d, 15*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 4*d, 12*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 4*d, 12*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 14*d, 15*d, 4*d, 12*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 15*d, 16*d, 4*d, 12*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 14*d, 15*d, 4*d, 12*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 4*d, 12*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 4*d, 12*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 4*d, 12*d, 0*d, 1*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 4*d, 12*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 4*d, 12*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 1*d, 2*d, 4*d, 12*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (meta == 2 || meta == 3)
			{
				if (meta == 2)
				{
					renderer.uvRotateNorth = 3;
					renderer.uvRotateTop = 2;
					renderer.uvRotateBottom = 1;
				}
				else if (meta == 3)
				{
					renderer.uvRotateSouth = 3;
					renderer.uvRotateTop = 1;
					renderer.uvRotateBottom = 2;
				}
				//inner wall
				setRenderBounds(renderer, 1*d, 2*d, 5*d, 11*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 11*d, 13*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 13*d, 14*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 14*d, 15*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 13*d, 14*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 11*d, 13*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 14*d, 15*d, 5*d, 11*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 3*d, 5*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 2*d, 3*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 1*d, 2*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 2*d, 3*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 3*d, 5*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				//outer wall
				setRenderBounds(renderer, 0*d, 1*d, 5*d, 11*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 1*d, 2*d, 11*d, 13*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 13*d, 14*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 14*d, 15*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 15*d, 16*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 14*d, 15*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 13*d, 14*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 14*d, 15*d, 11*d, 13*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 15*d, 16*d, 5*d, 11*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 14*d, 15*d, 3*d, 5*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 13*d, 14*d, 2*d, 3*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 11*d, 13*d, 1*d, 2*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 5*d, 11*d, 0*d, 1*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 3*d, 5*d, 1*d, 2*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 2*d, 3*d, 2*d, 3*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 1*d, 2*d, 3*d, 5*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else if (meta == 4 || meta == 5)
			{
				if (meta == 4)
				{
					renderer.uvRotateWest = 3;
					renderer.uvRotateTop = 3;
					renderer.uvRotateBottom = 3;
				}
				else if (meta == 5)
				{
					renderer.uvRotateEast = 3;
				}
				//inner wall
				setRenderBounds(renderer, 0*d, 16*d, 1*d, 2*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 2*d, 3*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 3*d, 5*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 5*d, 11*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 11*d, 13*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 13*d, 14*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 14*d, 15*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer,  0*d, 16*d, 13*d, 14*d,3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 11*d, 13*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 5*d, 11*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 3*d, 5*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 0*d, 16*d, 2*d, 3*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				//outer wall
				setRenderBounds(renderer, 4*d, 12*d, 0*d, 1*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 1*d, 2*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 2*d, 3*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 3*d, 5*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 5*d, 11*d, 15*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 11*d, 13*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 13*d, 14*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 14*d, 15*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 15*d, 16*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 14*d, 15*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 13*d, 14*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 11*d, 13*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 5*d, 11*d, 0*d, 1*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 3*d, 5*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 2*d, 3*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				setRenderBounds(renderer, 4*d, 12*d, 1*d, 2*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
			}

			renderer.uvRotateEast = 0;
			renderer.uvRotateWest = 0;
			renderer.uvRotateSouth = 0;
			renderer.uvRotateNorth = 0;
			renderer.uvRotateTop = 0;
			renderer.uvRotateBottom = 0;

			//inner wall
			if (meta == 0 || meta == 1)
			{
				renderer.setRenderBounds(0.0D, 1*d, 0.0D, 1.0D, 15*d, 1.0D);
				if (meta == 0)
				{
					renderer.renderFaceYNeg(block, x, y, z, icon[2]);
					renderer.renderFaceYPos(block, x, y, z, icon[3]);
				}
				else if (meta == 1)
				{
					renderer.renderFaceYPos(block, x, y, z, icon[2]);
					renderer.renderFaceYNeg(block, x, y, z, icon[3]);
				}
			}
			else if (meta == 2 || meta == 3)
			{
				renderer.setRenderBounds(0.0D, 0.0D, 1*d, 1.0D, 1.0D, 15*d);
				if (meta == 2)
				{
					renderer.renderFaceZNeg(block, x, y, z, icon[2]);
					renderer.renderFaceZPos(block, x, y, z, icon[3]);
				}
				else if (meta == 3)
				{
					renderer.renderFaceZPos(block, x, y, z, icon[2]);
					renderer.renderFaceZNeg(block, x, y, z, icon[3]);
				}
			}
			else if (meta == 4 || meta == 5)
			{
				renderer.setRenderBounds(1*d, 0.0D, 0.0D, 15*d, 1.0D, 1.0D);
				if (meta == 4)
				{
					renderer.renderFaceXNeg(block, x, y, z, icon[2]);
					renderer.renderFaceXPos(block, x, y, z, icon[3]);
				}
				else if (meta == 5)
				{
					renderer.renderFaceXPos(block, x, y, z, icon[2]);
					renderer.renderFaceXNeg(block, x, y, z, icon[3]);
				}
			}


			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderer.clearOverrideBlockTexture();
		}
		return true;
	}

	private void setRenderBounds(RenderBlocks r, double x1, double x2, double y1, double y2, double z1, double z2)
	{
		r.setRenderBounds(x1, y1, z1, x2, y2, z2);
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
