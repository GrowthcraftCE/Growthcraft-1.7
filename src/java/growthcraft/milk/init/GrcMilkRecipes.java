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
package growthcraft.milk.init;

import javax.annotation.Nonnull;

import growthcraft.api.core.util.OreItemStacks;
import growthcraft.api.core.util.TaggedFluidStacks;
import growthcraft.api.milk.cheesepress.ICheesePressRecipe;
import growthcraft.api.milk.util.CheeseVatRecipeBuilder;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.ItemBlockHangingCurds;

import net.minecraft.item.ItemStack;

public class GrcMilkRecipes extends GrcModuleBase
{
	public static class DriedCurdsCheesePressRecipe implements ICheesePressRecipe
	{
		private ItemStack inputStack;
		private ItemStack outputStack;
		private int time;

		public DriedCurdsCheesePressRecipe(@Nonnull ItemStack pInputStack, @Nonnull ItemStack pOutputStack, int pTime)
		{
			this.inputStack = pInputStack;
			this.outputStack = pOutputStack;
			this.time = pTime;
		}

		@Override
		public ItemStack getInputItemStack()
		{
			return inputStack;
		}

		@Override
		public ItemStack getOutputItemStack()
		{
			return outputStack;
		}

		@Override
		public int getTimeMax()
		{
			return time;
		}

		@Override
		public boolean isMatchingRecipe(@Nonnull ItemStack stack)
		{
			if (inputStack.isItemEqual(stack))
			{
				if (stack.stackSize >= inputStack.stackSize)
				{
					if (stack.getItem() instanceof ItemBlockHangingCurds)
					{
						final ItemBlockHangingCurds item = (ItemBlockHangingCurds)stack.getItem();
						return item.isDried(stack);
					}
				}
			}
			return false;
		}

		@Override
		public String toString()
		{
			return String.format("DriedCurdsCheesePressRecipe({%s} / %d = {%s})", getOutputItemStack(), time, getInputItemStack());
		}
	}

	private void registerCheeseVatRecipes()
	{
		CheeseVatRecipeBuilder.buildRecipe("CHEDDAR Recipe")
			.outputFluids(EnumCheeseType.CHEDDAR.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
			.inputItems(new OreItemStacks("foodSalt", 1), new OreItemStacks("dyeOrange", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("GORGONZOLA Recipe")
			.outputFluids(EnumCheeseType.GORGONZOLA.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
			.inputItems(new OreItemStacks("foodSalt", 1), new OreItemStacks("foodFruit", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("EMMENTALER Recipe")
			.outputFluids(EnumCheeseType.EMMENTALER.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
			.inputItems(new OreItemStacks("foodSalt", 1), new OreItemStacks("cropWheat", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("APPENZELLER Recipe")
			.outputFluids(EnumCheeseType.APPENZELLER.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"), new TaggedFluidStacks(1000, "wine"))
			.inputItems(new OreItemStacks("foodSalt", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("APPENZELLER Recipe")
			.outputFluids(EnumCheeseType.APPENZELLER.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"), new TaggedFluidStacks(1000, "cider"))
			.inputItems(new OreItemStacks("foodSalt", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("ASIAGO Recipe")
			.outputFluids(EnumCheeseType.ASIAGO.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
			.inputItems(new OreItemStacks("foodSalt", 1), new OreItemStacks("foodSalt", 1), new OreItemStacks("dyeYellow", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("PARMESAN Recipe")
			.outputFluids(EnumCheeseType.PARMESAN.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
			.inputItems(new OreItemStacks("foodSalt", 1), new OreItemStacks("dyeWhite", 1))
			.register();

		CheeseVatRecipeBuilder.buildRecipe("MONTEREY Recipe")
			.outputFluids(EnumCheeseType.MONTEREY.asFluidStack(5000))
			.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
			.inputItems(new OreItemStacks("foodSalt", 1), new OreItemStacks("dyeRed", 1))
			.register();
	}

	private void registerCheesePressRecipes()
	{
		for (EnumCheeseType type : EnumCheeseType.VALUES)
		{
			if (type.hasBlock() && type.hasCurdBlock())
			{
				MilkRegistry.instance().cheesePress().addRecipe(new DriedCurdsCheesePressRecipe(type.asCurdItemStack(), type.asBlockItemStack(), 200));
			}
		}
	}

	@Override
	public void init()
	{
		registerCheeseVatRecipes();
		registerCheesePressRecipes();
	}
}
