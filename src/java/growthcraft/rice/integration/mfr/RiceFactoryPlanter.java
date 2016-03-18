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
package growthcraft.rice.integration.mfr;

import growthcraft.core.integration.mfr.AbstractFactoryPlantable;
import growthcraft.rice.common.block.BlockRice;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RiceFactoryPlanter extends AbstractFactoryPlantable<BlockRice>
{
	public RiceFactoryPlanter()
	{
		super(GrowthCraftRice.rice.getItem(), GrowthCraftRice.riceBlock.getBlock());
	}

	@Override
	public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack)
	{
		return super.canBePlantedHere(world, x, y, z, stack) &&
			RiceBlockCheck.isFullPaddy(world, x, y - 1, z);
	}
}
