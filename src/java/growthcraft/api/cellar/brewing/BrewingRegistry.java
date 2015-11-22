package growthcraft.api.cellar.brewing;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.util.HashKey;
import growthcraft.api.core.util.ItemKey;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BrewingRegistry
{
	public static class BrewingKey extends HashKey
	{
		public Fluid fluid;
		public Item item;
		public int meta;

		public BrewingKey(@Nonnull Fluid flu, @Nonnull Item ite, int met)
		{
			super();
			this.fluid = flu;
			this.item = ite;
			this.meta = met;
			generateHashCode();
		}

		public BrewingKey(@Nonnull Fluid flu, @Nonnull ItemStack stack)
		{
			this(flu, stack.getItem(), stack.getItemDamage());
		}

		public void generateHashCode()
		{
			this.hash = fluid.hashCode();
			this.hash = 31 * hash + item.hashCode();
			this.hash = 31 * hash + meta;
		}
	}

	private HashMap<BrewingKey, BrewingResult> brewingList = new HashMap<BrewingKey, BrewingResult>();
	private List<ItemKey> brewingIngredients = new ArrayList<ItemKey>();

	public Map<BrewingKey, BrewingResult> getBrewingList()
	{
		return brewingList;
	}

	public List<ItemKey> getBrewingIngredients()
	{
		return brewingIngredients;
	}

	public Fluid boozeToKey(Fluid f)
	{
		return CellarRegistry.instance().booze().maybeAlternateBooze(f);
	}

	/**
	 * addBrewing()
	 *
	 * Example Usage:
	 * CellarRegistry.instance().addBrewing(FluidRegistry.WATER, Item.wheat, hopAle_booze, 20, 37, 0.3F);
	 *
	 * @param sourceFluid - The source Fluid.
	 * @param raw         - The source/input ItemStack.
	 * @param resultFluid - The resulting Fluid.
	 * @param time        - The time needed for the item/block to be brewed.
	 * @param residue     - The amount of residue this will produce.
	 */
	public void addBrewing(@Nonnull FluidStack sourceFluid, @Nonnull ItemStack raw, @Nonnull FluidStack resultFluid, int time, Residue residue)
	{
		this.brewingList.put(new BrewingKey(sourceFluid.getFluid(), raw), new BrewingResult(sourceFluid, resultFluid, time, residue));
		this.brewingIngredients.add(new ItemKey(raw));
	}

	public BrewingResult getBrewingResult(FluidStack fluidstack, ItemStack itemstack)
	{
		if (itemstack == null || fluidstack == null) return null;

		final Fluid f = boozeToKey(fluidstack.getFluid());
		final BrewingResult ret = brewingList.get(new BrewingKey(f, itemstack.getItem(), itemstack.getItemDamage()));
		if (ret != null) return ret;

		return brewingList.get(new BrewingKey(f, itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}

	public boolean isBrewingRecipe(FluidStack fluidstack, ItemStack itemstack)
	{
		return getBrewingResult(fluidstack, itemstack) != null;
	}

	public boolean isItemBrewingIngredient(ItemStack itemstack)
	{
		if (itemstack == null) return false;

		return brewingIngredients.contains(new ItemKey(itemstack)) ||
			brewingIngredients.contains(new ItemKey(itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}

	public FluidStack getBrewingFluidStack(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return null;

		return brewresults.asFluidStack(1);
	}

	public int getBrewingTime(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return 0;

		return brewresults.time;
	}

	public int getBrewingAmount(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return 0;

		return brewresults.getAmount();
	}

	public Residue getBrewingResidue(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return null;

		return brewresults.residue;
	}
}
