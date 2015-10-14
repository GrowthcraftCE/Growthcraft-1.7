package growthcraft.core.block;

import net.minecraft.world.World;

public interface ICropBlock
{
	public boolean onUseBonemeal(World world, int x, int y, int z);
	public boolean canGrow(World world, int x, int y, int z);
	public boolean isFullyGrown(World world, int x, int y, int z);
}
