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

import growthcraft.nether.GrowthCraftNether;
import growthcraft.nether.util.DamageSources;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemNetherMuertecap extends ItemFood implements IPlantable
{
	public ItemNetherMuertecap()
	{
		super(0, false);
		setAlwaysEdible();
		setUnlocalizedName("grcnether.netherMuertecapFood");
		setTextureName("grcnether:muertecap");
		setCreativeTab(GrowthCraftNether.tab);
	}

	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.attackEntityFrom(DamageSources.muertecapFood, 20.0F);
		}
	}

	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Nether;
	}

	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return GrowthCraftNether.blocks.netherMuertecap.getBlock();
	}

	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
}
