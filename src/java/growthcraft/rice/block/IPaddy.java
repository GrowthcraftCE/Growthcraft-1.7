package growthcraft.rice.block;

import net.minecraft.world.IBlockAccess;

public interface IPaddy
{
	public boolean isFilledWithWater(IBlockAccess world, int x, int y, int z, int meta);
	public boolean canConnectPaddyTo(IBlockAccess world, int x, int y, int z, int meta);
}
