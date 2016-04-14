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
package growthcraft.milk.common.block;

import java.util.Random;

import growthcraft.api.core.util.BBox;
import growthcraft.api.core.util.CuboidI;
import growthcraft.core.logic.FlowerSpread;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockThistle extends BlockBush
{
	private FlowerSpread spreadLogic;

	public BlockThistle()
	{
		super(Material.plants);
		setTickRandomly(true);
		setBlockTextureName("grcmilk:thistle/flower_thistle");
		setBlockName("grcmilk.Thistle");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		final BBox bb = BBox.newCube(2f, 0f, 2f, 12f, 16f, 12f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
		this.spreadLogic = new FlowerSpread(new CuboidI(-2, -1, -2, 4, 2, 4));
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		if (!world.isRemote)
		{
			if (random.nextInt(GrowthCraftMilk.getConfig().thistleSpreadChance) == 0)
			{
				spreadLogic.run(this, world, x, y, z, random);
			}
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}
}
