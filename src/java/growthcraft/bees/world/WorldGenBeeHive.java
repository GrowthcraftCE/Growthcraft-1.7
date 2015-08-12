package growthcraft.bees.world;

import java.util.Random;

import growthcraft.bees.GrowthCraftBees;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBeeHive extends WorldGenerator
{
	//constants
	private final int density = GrowthCraftBees.beeWorldGen_density;

	@Override
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		for (int loop = 0; loop < this.density; ++loop)
		{
			int i = x + random.nextInt(8) - random.nextInt(8);
			int j = y + random.nextInt(4) - random.nextInt(4);
			int k = z + random.nextInt(8) - random.nextInt(8);

			if (world.isAirBlock(i, j, k) && GrowthCraftBees.beeHive.canBlockStay(world, i, j, k))
			{
				//				System.out.println(x + " " + y + " " + z);
				world.setBlock(i, j, k, GrowthCraftBees.beeHive, new Random().nextInt(4) + 2, 2);
			}
		}
		return true;
	}


}
