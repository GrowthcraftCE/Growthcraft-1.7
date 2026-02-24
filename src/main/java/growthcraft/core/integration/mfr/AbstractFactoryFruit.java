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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.core.integration.MFRModuleBase;

import powercrystals.minefactoryreloaded.api.IFactoryFruit;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Optional.Interface(iface="powercrystals.minefactoryreloaded.api.IFactoryFruit", modid=MFRModuleBase.MOD_ID)
public abstract class AbstractFactoryFruit<TBlock extends Block> implements IFactoryFruit
{
	protected TBlock plantBlock;

	public AbstractFactoryFruit<TBlock> setPlant(TBlock pBlock)
	{
		this.plantBlock = pBlock;
		return this;
	}

	@Override
	public Block getPlant()
	{
		return plantBlock;
	}

	@Override
	@Deprecated
	public boolean breakBlock()
	{
		return true;
	}

	@Override
	public boolean canBePicked(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	@Optional.Method(modid=MFRModuleBase.MOD_ID)
	public ReplacementBlock getReplacementBlock(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public List<ItemStack> getDrops(World world, Random rand, int x, int y, int z)
	{
		if (plantBlock != null)
			return plantBlock.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		return new ArrayList<ItemStack>();
	}

	@Override
	public void prePick(World world, int x, int y, int z)
	{
	}

	@Override
	public void postPick(World world, int x, int y, int z)
	{
		if (plantBlock != null) world.notifyBlocksOfNeighborChange(x, y, z, plantBlock);
	}
}
