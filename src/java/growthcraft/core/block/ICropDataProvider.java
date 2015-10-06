package growthcraft.core.block;

import net.minecraft.world.IBlockAccess;

public interface ICropDataProvider
{
	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta);
}
