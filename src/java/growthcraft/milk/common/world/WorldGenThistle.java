package growthcraft.milk.common.world;

import cpw.mods.fml.common.IWorldGenerator;
import growthcraft.milk.GrowthCraftMilk;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Firedingo on 25/02/2016.
 */
public class WorldGenThistle implements IWorldGenerator
{
	private WorldGenerator thistle;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.dimensionId == 0)
		{
			final BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
			if (b.biomeName.matches("Extreme Hills") || b.biomeName.matches("Extreme Hills Edge"))
			{
				this.genRandThistle(this.thistle, world, random, chunkX, chunkZ, 10, 64, 255);
			}
		}
	}

	private void genRandThistle(WorldGenerator generator, World world, Random rand, int chunk_x, int chunk_z, int chanceToSpawn, int minHeight, int maxHeight)
	{
		final int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chanceToSpawn; ++i)
		{
			final int x = chunk_x * 16 + rand.nextInt(16);
			final int z = chunk_z * 16 + rand.nextInt(16);
			final int y = minHeight + rand.nextInt(heightDiff);
			if (world.getBlock(x, y - 1, z) == Blocks.grass)
			{
				world.setBlock(x, y, z, GrowthCraftMilk.blocks.thistle.getBlock());
			}
		}
	}
}
