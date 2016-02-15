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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

import growthcraft.api.core.definition.IItemStackFactory;
import growthcraft.api.core.definition.IFluidStackFactory;
import growthcraft.api.core.util.ObjectUtils;
import growthcraft.api.core.util.FluidTest;
import growthcraft.api.core.util.ItemTest;

import forestry.api.core.ForestryAPI;
import forestry.api.core.IGameMode;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import forestry.api.recipes.ICarpenterManager;
import forestry.api.recipes.ICentrifugeManager;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.ICraftingProvider;
import forestry.api.recipes.IFabricatorManager;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.api.recipes.IFermenterManager;
import forestry.api.recipes.IMoistenerManager;
import forestry.api.recipes.ISqueezerManager;
import forestry.api.recipes.IStillManager;
import forestry.api.recipes.RecipeManagers;
import forestry.api.storage.BackpackManager;

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

	// Forestry API shims, so we don't have to do null checks all over the place.

	public abstract static class AbstractManagerShim implements ICraftingProvider
	{
		private static Map<Object[], Object[]> map = new HashMap<Object[], Object[]>();

		public Map<Object[], Object[]> getRecipes()
		{
			return map;
		}
	}

	public static class CarpenterManagerShim extends AbstractManagerShim implements ICarpenterManager
	{
		@Override
		public void addRecipe(ItemStack box, ItemStack product, Object... materials) {}

		@Override
		public void addRecipe(int packagingTime, ItemStack box, ItemStack product, Object... materials) {}

		@Override
		public void addRecipe(int packagingTime, FluidStack liquid, ItemStack box, ItemStack product, Object... materials) {}
	}

	public static class CentrifugeManagerShim extends AbstractManagerShim implements ICentrifugeManager
	{
		@Override
		public void addRecipe(ICentrifugeRecipe recipe) {}

		@Override
		public void addRecipe(int timePerItem, ItemStack input, Map<ItemStack, Float> products) {}
	}

	public static class FabricatorManagerShim extends AbstractManagerShim implements IFabricatorManager
	{
		@Override
		public void addRecipe(IFabricatorRecipe recipe) {}

		@Override
		public void addRecipe(ItemStack plan, FluidStack molten, ItemStack result, Object[] pattern) {}

		@Override
		public void addSmelting(ItemStack resource, FluidStack molten, int meltingPoint) {}
	}

	public static class FermenterManagerShim extends AbstractManagerShim implements IFermenterManager
	{
		@Override
		public void addRecipe(ItemStack resource, int fermentationValue, float modifier, FluidStack output, FluidStack liquid) {}

		@Override
		public void addRecipe(ItemStack resource, int fermentationValue, float modifier, FluidStack output) {}
	}

	public static class MoistenerManagerShim extends AbstractManagerShim implements IMoistenerManager
	{
		@Override
		public void addRecipe(ItemStack resource, ItemStack product, int timePerItem) {}
	}

	public static class SqueezerManagerShim extends AbstractManagerShim implements ISqueezerManager
	{
		@Override
		public void addRecipe(int timePerItem, ItemStack[] resources, FluidStack liquid, ItemStack remnants, int chance) {}

		@Override
		public void addRecipe(int timePerItem, ItemStack[] resources, FluidStack liquid) {}

		@Override
		public void addContainerRecipe(int timePerItem, ItemStack emptyContainer, @Nullable ItemStack remnants, float chance) {}
	}

	public static class StillManagerShim extends AbstractManagerShim implements IStillManager
	{
		@Override
		public void addRecipe(int cyclesPerUnit, FluidStack input, FluidStack output) {}
	}

	public static class RecipeManagersShims
	{
		private static RecipeManagersShims INSTANCE;
		public Collection<ICraftingProvider> craftingProviders = ObjectUtils.<Collection<ICraftingProvider>>maybe(RecipeManagers.craftingProviders, new ArrayList<ICraftingProvider>());
		public ICarpenterManager carpenterManager = ObjectUtils.<ICarpenterManager>maybe(RecipeManagers.carpenterManager, new CarpenterManagerShim());
		public ICentrifugeManager centrifugeManager = ObjectUtils.<ICentrifugeManager>maybe(RecipeManagers.centrifugeManager, new CentrifugeManagerShim());
		public IFabricatorManager fabricatorManager = ObjectUtils.<IFabricatorManager>maybe(RecipeManagers.fabricatorManager, new FabricatorManagerShim());
		public IFermenterManager fermenterManager = ObjectUtils.<IFermenterManager>maybe(RecipeManagers.fermenterManager, new FermenterManagerShim());
		public IMoistenerManager moistenerManager = ObjectUtils.<IMoistenerManager>maybe(RecipeManagers.moistenerManager, new MoistenerManagerShim());
		public ISqueezerManager squeezerManager = ObjectUtils.<ISqueezerManager>maybe(RecipeManagers.squeezerManager, new SqueezerManagerShim());
		public IStillManager stillManager = ObjectUtils.<IStillManager>maybe(RecipeManagers.stillManager, new StillManagerShim());

		public static RecipeManagersShims instance()
		{
			if (INSTANCE == null) INSTANCE = new RecipeManagersShims();
			return INSTANCE;
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

	public ForestryModuleBase(String modid)
	{
		super(modid, "Forestry");
	}

	public IGameMode getActiveMode()
	{
		return ForestryAPI.activeMode;
	}

	public RecipeManagersShims recipes()
	{
		return ForestryRecipeUtils.recipes();
	}

	public void addFarmable(String ns, IFarmable farmable)
	{
		final Collection<IFarmable> farmables = Farmables.farmables.get(ns);
		if (farmables != null) farmables.add(farmable);
	}
}
