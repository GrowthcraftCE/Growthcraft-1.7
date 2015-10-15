package growthcraft.core.utils;

import java.util.Random;

import growthcraft.core.block.IBlockRope;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCheck
{
	/**
	 * 2D directions
	 */
	public static final ForgeDirection[] DIR4 = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

	private BlockCheck() {}

	/**
	 * Randomly selects a direction from the DIR4 array and returns it
	 *
	 * @param random - random number generator
	 * @return a random direction
	 */
	public static ForgeDirection randomDirection4(Random random)
	{
		return DIR4[random.nextInt(DIR4.length)];
	}

	/**
	 * Determines if block is a water block
	 *
	 * @param block - the block to check
	 * @return true if the block is a water, false otherwise
	 */
	public static boolean isWater(Block block)
	{
		if (block == null) return false;
		return block.getMaterial() == Material.water;
	}

	/**
	 * Determines if block is a rope block
	 *
	 * @param block - the block to check
	 * @return true if the block is a rope block, false otherwise
	 */
	public static boolean isRopeBlock(Block block)
	{
		return block instanceof IBlockRope;
	}

	/**
	 * Determines if block is a "rope"
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
		final Block block = world.getBlock(x, y, z);
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
		return soil != null && soil.canSustainPlant(world, x, y, z, dir, plant);
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
		final Block soil = world.getBlock(x, y, z);
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
		final Block soil = world.getBlock(x, y, z);
		if (canSustainPlantOn(world, x, y, z, dir, plant, soil))
			return soil;
		return null;
	}
}
