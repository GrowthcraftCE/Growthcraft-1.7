package growthcraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public interface IPaddy {

    @Nonnull
    Block getFluidBlock();

    @Nonnull
    Fluid getFillingFluid();

    int getMaxPaddyMeta(IBlockAccess world, int x, int y, int z);

    boolean isFilledWithFluid(IBlockAccess world, int x, int y, int z, int meta);

    boolean canConnectPaddyTo(IBlockAccess world, int x, int y, int z, int meta);

    boolean isBelowFillingFluid(IBlockAccess world, int x, int y, int z);
}
