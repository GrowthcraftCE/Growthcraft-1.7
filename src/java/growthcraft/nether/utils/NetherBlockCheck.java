package growthcraft.nether.utils;

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.block.Block;

public class NetherBlockCheck
{
	private NetherBlockCheck() {}

	/**
	 * Determines if block is a nether paddy block
	 *
	 * @param block - the block to check
	 * @return true if the block is a Paddy, false otherwise
	 */
	public static boolean isPaddy(Block block)
	{
		return GrowthCraftNether.blocks.netherPaddyField.equals(block);
	}
}
