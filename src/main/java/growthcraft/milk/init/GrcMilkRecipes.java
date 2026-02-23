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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.core.fluids.TaggedFluidStacks;
import growthcraft.api.core.item.CommonItemStackComparator;
import growthcraft.api.core.item.IItemStackComparator;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.milk.cheesepress.ICheesePressRecipe;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.api.milk.util.CheeseVatRecipeBuilder;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.common.item.crafting.ShapelessItemComparableRecipe;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.ItemBlockHangingCurds;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class GrcMilkRecipes extends GrcModuleBase
{
	public static class DriedCurdComparator implements IItemStackComparator
	{
		private CommonItemStackComparator common = new CommonItemStackComparator();

		public boolean equals(ItemStack expected, ItemStack actual)
		{
			if (expected.getItem() instanceof ItemBlockHangingCurds)
			{
				if (actual.getItem() instanceof ItemBlockHangingCurds)
				{
					final ItemBlockHangingCurds actualCurd = (ItemBlockHangingCurds)actual.getItem();
					final ItemBlockHangingCurds expectedCurd = (ItemBlockHangingCurds)expected.getItem();
					if (expectedCurd.getCheeseType(expected) == actualCurd.getCheeseType(actual))
					{
						if (actualCurd.isDried(actual)) return true;
					}
				}
				return false;
			}
			else
			{
				return common.equals(expected, actual);
			}
		}
	}

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

	private void registerCraftingRecipes()
	{
		final int ricottaBowlCount = GrowthCraftMilk.getConfig().ricottaBowlCount;
		final List<ItemStack> ricottaBowlRecipe = new ArrayList<ItemStack>();
		ricottaBowlRecipe.add(EnumCheeseType.RICOTTA.asCurdItemStack());
		for (int i = 0; i < ricottaBowlCount; ++i)
		{
			ricottaBowlRecipe.add(new ItemStack(Items.bowl, 1));
		}
		GameRegistry.addRecipe(new ShapelessItemComparableRecipe(new DriedCurdComparator(),
			EnumCheeseType.RICOTTA.asStack(ricottaBowlCount), ricottaBowlRecipe
		));

		GameRegistry.addRecipe(new ShapelessOreRecipe(GrowthCraftMilk.blocks.cheeseVat.asStack(),
			GrowthCraftCellar.blocks.brewKettle.asStack()
		));

		GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftMilk.blocks.butterChurn.asStack(),
			" S ",
			"P P",
			"PPP",
			'S', "stickWood",
			'P', "plankWood"
		));

		GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftMilk.blocks.cheesePress.asStack(),
			"iii",
			"iCi",
			"ppp",
			'i', "ingotIron",
			'C', Blocks.chest,
			'p', "slabWood"
		));

		GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftMilk.blocks.pancheon.asStack(),
			"c c",
			"ccc",
			'c', Items.clay_ball
		));
	}

	private void registerCheeseVatRecipes()
	{
		final String[] saltOres = { "foodSalt", "materialSalt", "dustSalt" };

		for (String saltOre : saltOres)
		{
			CheeseVatRecipeBuilder.buildRecipe("CHEDDAR Orange Dye Recipe")
				.outputFluids(EnumCheeseType.CHEDDAR.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new OreItemStacks("dyeOrange", 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("CHEDDAR Pumpkin Recipe")
				.outputFluids(EnumCheeseType.CHEDDAR.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new ItemStack(Blocks.pumpkin))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("GORGONZOLA Recipe")
				.outputFluids(EnumCheeseType.GORGONZOLA.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new OreItemStacks("foodFruit", 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("EMMENTALER Recipe")
				.outputFluids(EnumCheeseType.EMMENTALER.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new OreItemStacks("cropWheat", 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("APPENZELLER Wine Recipe")
				.outputFluids(EnumCheeseType.APPENZELLER.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"), new TaggedFluidStacks(1000, "wine"))
				.inputItems(new OreItemStacks(saltOre, 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("APPENZELLER Cider Recipe")
				.outputFluids(EnumCheeseType.APPENZELLER.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"), new TaggedFluidStacks(1000, "cider"))
				.inputItems(new OreItemStacks(saltOre, 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("ASIAGO Recipe")
				.outputFluids(EnumCheeseType.ASIAGO.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new OreItemStacks(saltOre, 1), new OreItemStacks("dyeYellow", 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("PARMESAN Recipe")
				.outputFluids(EnumCheeseType.PARMESAN.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new OreItemStacks("dyeWhite", 1))
				.register();

			CheeseVatRecipeBuilder.buildRecipe("MONTEREY Recipe")
				.outputFluids(EnumCheeseType.MONTEREY.asFluidStack(5000))
				.inputFluids(new TaggedFluidStacks(5000, "milk_curds"))
				.inputItems(new OreItemStacks(saltOre, 1), new OreItemStacks("dyeRed", 1))
				.register();
		}
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

	private void registerCheeseWaxes()
	{
		EnumCheeseType.CHEDDAR.waxes.add(new OreItemStacks("materialBeeswaxRed", 1));
		EnumCheeseType.MONTEREY.waxes.add(new OreItemStacks("materialBeeswaxBlack", 1));
	}

	@Override
	public void init()
	{
		registerCraftingRecipes();
		registerCheeseVatRecipes();
		registerCheesePressRecipes();
		registerCheeseWaxes();
	}
}
