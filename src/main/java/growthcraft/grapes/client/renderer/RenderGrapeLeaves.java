package growthcraft.grapes.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import growthcraft.core.util.RenderUtils;
import growthcraft.grapes.common.block.BlockGrapeLeaves;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderGrapeLeaves implements ISimpleBlockRenderingHandler {

    public static final int id = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        if (modelID == id) {
            final Tessellator tes = Tessellator.instance;
            if (renderer.useInventoryTint) {
                final int color = block.getRenderColor(0);
                final float f1 = (float) (color >> 16 & 255) / 255.0F;
                final float f2 = (float) (color >> 8 & 255) / 255.0F;
                final float f3 = (float) (color & 255) / 255.0F;
                GL11.glColor4f(f1, f2, f3, 1.0F);
            }
            RenderUtils.drawInventoryBlock_icon(block, renderer, block.getIcon(0, 0), tes);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
                                    RenderBlocks renderer) {
        if (modelId == id) {
            final BlockGrapeLeaves grapeLeaves = (BlockGrapeLeaves) block;
            final boolean graphicFlag = !Blocks.leaves.isOpaqueCube();
            renderer.renderStandardBlock(block, x, y, z);
            final double d = 0.0625D;

            final Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            final int color = block.colorMultiplier(world, x, y, z);
            final float r = (float) (color >> 16 & 255) / 255.0F;
            final float g = (float) (color >> 8 & 255) / 255.0F;
            final float b = (float) (color & 255) / 255.0F;
            tessellator.setColorOpaque_F(r, g, b);
            IIcon icon = grapeLeaves.getIconByIndex(3);

            double minX;
            double maxX;
            double minY;
            double maxY;
            double minZ;
            double maxZ;

            double minU;
            double maxU;
            double minV;
            double maxV;

            minU = icon.getMinU();
            minV = icon.getMinV();
            maxU = icon.getMaxU();
            maxV = icon.getMaxV();

            minX = (double) x + 0.5D - 0.25D;
            maxX = (double) x + 0.5D + 0.25D;
            minZ = (double) z + 0.5D - 0.5D;
            maxZ = (double) z + 0.5D + 0.5D;
            minY = (double) y - 1.0D;
            tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, minU, minV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
            tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, maxU, minV);
            tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, minU, minV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
            tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, maxU, minV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, minU, minV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, maxU, minV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, minU, minV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, maxU, minV);
            minX = (double) x + 0.5D - 0.5D;
            maxX = (double) x + 0.5D + 0.5D;
            minZ = (double) z + 0.5D - 0.25D;
            maxZ = (double) z + 0.5D + 0.25D;
            tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, minU, minV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, minU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, maxU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, maxU, minV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, minZ, minU, minV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, minZ, minU, maxV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, minZ, maxU, maxV);
            tessellator.addVertexWithUV(minX, minY + 1.0D, minZ, maxU, minV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, minU, minV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, minU, maxV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, maxU, maxV);
            tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, maxU, minV);
            tessellator.addVertexWithUV(minX, minY + 1.0D, maxZ, minU, minV);
            tessellator.addVertexWithUV(minX, minY + 0.0D, maxZ, minU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 0.0D, maxZ, maxU, maxV);
            tessellator.addVertexWithUV(maxX, minY + 1.0D, maxZ, maxU, minV);

            if (graphicFlag) {
                renderer.renderAllFaces = true;

                // Render Ropes
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                icon = grapeLeaves.getIconByIndex(2);

                final boolean flag = ((BlockGrapeLeaves) block).canConnectRopeTo(world, x, y, z - 1);
                final boolean flag1 = ((BlockGrapeLeaves) block).canConnectRopeTo(world, x, y, z + 1);
                final boolean flag2 = ((BlockGrapeLeaves) block).canConnectRopeTo(world, x - 1, y, z);
                final boolean flag3 = ((BlockGrapeLeaves) block).canConnectRopeTo(world, x + 1, y, z);
                final boolean flag4 = ((BlockGrapeLeaves) block).canConnectRopeTo(world, x, y - 1, z);
                final boolean flag5 = ((BlockGrapeLeaves) block).canConnectRopeTo(world, x, y + 1, z);

                minV = icon.getInterpolatedV(14);
                maxV = icon.getMaxV();

                if (flag && flag1) {
                    minX = (double) x + 7 * d;
                    maxX = (double) x + 9 * d;
                    minY = (double) y + 7 * d;
                    maxY = (double) y + 9 * d;
                    minZ = z;
                    maxZ = (double) z + 16 * d;

                    minU = icon.getMinU();
                    maxU = icon.getMaxU();

                    RenderUtils.drawCrossSquaresAlongZ(
                            tessellator,
                            minX,
                            maxX,
                            minY,
                            maxY,
                            minZ,
                            maxZ,
                            minU,
                            maxU,
                            minV,
                            maxV);
                } else {
                    if (flag) {
                        minX = (double) x + 7 * d;
                        maxX = (double) x + 9 * d;
                        minY = (double) y + 7 * d;
                        maxY = (double) y + 9 * d;
                        minZ = z;
                        maxZ = (double) z + 8 * d;

                        minU = icon.getInterpolatedU(8);
                        maxU = icon.getMaxU();

                        RenderUtils.drawCrossSquaresAlongZ(
                                tessellator,
                                minX,
                                maxX,
                                minY,
                                maxY,
                                minZ,
                                maxZ,
                                minU,
                                maxU,
                                minV,
                                maxV);
                    }

                    if (flag1) {
                        minX = (double) x + 7 * d;
                        maxX = (double) x + 9 * d;
                        minY = (double) y + 7 * d;
                        maxY = (double) y + 9 * d;
                        minZ = (double) z + 8 * d;
                        maxZ = (double) z + 16 * d;

                        minU = icon.getMinU();
                        maxU = icon.getInterpolatedU(8);

                        RenderUtils.drawCrossSquaresAlongZ(
                                tessellator,
                                minX,
                                maxX,
                                minY,
                                maxY,
                                minZ,
                                maxZ,
                                minU,
                                maxU,
                                minV,
                                maxV);
                    }
                }

                if (flag2 && flag3) {
                    minX = x;
                    maxX = (double) x + 16 * d;
                    minY = (double) y + 7 * d;
                    maxY = (double) y + 9 * d;
                    minZ = (double) z + 7 * d;
                    maxZ = (double) z + 9 * d;

                    minU = icon.getMinU();
                    maxU = icon.getMaxU();

                    RenderUtils.drawCrossSquaresAlongX(
                            tessellator,
                            minX,
                            maxX,
                            minY,
                            maxY,
                            minZ,
                            maxZ,
                            minU,
                            maxU,
                            minV,
                            maxV);
                } else {
                    if (flag2) {
                        minX = x;
                        maxX = (double) x + 8 * d;
                        minY = (double) y + 7 * d;
                        maxY = (double) y + 9 * d;
                        minZ = (double) z + 7 * d;
                        maxZ = (double) z + 9 * d;

                        minU = icon.getInterpolatedU(8);
                        maxU = icon.getMaxU();

                        RenderUtils.drawCrossSquaresAlongX(
                                tessellator,
                                minX,
                                maxX,
                                minY,
                                maxY,
                                minZ,
                                maxZ,
                                minU,
                                maxU,
                                minV,
                                maxV);
                    }

                    if (flag3) {
                        minX = (double) x + 8 * d;
                        maxX = (double) x + 16 * d;
                        minY = (double) y + 7 * d;
                        maxY = (double) y + 9 * d;
                        minZ = (double) z + 7 * d;
                        maxZ = (double) z + 9 * d;

                        minU = icon.getMinU();
                        maxU = icon.getInterpolatedU(8);

                        RenderUtils.drawCrossSquaresAlongX(
                                tessellator,
                                minX,
                                maxX,
                                minY,
                                maxY,
                                minZ,
                                maxZ,
                                minU,
                                maxU,
                                minV,
                                maxV);
                    }
                }

                if (flag4 && flag5) {
                    minX = (double) x + 7 * d;
                    maxX = (double) x + 9 * d;
                    minY = y;
                    maxY = (double) y + 16 * d;
                    minZ = (double) z + 7 * d;
                    maxZ = (double) z + 9 * d;

                    minU = icon.getMinU();
                    maxU = icon.getMaxU();

                    RenderUtils.drawCrossSquaresAlongYRotated(
                            tessellator,
                            minX,
                            maxX,
                            minY,
                            maxY,
                            minZ,
                            maxZ,
                            minU,
                            maxU,
                            minV,
                            maxV);
                } else {
                    if (flag4) {
                        minX = (double) x + 7 * d;
                        maxX = (double) x + 9 * d;
                        minY = y;
                        maxY = (double) y + 8 * d;
                        minZ = (double) z + 7 * d;
                        maxZ = (double) z + 9 * d;

                        minU = icon.getInterpolatedU(8);
                        maxU = icon.getMaxU();

                        RenderUtils.drawCrossSquaresAlongYRotated(
                                tessellator,
                                minX,
                                maxX,
                                minY,
                                maxY,
                                minZ,
                                maxZ,
                                minU,
                                maxU,
                                minV,
                                maxV);
                    }

                    if (flag5) {
                        minX = (double) x + 7 * d;
                        maxX = (double) x + 9 * d;
                        minY = (double) y + 8 * d;
                        maxY = (double) y + 16 * d;
                        minZ = (double) z + 7 * d;
                        maxZ = (double) z + 9 * d;

                        minU = icon.getMinU();
                        maxU = icon.getInterpolatedU(8);

                        RenderUtils.drawCrossSquaresAlongYRotated(
                                tessellator,
                                minX,
                                maxX,
                                minY,
                                maxY,
                                minZ,
                                maxZ,
                                minU,
                                maxU,
                                minV,
                                maxV);
                    }
                }

                renderer.renderAllFaces = false;
            }
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelID) {
        return true;
    }

    @Override
    public int getRenderId() {
        return id;
    }
}
