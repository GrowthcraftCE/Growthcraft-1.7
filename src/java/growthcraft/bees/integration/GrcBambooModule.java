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

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.integration.ModIntegrationBase;
import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.bees.common.block.BlockBeeBoxBamboo;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.core.common.definition.BlockTypeDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GrcBambooModule extends ModIntegrationBase
{
	public GrcBambooModule()
	{
		super(GrowthCraftBees.MOD_ID, "Growthcraft|Bamboo");
	}

	@Override
	protected void doPreInit()
	{
		GrowthCraftBees.beeBoxBamboo  = new BlockTypeDefinition<BlockBeeBox>(new BlockBeeBoxBamboo());
		GrowthCraftBees.beeBoxBamboo.getBlock().setFlammability(20).setFireSpreadSpeed(5).setHarvestLevel("axe", 0);
	}

	@Override
	protected void doRegister()
	{
		GameRegistry.registerBlock(GrowthCraftBees.beeBoxBamboo.getBlock(), ItemBlockBeeBox.class, "grc.BeeBox.Bamboo");
	}

	@Override
	protected void doLateRegister()
	{
		// Bamboo
		GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftBees.beeBoxBamboo.asStack(), " A ", "A A", "AAA", 'A', "plankBamboo"));
	}
}
