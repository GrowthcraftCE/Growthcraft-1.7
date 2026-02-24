package growthcraft.core.common.block;

import net.minecraft.world.World;

/**
 * Interface for blocks that can be dropped as an item, normally removing
 * itself from the world (replacing itself as an air block)
 */
public interface IDroppableBlock
{
	void fellBlockAsItem(World world, int x, int y, int z);
}
