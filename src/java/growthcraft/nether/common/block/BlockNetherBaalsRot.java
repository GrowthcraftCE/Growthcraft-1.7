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

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BlockNetherBaalsRot extends BlockNetherFungusBase
{
	private final float baalsRotSpreadRate = GrowthCraftNether.getConfig().baalsRotSpreadRate;

	public BlockNetherBaalsRot()
	{
		super();
		setBlockName("grcnether.netherBaalsRot");
		setBlockTextureName("grcnether:baals_rot");
		setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		setCreativeTab(GrowthCraftNether.tab);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote) return;
		if (entity instanceof EntityLivingBase)
		{
			if (world.rand.nextFloat() < 0.3F)
			{
				((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * 20));
			}
		}
	}

	@Override
	protected float getSpreadRate(World world, int x, int y, int z)
	{
		return baalsRotSpreadRate;
	}
}
