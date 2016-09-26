package growthcraft.bees.common.world;

import java.util.Random;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.common.block.BlockBeeHive;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBeeHive extends WorldGenerator
{
	//constants
	private final int density = GrowthCraftBees.getConfig().beeWorldGenDensity;

	@Override
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		for (int loop = 0; loop < this.density; ++loop)
		{
			final int i = x + random.nextInt(8) - random.nextInt(8);
			final int j = y + random.nextInt(4) - random.nextInt(4);
			final int k = z + random.nextInt(8) - random.nextInt(8);

			final BlockBeeHive beeHive = (BlockBeeHive)GrowthCraftBees.blocks.beeHive.getBlock();
			if (world.isAirBlock(i, j, k) && beeHive.canBlockStay(world, i, j, k))
			{
				//				System.out.println(x + " " + y + " " + z);
				world.setBlock(i, j, k, beeHive, new Random().nextInt(4) + 2, 2);
			}
		}
		return true;
	}
}
