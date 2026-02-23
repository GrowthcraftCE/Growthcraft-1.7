package growthcraft.core.common.block;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatableBlock
{
	/**
	 * Can this block be rotated?
	 *
	 * @param world - world the block is to be rotated in
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @param side - side that block should be rotated from
	 * @return true, if it can be rotated, false otherwise
	 */
	boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side);
}
