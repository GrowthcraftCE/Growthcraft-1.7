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
package growthcraft.nether.common.item;

import growthcraft.core.util.BlockCheck;
import growthcraft.core.util.ItemUtils;
import growthcraft.nether.GrowthCraftNether;
import growthcraft.nether.common.block.BlockNetherSquashStem;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemNetherSquashSeeds extends Item implements IPlantable
{
	public ItemNetherSquashSeeds()
	{
		super();
		setUnlocalizedName("grcnether.netherSquashSeeds");
		setCreativeTab(GrowthCraftNether.tab);
		setTextureName("grcnether:seeds_soulsquash");
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Nether;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return GrowthCraftNether.blocks.netherSquashStem.getBlock();
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float par8, float par9, float par10)
	{
		if (dir != 0) return false;

		if (player.canPlayerEdit(x, y, z, dir, stack) && player.canPlayerEdit(x, y - 1, z, dir, stack))
		{
			final BlockNetherSquashStem plant = (BlockNetherSquashStem)getPlant(world, x, y, z);
			if (BlockCheck.canSustainPlant(world, x, y, z, ForgeDirection.DOWN, plant))
			{
				world.setBlock(x, y - 1, z, plant);
				ItemUtils.consumeStack(stack);
				return true;
			}
		}

		return false;
	}
}
