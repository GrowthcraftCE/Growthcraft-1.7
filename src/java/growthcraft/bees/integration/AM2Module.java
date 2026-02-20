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
package growthcraft.bees.integration;

import growthcraft.bees.common.block.BlockBeeBoxArsMagica2;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.integration.AM2.AM2Platform;
import growthcraft.core.integration.AM2.EnumAM2WoodType;
import growthcraft.core.integration.ModIntegrationBase;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class AM2Module extends ModIntegrationBase
{
	public AM2Module()
	{
		super(GrowthCraftBees.MOD_ID, AM2Platform.MOD_ID);
	}

	@Override
	public void doPreInit()
	{
		GrowthCraftBees.blocks.beeBoxArsMagica2 = GrowthCraftBees.blocks.newTypedDefinition(new BlockBeeBoxArsMagica2());
	}

	@Override
	public void doRegister()
	{
		GrowthCraftBees.blocks.beeBoxArsMagica2.register("grc.beeBox.ArsMagica2", ItemBlockBeeBox.class);
	}

	@Override
	protected void doLateRegister()
	{
		for (EnumAM2WoodType type : EnumAM2WoodType.VALUES)
		{
			final ItemStack planks = type.asPlanksItemStack();
			if (planks != null)
			{
				GameRegistry.addShapedRecipe(GrowthCraftBees.blocks.beeBoxArsMagica2.asStack(), " A ", "A A", "AAA", 'A', planks);
			}
		}
	}
}
