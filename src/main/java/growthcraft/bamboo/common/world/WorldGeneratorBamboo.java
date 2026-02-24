package growthcraft.bamboo.common.world;

import java.util.Random;

import growthcraft.api.core.util.BiomeUtils;
import growthcraft.bamboo.GrowthCraftBamboo;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGeneratorBamboo implements IWorldGenerator
{
	private final int rarity = GrowthCraftBamboo.getConfig().bambooWorldGenRarity;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.dimensionId == 0)
		{
			generateSurface(world, random, chunkX, chunkZ);
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (!world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat"))
		{
			final int i = chunkX * 16 + random.nextInt(16) + 8;
			final int j = random.nextInt(128);
			final int k = chunkZ * 16 + random.nextInt(16) + 8;

			final BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
			if (GrowthCraftBamboo.getConfig().useBiomeDict)
			{
				if (!BiomeUtils.testBiomeTypeTagsTable(biome, GrowthCraftBamboo.getConfig().bambooBiomesTypeList)) return;
			}
			else
			{
				final String biomeId = "" + biome.biomeID;
				if (!BiomeUtils.testBiomeIdTags(biomeId, GrowthCraftBamboo.getConfig().bambooBiomesIdList)) return;
			}

			if (random.nextInt(this.rarity) == 0)
			{
				new WorldGenBamboo(true).generateClumps(world, random, i, j, k);
			}
		}
	}
}
