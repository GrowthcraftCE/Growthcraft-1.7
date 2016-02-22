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
package growthcraft.bees.integration;

import com.google.common.collect.ImmutableMap;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.integration.ForestryModuleBase;

import cpw.mods.fml.common.registry.GameRegistry;

import cpw.mods.fml.common.Optional;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ForestryModule extends ForestryModuleBase
{
	public ForestryModule()
	{
		super(GrowthCraftBees.MOD_ID);
	}

	private void maybeAddBee(Item item)
	{
		if (item != null)
		{
			GrowthCraftBees.getUserBeesConfig().addDefault(item).setComment("From Forestry");
		}
	}

	@Override
	@Optional.Method(modid="Forestry")
	protected void integrate()
	{
		maybeAddBee(GameRegistry.findItem(modID, "beeQueenGE"));
		maybeAddBee(GameRegistry.findItem(modID, "beeDroneGE"));
		maybeAddBee(GameRegistry.findItem(modID, "beePrincessGE"));

		CellarRegistry.instance().booze().addBoozeAlternative("short.mead", "grc.honeymead0");

		final ItemStack emptyComb = GrowthCraftBees.items.honeyCombEmpty.asStack();
		final ItemStack fullComb = GrowthCraftBees.items.honeyCombFilled.asStack();
		if (ForestryItems.BEESWAX.exists()) recipes().centrifugeManager.addRecipe(20, emptyComb, ImmutableMap.of(ForestryItems.BEESWAX.asStack(), 1.0f));

		if (ForestryItems.BEESWAX.exists() && ForestryItems.HONEY_DROP.exists() && ForestryItems.HONEYDEW.exists())
		{
			recipes().centrifugeManager.addRecipe(20, fullComb,
				ImmutableMap.of(
					ForestryItems.BEESWAX.asStack(), 1.0f,
					ForestryItems.HONEY_DROP.asStack(), 0.9f,
					ForestryItems.HONEYDEW.asStack(), 0.1f
				)
			);
		}
	}
}

