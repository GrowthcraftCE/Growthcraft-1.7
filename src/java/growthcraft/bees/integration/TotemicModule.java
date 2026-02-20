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

import growthcraft.bees.common.block.BlockBeeBoxTotemic;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.integration.totemic.TotemicPlatform;
import growthcraft.core.integration.totemic.EnumTotemicWoodType;
import growthcraft.core.integration.ModIntegrationBase;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class TotemicModule extends ModIntegrationBase
{
	public TotemicModule()
	{
		super(GrowthCraftBees.MOD_ID, TotemicPlatform.MOD_ID);
	}

	@Override
	public void doPreInit()
	{
		GrowthCraftBees.blocks.beeBoxTotemic = GrowthCraftBees.blocks.newTypedDefinition(new BlockBeeBoxTotemic());
	}

	@Override
	public void doRegister()
	{
		GrowthCraftBees.blocks.beeBoxTotemic.register("grc.BeeBox.Totemic", ItemBlockBeeBox.class);
	}

	@Override
	protected void doLateRegister()
	{
		for (EnumTotemicWoodType type : EnumTotemicWoodType.VALUES)
		{
			final ItemStack planks = type.asPlanksItemStack();
			if (planks != null)
			{
				GameRegistry.addShapedRecipe(GrowthCraftBees.blocks.beeBoxTotemic.asStack(), " A ", "A A", "AAA", 'A', planks);
			}
		}
	}
}
