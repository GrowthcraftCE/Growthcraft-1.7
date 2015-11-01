package growthcraft.api.cellar.heatsource;

import net.minecraft.world.World;

public interface IHeatSourceBlock
{
	float getHeat(World world, int x, int y, int z);
}
