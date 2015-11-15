package growthcraft.cellar.client.render;

import org.lwjgl.opengl.GL11;

import growthcraft.cellar.client.render.model.ModelFermentJar;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.block.BlockFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.core.util.BoundUtils;
import growthcraft.core.util.ColorUtils;
import growthcraft.core.util.RenderUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

public class RenderFermentJar implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	public static float[] innerBounds;
	static
	{
		innerBounds = BoundUtils.newCubeToBounds(5f, 1f, 5f, 22f, 22f, 22f);
		BoundUtils.scaleBounds(innerBounds, 1f / 32f, innerBounds);
	}

	private float[] tempFloatColor = new float[3];

	public int getRenderId()
	{
		return RENDER_ID;
	}

	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return;
		if (block instanceof BlockFermentJar)
		{
			renderer.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
			RenderUtils.startInventoryRender();
			{
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				Minecraft.getMinecraft().renderEngine.bindTexture(GrcCellarResources.INSTANCE.textureFermentJar);
				GrcCellarResources.INSTANCE.modelFermentJar.renderForInventory(ModelFermentJar.SCALE);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			}
			RenderUtils.endInventoryRender();
		}
	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (RENDER_ID != modelId) return false;

		// This only draws in the fluid inside the Jar, the jar itself is a model
		if (block instanceof BlockFermentJar)
		{
			final Tessellator tes = Tessellator.instance;
			final BlockFermentJar fermentJar = (BlockFermentJar)block;
			final TileEntityFermentJar te = fermentJar.getTileEntity(world, x, y, z);
			final Fluid fluid = te.getFluid(0);

			if (fluid != null)
			{
				final double fluidRate = (double)te.getFluidAmountRate(0);
				final double bx1 = (double)innerBounds[0];
				final double by1 = (double)innerBounds[1];
				final double bz1 = (double)innerBounds[2];
				final double bx2 = (double)innerBounds[3];
				final double by2 = (double)innerBounds[4];
				final double bz2 = (double)innerBounds[5];
				final IIcon icon = fluid.getIcon();

				if (icon != null)
				{
					final int color = fluid.getColor();
					ColorUtils.rgb24FloatArray(tempFloatColor, color);
					tes.setColorOpaque_F(tempFloatColor[0], tempFloatColor[1], tempFloatColor[2]);

					renderer.setRenderBounds(bx1, by1, bz1, bx2, by1 + (by2 - by1) * fluidRate, bz2);
					{
						renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, icon);
						renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, icon);
						renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, icon);
						renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, icon);
						renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, icon);
						renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, icon);
					}
					renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
					tes.setColorOpaque_F(1f, 1f, 1f);
				}
			}
		}
		return true;
	}
}
