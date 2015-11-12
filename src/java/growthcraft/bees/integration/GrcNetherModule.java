/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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

import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.bees.common.block.BlockBeeBoxNether;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.integration.ModIntegrationBase;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GrcNetherModule extends ModIntegrationBase
{
	public GrcNetherModule()
	{
		super(GrowthCraftBees.MOD_ID, "Growthcraft|Nether");
	}

	@Override
	protected void doPreInit()
	{
		GrowthCraftBees.beeBoxNether  = new BlockTypeDefinition<BlockBeeBox>(new BlockBeeBoxNether());
		GrowthCraftBees.beeBoxNether.getBlock().setHarvestLevel("axe", 0);
	}

	@Override
	protected void doRegister()
	{
		if (GrowthCraftBees.beeBoxNether != null)
		{
			GameRegistry.registerBlock(GrowthCraftBees.beeBoxNether.getBlock(), ItemBlockBeeBox.class, "grc.BeeBox.Nether");
		}
	}

	@Override
	protected void doLateRegister()
	{
		if (GrowthCraftBees.beeBoxNether != null)
		{
			// plankMaliceWood is registered by the Growthcraft|Nether module, and is a non-flammable plank
			GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftBees.beeBoxNether.asStack(), " A ", "A A", "AAA", 'A', "plankMaliceWood"));
		}
	}
}
