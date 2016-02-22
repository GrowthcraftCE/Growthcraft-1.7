package growthcraft.cellar.client.render;

import growthcraft.cellar.common.block.BlockFermentBarrel;
import growthcraft.core.util.RenderUtils;

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
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public int getRenderId()
	{
		return RENDER_ID;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == RENDER_ID)
		{
			final BlockFermentBarrel fermentBarrel = (BlockFermentBarrel)block;
			final Tessellator tes = Tessellator.instance;
			final IIcon[] icons = {
				fermentBarrel.getIconByIndex(0),
				fermentBarrel.getIconByIndex(0),
				fermentBarrel.getIconByIndex(0),
				fermentBarrel.getIconByIndex(0),
				fermentBarrel.getIconByIndex(1),
				fermentBarrel.getIconByIndex(1)
			};
			final double d = 0.0625D;
			final float f = 0.125F;
			renderer.uvRotateEast = 3;
			//inner wall
			renderer.setRenderBounds(0*d, 16*d, 1*d, 2*d, 5*d, 11*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 2*d, 3*d, 11*d, 13*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 3*d, 5*d, 13*d, 14*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 5*d, 11*d, 14*d, 15*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 11*d, 13*d, 13*d, 14*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 13*d, 14*d, 11*d, 13*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 14*d, 15*d, 5*d, 11*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds( 0*d, 16*d, 13*d, 14*d,3*d, 5*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 11*d, 13*d, 2*d, 3*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 5*d, 11*d, 1*d, 2*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 3*d, 5*d, 2*d, 3*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(0*d, 16*d, 2*d, 3*d, 3*d, 5*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			//outer wall
			renderer.setRenderBounds(4*d, 12*d, 0*d, 1*d, 5*d, 11*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 1*d, 2*d, 11*d, 13*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 2*d, 3*d, 13*d, 14*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 3*d, 5*d, 14*d, 15*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 5*d, 11*d, 15*d, 16*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 11*d, 13*d, 14*d, 15*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 13*d, 14*d, 13*d, 14*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 14*d, 15*d, 11*d, 13*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 15*d, 16*d, 5*d, 11*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 14*d, 15*d, 3*d, 5*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 13*d, 14*d, 2*d, 3*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 11*d, 13*d, 1*d, 2*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 5*d, 11*d, 0*d, 1*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 3*d, 5*d, 1*d, 2*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 2*d, 3*d, 2*d, 3*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.setRenderBounds(4*d, 12*d, 1*d, 2*d, 3*d, 5*d);
			RenderUtils.renderInventoryBlockOverride(block, renderer, icons, tes);
			renderer.uvRotateEast = 0;

			renderer.setRenderBounds(1*d, 0.0D, 0.0D, 15*d, 1.0D, 1.0D);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			RenderUtils.drawFace(RenderUtils.Face.XPOS, block, renderer, tes, fermentBarrel.getIconByIndex(2), 0.0D, 0.0D, 0.0D);
			RenderUtils.drawFace(RenderUtils.Face.XNEG, block, renderer, tes, fermentBarrel.getIconByIndex(3), 0.0D, 0.0D, 0.0D);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (modelId == RENDER_ID)
		{
			final BlockFermentBarrel fermentBarrel = (BlockFermentBarrel)block;
			final IIcon[] icon = {
				fermentBarrel.getIconByIndex(0),
				fermentBarrel.getIconByIndex(1),
				fermentBarrel.getIconByIndex(2),
				fermentBarrel.getIconByIndex(3)
			};
			final int meta = world.getBlockMetadata(x, y, z);
			final double d = 0.0625D;

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
				renderer.setRenderBounds(1*d, 2*d, 0*d, 16*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 0*d, 16*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 0*d, 16*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 0*d, 16*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 0*d, 16*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 0*d, 16*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d, 15*d, 0*d, 16*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 0*d, 16*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 0*d, 16*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 0*d, 16*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 0*d, 16*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 0*d, 16*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				//outer wall
				renderer.setRenderBounds(0*d, 1*d, 4*d, 12*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(1*d, 2*d, 4*d, 12*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 4*d, 12*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 4*d, 12*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 4*d, 12*d, 15*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 4*d, 12*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 4*d, 12*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d, 15*d, 4*d, 12*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(15*d, 16*d, 4*d, 12*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d, 15*d, 4*d, 12*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 4*d, 12*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 4*d, 12*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 4*d, 12*d, 0*d, 1*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 4*d, 12*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 4*d, 12*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(1*d, 2*d, 4*d, 12*d, 3*d, 5*d);
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
				renderer.setRenderBounds(1*d, 2*d, 5*d, 11*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 11*d, 13*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 13*d, 14*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 14*d, 15*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 13*d, 14*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 11*d, 13*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d, 15*d, 5*d, 11*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 3*d, 5*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 2*d, 3*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 1*d, 2*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 2*d, 3*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 3*d, 5*d, 0*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				//outer wall
				renderer.setRenderBounds(0*d, 1*d, 5*d, 11*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(1*d, 2*d, 11*d, 13*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 13*d, 14*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 14*d, 15*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 15*d, 16*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 14*d, 15*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 13*d, 14*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d, 15*d, 11*d, 13*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(15*d, 16*d, 5*d, 11*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(14*d, 15*d, 3*d, 5*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(13*d, 14*d, 2*d, 3*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(11*d, 13*d, 1*d, 2*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(5*d, 11*d, 0*d, 1*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(3*d, 5*d, 1*d, 2*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(2*d, 3*d, 2*d, 3*d, 4*d, 12*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(1*d, 2*d, 3*d, 5*d, 4*d, 12*d);
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
				renderer.setRenderBounds(0*d, 16*d, 1*d, 2*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 2*d, 3*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 3*d, 5*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 5*d, 11*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 11*d, 13*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 13*d, 14*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 14*d, 15*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds( 0*d, 16*d, 13*d, 14*d,3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 11*d, 13*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 5*d, 11*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 3*d, 5*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0*d, 16*d, 2*d, 3*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				//outer wall
				renderer.setRenderBounds(4*d, 12*d, 0*d, 1*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 1*d, 2*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 2*d, 3*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 3*d, 5*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 5*d, 11*d, 15*d, 16*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 11*d, 13*d, 14*d, 15*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 13*d, 14*d, 13*d, 14*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 14*d, 15*d, 11*d, 13*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 15*d, 16*d, 5*d, 11*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 14*d, 15*d, 3*d, 5*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 13*d, 14*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 11*d, 13*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 5*d, 11*d, 0*d, 1*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 3*d, 5*d, 1*d, 2*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 2*d, 3*d, 2*d, 3*d);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(4*d, 12*d, 1*d, 2*d, 3*d, 5*d);
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
}
