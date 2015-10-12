package growthcraft.grapes.utils;

import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.Block;

public class GrapeBlockCheck
{
	private GrapeBlockCheck() {}

	/**
	 * Determines if block is a grape vine.
	 *
	 * @param block - block to check
	 * @return true if block is a grape vine, false otherwise
	 */
	public static boolean isGrapeVine(Block block)
	{
		return GrowthCraftGrapes.grapeVine0.getBlock() == block ||
			GrowthCraftGrapes.grapeVine1.getBlock() == block;
	}
}
