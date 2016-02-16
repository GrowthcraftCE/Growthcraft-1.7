/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.nether.common.block;

import java.util.Random;

import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockNetherMuertecap extends BlockNetherFungusBase
{
	private final float muertecapSpreadRate = GrowthCraftNether.getConfig().muertecapSpreadRate;

	public BlockNetherMuertecap()
	{
		super();
		setBlockName("grcnether.netherMuertecap");
		setBlockTextureName("grcnether:muertecap");
		setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.375F, 0.625F);
		setCreativeTab(null);
	}

	@Override
	protected float getSpreadRate(World world, int x, int y, int z)
	{
		return muertecapSpreadRate;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftNether.items.netherMuertecap.getItem();
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftNether.items.netherMuertecap.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
}
