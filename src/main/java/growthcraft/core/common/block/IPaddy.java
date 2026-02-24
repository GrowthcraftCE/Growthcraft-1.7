package growthcraft.core.common.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

public interface IPaddy
{
	@Nonnull public Block getFluidBlock();
	@Nonnull public Fluid getFillingFluid();
	public int getMaxPaddyMeta(IBlockAccess world, int x, int y, int z);
	public boolean isFilledWithFluid(IBlockAccess world, int x, int y, int z, int meta);
	public boolean canConnectPaddyTo(IBlockAccess world, int x, int y, int z, int meta);
	public boolean isBelowFillingFluid(IBlockAccess world, int x, int y, int z);
}
