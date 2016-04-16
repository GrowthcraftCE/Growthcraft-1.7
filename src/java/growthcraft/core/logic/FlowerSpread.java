/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.logic;

import java.util.Random;
import javax.annotation.Nonnull;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.CuboidI;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class FlowerSpread
{
	private CuboidI spreadCube;

	public FlowerSpread(@Nonnull CuboidI spread)
	{
		this.spreadCube = spread;
	}

	private boolean canSpreadTo(Block block, World world, int x, int y, int z)
	{
		if (block instanceof ISpreadablePlant)
		{
			return ((ISpreadablePlant)block).canSpreadTo(world, x, y, z);
		}
		else
		{
			return block.canPlaceBlockAt(world, x, y, z);
		}
	}

	public boolean run(Block block, World world, int x, int y, int z, Random random)
	{
		final int fx = random.nextInt(spreadCube.w) - spreadCube.x;
		final int fz = random.nextInt(spreadCube.l) - spreadCube.z;
		for (int i = spreadCube.y; i <= spreadCube.y2(); ++i)
		{
			if (canSpreadTo(block, world, x + fx, y + i, z + fz))
			{
				world.setBlock(x + fx, y + i, z + fz, block, 0, BlockFlags.UPDATE_AND_SYNC);
				return true;
			}
		}
		return false;
	}
}
