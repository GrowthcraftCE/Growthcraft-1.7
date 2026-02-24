package growthcraft.bamboo.common.world;

import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenBamboo extends BiomeGenBase
{
	private WorldGenBamboo worldGenBamboo;

	public BiomeGenBamboo(int id)
	{
		super(id);
		this.theBiomeDecorator.treesPerChunk = 20;
		this.theBiomeDecorator.grassPerChunk = 4;
		this.worldGenBamboo = new WorldGenBamboo(false);
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 *
	 * @param random - random generator
	 * @return world abstract tree - The world generator used for the Bamboo Biome
	 */
	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
	{
		return worldGenBamboo;
	}
}
