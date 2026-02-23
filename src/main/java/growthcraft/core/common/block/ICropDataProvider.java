package growthcraft.core.common.block;

import net.minecraft.world.IBlockAccess;

/**
 * Waila data provider for crop blocks
 */
public interface ICropDataProvider
{
	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta);
}
