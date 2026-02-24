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
package growthcraft.core.integration.mfr;

import powercrystals.minefactoryreloaded.api.ReplacementBlock;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AbstractFactoryPlantable<T extends Block> implements IFactoryPlantable
{
	protected final Item plantSeed;
	protected final T plantBlock;
	protected ReplacementBlock replacementBlock;

	public AbstractFactoryPlantable(Item pSeed, T pBlock)
	{
		this.plantSeed = pSeed;
		this.plantBlock = pBlock;
		this.replacementBlock = new ReplacementBlock(plantBlock);
	}

	@Override
	public Item getSeed()
	{
		return plantSeed;
	}

	@Override
	public boolean canBePlanted(ItemStack stack, boolean forFermenting)
	{
		return true;
	}

	@Override
	public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack)
	{
		return replacementBlock;
	}

	@Override
	public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack)
	{
		return plantBlock.canPlaceBlockAt(world, x, y, z);
	}

	@Override
	public void prePlant(World world, int x, int y, int z, ItemStack stack)
	{
	}

	@Override
	public void postPlant(World world, int x, int y, int z, ItemStack stack)
	{
	}
}
