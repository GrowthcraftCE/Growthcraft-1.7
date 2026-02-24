package growthcraft.milk.common.world;

import java.util.Random;

import growthcraft.api.core.util.BiomeUtils;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;

/**
 * Created by Firedingo on 25/02/2016.
 */
public class WorldGeneratorThistle implements IWorldGenerator
{
	private WorldGenerator thistle;

	private boolean canPlaceOnBlock(World world, int x, int y, int z, Block block)
	{
		return Blocks.dirt.equals(block) ||
			Blocks.grass.equals(block);
	}

	private boolean canReplaceBlock(World world, int x, int y, int z, Block block)
	{
		return block.isAir(world, x, y, z) ||
			block.isLeaves(world, x, y, z) ||
			Blocks.vine == block;
	}

	private void genRandThistle(WorldGenerator generator, World world, Random rand, int chunk_x, int chunk_z, int maxToSpawn, int minHeight, int maxHeight)
	{
		final int genChance = GrowthCraftMilk.getConfig().thistleGenChance;
		for (int i = 0; i < maxToSpawn; ++i)
		{
			if (genChance > 0)
			{
				if (rand.nextInt(genChance) != 0) continue;
			}

			final int x = chunk_x * 16 + rand.nextInt(16);
			final int z = chunk_z * 16 + rand.nextInt(16);
			int y = maxHeight;
			for (; y > minHeight; --y)
			{
				// If you can't replace the block now, it means you probably
				// hit the floor
				if (!canReplaceBlock(world, x, y, z, world.getBlock(x, y, z)))
				{
					// move back up and break loop
					y += 1;
					break;
				}
			}
			// If we've exceeded the minHeight, bail this operation immediately
			if (y <= minHeight)
			{
				continue;
			}

			final Block block = world.getBlock(x, y - 1, z);
			if (canPlaceOnBlock(world, x, y - 1, z, block))
			{
				world.setBlock(x, y, z, GrowthCraftMilk.blocks.thistle.getBlock());
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.dimensionId == 0)
		{
			final BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
			if (GrowthCraftMilk.getConfig().thistleUseBiomeDict)
			{
				if (!BiomeUtils.testBiomeTypeTagsTable(biome, GrowthCraftMilk.getConfig().thistleBiomesTypeList)) return;
			}
			else
			{
				final String biomeId = "" + biome.biomeID;
				if (!BiomeUtils.testBiomeIdTags(biomeId, GrowthCraftMilk.getConfig().thistleBiomesIdList)) return;
			}
			genRandThistle(thistle, world, random, chunkX, chunkZ, GrowthCraftMilk.getConfig().thistleGenAmount, 64, 255);
		}
	}
}
