package growthcraft.core.block;

import net.minecraft.world.World;

public interface IDroppableBlock
{
	void fellBlockAsItem(World world, int x, int y, int z);
}
