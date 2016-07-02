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
import growthcraft.core.logic.ISpreadablePlant;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockThistle extends BlockBush implements ISpreadablePlant, IGrowable
{
	private FlowerSpread spreadLogic;

	public BlockThistle()
	{
		super(Material.plants);
		setTickRandomly(true);
		setBlockTextureName("grcmilk:thistle/flower_thistle");
		setBlockName("grcmilk.Thistle");
		setStepSound(soundTypeGrass);
		setCreativeTab(GrowthCraftMilk.creativeTab);
		final BBox bb = BBox.newCube(2f, 0f, 2f, 12f, 16f, 12f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
		this.spreadLogic = new FlowerSpread(new CuboidI(-1, -1, -1, 2, 2, 2));
	}

	@Override
	public boolean canSpreadTo(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y, z) && canBlockStay(world, x, y, z))
		{
			return true;
		}
		return false;
	}

	private void runSpread(World world, int x, int y, int z, Random random)
	{
		spreadLogic.run(this, 0, world, x, y, z, random);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		if (!world.isRemote)
		{
			final int spreadChance = GrowthCraftMilk.getConfig().thistleSpreadChance;
			if (spreadChance > 0)
			{
				if (random.nextInt(spreadChance) == 0)
				{
					runSpread(world, x, y, z, random);
				}
			}
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	/* IGrowable interface
	 *	Check: http://www.minecraftforge.net/forum/index.php?topic=22571.0
	 *	if you have no idea what this stuff means
	 */

	/* Can this accept bonemeal? */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient)
	{
		return true;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		runSpread(world, x, y, z, random);
	}
}
