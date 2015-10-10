package growthcraft.core.utils;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.block.IBlockRope;
import growthcraft.core.block.BlockRope;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCheck
{
	/**
	 * Determines if block at the specified location is a valid rope block
	 *
	 * @param block - the block to check
	 * @return true if the block is a Rope, false otherwise
	 */
	public static boolean isRope(Block block)
	{
		return GrowthCraftCore.ropeBlock.equals(block);
	}

	/**
	 * Determines if block at the specified location is a valid rope block
	 *
	 * @param world - World, duh.
	 * @param x  - x coord
	 * @param y  - y coord
	 * @param z  - z coord
	 * @return true if the block is a Rope, false otherwise
	 */
	public static boolean isRope(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		// TODO: IBlockRope is used for any block which can grow on Ropes,
		// as well as Ropes themselves, we need someway to seperate them,
		// either, IBlockRope.isRope(world, x, y, z) OR an additional interface
		// IBlockRopeCrop, IRope
		return isRope(block);
	}

	/**
	 * Determines if the block at the specified location can sustain an
	 * IPlantable plant.
	 *
	 * @param soil - The soil block
	 * @param world - World
	 * @param x  - x coord
	 * @param y  - y coord
	 * @param z  - z coord
	 * @param dir  - direction in which the plant will grow
	 * @param plant  - the plant in question
	 * @return true if the block can be planted, false otherwise
	 */
	public static boolean canSustainPlantOn(IBlockAccess world, int x, int y, int z, ForgeDirection dir, IPlantable plant, Block soil)
	{
		return (soil != null && soil.canSustainPlant(world, x, y, z, dir, plant));
	}

	/**
	 * Determines if the block at the specified location can sustain an
	 * IPlantable plant.
	 *
	 * @param world - World, duh.
	 * @param x  - x coord
	 * @param y  - y coord
	 * @param z  - z coord
	 * @param dir  - direction in which the plant will grow
	 * @param plant  - the plant in question
	 * @return true if the block can be planted, false otherwise
	 */
	public static boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection dir, IPlantable plant)
	{
		Block soil = world.getBlock(x, y, z);
		return canSustainPlantOn(world, x, y, z, dir, plant, soil);
	}

	/**
	 * Determines if the block at the specified location can sustain an IPlantable plant, returns the block if so, else returns null;
	 *
	 * @param world - World
	 * @param x  - x coord
	 * @param y  - y coord
	 * @param z  - z coord
	 * @param dir  - direction in which the plant will grow
	 * @param plant  - the plant in question
	 * @return block if it can be planted upon, else null
	 */
	public static Block getFarmableBlock(IBlockAccess world, int x, int y, int z, ForgeDirection dir, IPlantable plant)
	{
		Block soil = world.getBlock(x, y, z);
		if (canSustainPlantOn(world, x, y, z, dir, plant, soil))
			return soil;
		return null;
	}
}
