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
package growthcraft.core.integration;

import java.util.Collection;
import java.util.List;

import growthcraft.api.core.definition.IFluidStackFactory;
import growthcraft.api.core.definition.IItemStackFactory;
import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.item.ItemTest;
import growthcraft.core.integration.forestry.recipes.RecipeManagersShims;

import forestry.api.core.ForestryAPI;
import forestry.api.core.IGameMode;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import forestry.api.storage.BackpackManager;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Helper class for integrating Forestry with Growthcraft, simply extend
 * class and implement the integrate method
 */
public abstract class ForestryModuleBase extends ModIntegrationBase
{
	public static enum ForestryFluids implements IFluidStackFactory
	{
		// This is really just vanilla water though ;O
		WATER("water"),
		HONEY("honey"),
		BIOMASS("biomass"),
		JUICE("juice"),
		SEEDOIL("seedoil");

		public final String name;

		private ForestryFluids(String nm)
		{
			this.name = nm;
		}

		public Fluid getFluid()
		{
			return FluidRegistry.getFluid(name);
		}

		@Override
		public FluidStack asFluidStack(int amount)
		{
			final Fluid fluid = getFluid();
			if (fluid == null) return null;
			return new FluidStack(fluid, amount);
		}

		@Override
		public FluidStack asFluidStack()
		{
			return asFluidStack(1);
		}

		/**
		 * Does the underlying fluid exist?
		 *
		 * @return true if it exists, false otherwise
		 */
		public boolean exists()
		{
			return getFluid() != null;
		}
	}

	public static enum ForestryItems implements IItemStackFactory
	{
		BEESWAX("beeswax"),
		HONEY_DROP("honeyDrop"),
		HONEYDEW("honeydew");

		public final String name;
		public final int meta;

		private ForestryItems(String n)
		{
			this.name = n;
			this.meta = 0;
		}

		public Item getItem()
		{
			return GameRegistry.findItem("Forestry", name);
		}

		@Override
		public ItemStack asStack(int size)
		{
			final Item item = getItem();
			if (item == null) return null;
			return new ItemStack(item, size, meta);
		}

		@Override
		public ItemStack asStack()
		{
			return asStack(1);
		}

		public boolean exists()
		{
			return getItem() != null;
		}
	}

	/**
	 * Wrapper around the Forestry BackpackManager, safely handles null backpacks.
	 *
	 * @example
	 *   Backpack.MINERS.add(anItemStack);
	 */
	public static enum Backpack
	{
		MINERS,
		DIGGERS,
		FORESTERS,
		HUNTERS,
		ADVENTURERS;

		public final int index;

		private Backpack()
		{
			this.index = ordinal();
		}

		@Optional.Method(modid="ForestryAPI|storage")
		public List<ItemStack> items()
		{
			if (BackpackManager.backpackItems == null)
				return null;

			return BackpackManager.backpackItems[index];
		}

		public void add(ItemStack stack)
		{
			final List<ItemStack> target = items();
			if (target != null) target.add(stack);
		}
	}

	public static class ForestryRecipeUtils
	{
		public static RecipeManagersShims recipes()
		{
			return RecipeManagersShims.instance();
		}

		public static void addFermenterRecipes(ItemStack resource, int fermentationValue, FluidStack output)
		{
			if (!ItemTest.isValid(resource)) return;
			if (!FluidTest.isValid(output)) return;
			if (ForestryFluids.WATER.exists()) recipes().fermenterManager.addRecipe(resource, fermentationValue, 1.0f, output, ForestryFluids.WATER.asFluidStack());
			if (ForestryFluids.JUICE.exists()) recipes().fermenterManager.addRecipe(resource, fermentationValue, 1.5f, output, ForestryFluids.JUICE.asFluidStack());
			if (ForestryFluids.HONEY.exists()) recipes().fermenterManager.addRecipe(resource, fermentationValue, 1.5f, output, ForestryFluids.HONEY.asFluidStack());
		}
	}

	public static final String MOD_ID = "Forestry";

	public ForestryModuleBase(String modid)
	{
		super(modid, MOD_ID);
	}

	@Optional.Method(modid="ForestryAPI|core")
	public IGameMode getActiveMode()
	{
		return ForestryAPI.activeMode;
	}

	public RecipeManagersShims recipes()
	{
		return ForestryRecipeUtils.recipes();
	}

	@Optional.Method(modid="ForestryAPI|farming")
	public void addFarmable(String ns, IFarmable farmable)
	{
		final Collection<IFarmable> farmables = Farmables.farmables.get(ns);
		if (farmables != null) farmables.add(farmable);
	}
}
