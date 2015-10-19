package growthcraft.bamboo.common.world;

import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenBamboo extends BiomeGenBase
{
	public BiomeGenBamboo(int id)
	{
		super(id);
		//this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
		this.theBiomeDecorator.treesPerChunk = 20;
		this.theBiomeDecorator.grassPerChunk = 2;
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 * @param random - random generator
	 * @return world generator - The world generator used for the Bamboo Biome
	 */
	public WorldGenerator getRandomWorldGenForTrees(Random random)
	{
		return new WorldGenBamboo(false);
	}
}
