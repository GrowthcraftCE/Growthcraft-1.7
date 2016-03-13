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
import java.util.Map;
import java.util.Random;

import growthcraft.core.integration.MFRModuleBase;

import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;

import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Optional.Interface(iface="powercrystals.minefactoryreloaded.api.IFactoryHarvestable", modid=MFRModuleBase.MOD_ID)
public abstract class AbstractFactoryHarvestable<TBlock extends Block> implements IFactoryHarvestable
{
	protected TBlock plantBlock;
	protected HarvestType type = HarvestType.Normal;

	public AbstractFactoryHarvestable setPlant(TBlock pBlock)
	{
		this.plantBlock = pBlock;
		return this;
	}

	@Optional.Method(modid=MFRModuleBase.MOD_ID)
	public AbstractFactoryHarvestable setHarvestType(HarvestType pType)
	{
		this.type = pType;
		return this;
	}

	@Override
	public Block getPlant()
	{
		return plantBlock;
	}

	@Override
	@Optional.Method(modid=MFRModuleBase.MOD_ID)
	public HarvestType getHarvestType()
	{
		return type;
	}

	@Override
	public boolean breakBlock()
	{
		return true;
	}

	@Override
	public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z)
	{
		return true;
	}

	@Override
	public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z)
	{
		if (plantBlock != null)
			return plantBlock.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		return new ArrayList<ItemStack>();
	}

	@Override
	public void preHarvest(World world, int x, int y, int z)
	{
	}

	@Override
	public void postHarvest(World world, int x, int y, int z)
	{
		if (plantBlock != null) world.notifyBlocksOfNeighborChange(x, y, z, plantBlock);
	}
}
