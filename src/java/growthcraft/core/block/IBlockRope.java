package growthcraft.core.block;

import net.minecraft.world.IBlockAccess;

public interface IBlockRope
{
	/**
	 * @param world - world to interact with
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return can the rope be connected at this point?
	 *
	 * <pre>
	 * {@code
	 *	 public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z)
	 *	 {
	 *	 	 if (Block.blocksList[world.getBlockId(x, y, z)] instanceof IBlockRope)
	 *	 	 {
	 *	 	 	 return true;
	 *	 	 }
	 *	 	 return false;
	 *	 }
	 * }
	 * </pre>
	 */
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z);
}
