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
package growthcraft.core.common.block;

import java.util.Random;

import growthcraft.api.core.util.RandomUtils;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockSaltBlock extends GrcBlockBase
{
	public BlockSaltBlock()
	{
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypePiston);
		setBlockName("grccore.salt_block");
		setBlockTextureName("grccore:salt_block");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune)
	{
		return GrowthCraftCore.items.salt.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return RandomUtils.range(random, 4, 9);
	}
}
