package growthcraft.core.block;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public interface IBlockRope
{
	/**
	 * Example usage:
	 *
	 * 	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z)
	 * {
	 * 	 if (Block.blocksList[world.getBlockId(x, y, z)] instanceof IBlockRope)
	 * 	 {
	 * 	 	 return true;
	 * 	 }
	 * 	 return false;
	 * }
	 */
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z);
}
