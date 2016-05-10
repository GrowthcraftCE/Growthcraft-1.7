package growthcraft.milk.common.world;

import java.util.Random;

import growthcraft.api.core.util.BiomeUtils;
import growthcraft.api.core.util.StringUtils;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

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

	private void genRandThistle(WorldGenerator generator, World world, Random rand, int chunk_x, int chunk_z, int chanceToSpawn, int minHeight, int maxHeight)
	{
		for (int i = 0; i < chanceToSpawn; ++i)
		{
			final int x = chunk_x * 16 + rand.nextInt(16);
			final int z = chunk_z * 16 + rand.nextInt(16);
			int y = maxHeight;
			GrowthCraftMilk.getLogger().info("(Pre Height Map) Attempting to place a thistle { x: %d, y: %d, z: %d }", x, y, z);
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
			GrowthCraftMilk.getLogger().info("(Post Height Map) Attempting to place a thistle { x: %d, y: %d, z: %d }", x, y, z);
			// If we've exceeded the minHeight, bail this operation immediately
			if (y <= minHeight)
			{
				GrowthCraftMilk.getLogger().info("(ERROR: Min-Height Exceeded!) Attempting to place a thistle { x: %d, y: %d, z: %d }", x, y, z);
				continue;
			}

			final Block block = world.getBlock(x, y - 1, z);
			GrowthCraftMilk.getLogger().info("(Testing Plant Block) Attempting to place a thistle { x: %d, y: %d, z: %d, block: {%s} }", x, y - 1, z, block);
			if (canPlaceOnBlock(world, x, y - 1, z, block))
			{
				GrowthCraftMilk.getLogger().info("(Placing Thistle) Attempting to place a thistle { x: %d, y: %d, z: %d }", x, y, z);
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
				GrowthCraftMilk.getLogger().info(
					"Determining Biome Types { biome_gen: %s, biome_types: %s, type_tags: %s }",
					biome,
					StringUtils.inspect(BiomeDictionary.getTypesForBiome(biome)),
					StringUtils.inspect(GrowthCraftMilk.getConfig().thistleBiomesTypeList));
				if (!BiomeUtils.testBiomeTypeTagsTable(biome, GrowthCraftMilk.getConfig().thistleBiomesTypeList)) return;
			}
			else
			{
				final String biomeId = "" + biome.biomeID;
				GrowthCraftMilk.getLogger().info(
					"Determining Biome ID { id: %s, biome_gen: %s, ids: %s }",
					biomeId,
					biome,
					StringUtils.inspect(GrowthCraftMilk.getConfig().thistleBiomesIdList));

				if (!BiomeUtils.testBiomeIdTags(biomeId, GrowthCraftMilk.getConfig().thistleBiomesIdList)) return;
			}
			GrowthCraftMilk.getLogger().info("Doing le world gen");
			genRandThistle(thistle, world, random, chunkX, chunkZ, 10, 64, 255);
		}
	}
}
