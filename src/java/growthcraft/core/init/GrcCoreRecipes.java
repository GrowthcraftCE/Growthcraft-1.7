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
package growthcraft.core.init;

import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GrcCoreRecipes extends GrcModuleBase
{
	@Override
	public void register()
	{
		final ItemStack saltStack = GrowthCraftCore.items.salt.asStack();
		GameRegistry.addRecipe(GrowthCraftCore.items.rope.asStack(8), new Object[] {"A", 'A', Items.lead});
		GameRegistry.addShapelessRecipe(GrowthCraftCore.items.salt.asStack(2), GrowthCraftCore.items.saltBottle.asStack());
		GameRegistry.addShapelessRecipe(GrowthCraftCore.items.salt.asStack(6), GrowthCraftCore.items.saltBucket.asStack());
		GameRegistry.addShapelessRecipe(GrowthCraftCore.items.saltBottle.asStack(),
			Items.glass_bottle,
			saltStack, saltStack);
		GameRegistry.addShapelessRecipe(GrowthCraftCore.items.saltBucket.asStack(),
			Items.bucket,
			saltStack, saltStack, saltStack, saltStack, saltStack, saltStack);

		GameRegistry.addSmelting(GrowthCraftCore.fluids.saltWater.asBottleItemStack(), GrowthCraftCore.items.saltBottle.asStack(), 0.1F);
		GameRegistry.addSmelting(GrowthCraftCore.fluids.saltWater.asBucketItemStack(), GrowthCraftCore.items.saltBucket.asStack(), 0.2F);
	}
}
