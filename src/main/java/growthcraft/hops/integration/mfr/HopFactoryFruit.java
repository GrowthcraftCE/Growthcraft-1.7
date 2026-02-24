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
package growthcraft.hops.integration.mfr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.mfr.AbstractFactoryFruit;
import growthcraft.hops.common.block.BlockHops;
import growthcraft.hops.GrowthCraftHops;

import powercrystals.minefactoryreloaded.api.ReplacementBlock;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public class HopFactoryFruit extends AbstractFactoryFruit<BlockHops>
{
	private ReplacementBlock replacementBlock;

	public HopFactoryFruit()
	{
		super();
		setPlant(GrowthCraftHops.blocks.hopVine.getBlock());
		this.replacementBlock = new ReplacementBlock(plantBlock);
		replacementBlock.setMeta(BlockHops.HopsStage.BIG);
	}

	@Override
	@Deprecated
	public boolean breakBlock()
	{
		return false;
	}

	@Override
	public boolean canBePicked(World world, int x, int y, int z)
	{
		return plantBlock.isMature(world, x, y, z);
	}

	@Override
	public ReplacementBlock getReplacementBlock(World world, int x, int y, int z)
	{
		return replacementBlock;
	}

	@Override
	public List<ItemStack> getDrops(World world, Random rand, int x, int y, int z)
	{
		final List<ItemStack> drops = super.getDrops(world, rand, x, y, z);
		final List<ItemStack> result = new ArrayList<ItemStack>();
		for (ItemStack drop : drops)
		{
			if (GrowthCraftCore.items.rope.equals(drop.getItem())) continue;
			result.add(drop);
		}
		return result;
	}
}
