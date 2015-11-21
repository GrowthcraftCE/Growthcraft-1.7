package growthcraft.api.cellar.brewing;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.api.core.util.ItemKey;
import growthcraft.api.core.util.HashKey;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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

	/**
	 * addBrewing()
	 *
	 * Example Usage:
	 * CellarRegistry.instance().addBrewing(FluidRegistry.WATER, Item.wheat, hopAle_booze, 20, 37, 0.3F);
	 *
	 * @param sourceFluid - The source Fluid.
	 * @param raw         - The source/input Item/BlockItemStack.
	 * @param meta        - The metadata
	 * @param resultFluid - The resulting Fluid.
	 * @param time        - The time needed for the item/block to be brewed.
	 * @param amount      - The amount of booze the item/block produces.
	 * @param residue     - The amount of residue this will produce.
	 */
	public void addBrewing(@Nonnull Fluid sourceFluid, @Nonnull Item raw, int meta, @Nonnull Fluid resultFluid, int time, int amount, Residue residue)
	{
		this.brewingList.put(new BrewingKey(sourceFluid, raw, meta), new BrewingResult(new FluidStack(resultFluid, amount), time, residue));
		this.brewingIngredients.add(new ItemKey(raw, meta));
	}

	public void addBrewing(Fluid sourceFluid, Block raw, int meta, Fluid resultFluid, int time, int amount, Residue residue)
	{
		addBrewing(sourceFluid, Item.getItemFromBlock(raw), meta, resultFluid, time, amount, residue);
	}

	public void addBrewing(String sourceFluid, Item raw, int meta, String resultFluid, int time, int amount, Residue residue)
	{
		if (FluidUtils.doesFluidExist(sourceFluid) && FluidUtils.doesFluidExist(resultFluid))
		{
			addBrewing(FluidRegistry.getFluid(sourceFluid), raw, meta, FluidRegistry.getFluid(resultFluid), time, amount, residue);
		}
	}

	public void addBrewing(String sourceFluid, Block raw, int meta, String resultFluid, int time, int amount, Residue residue)
	{
		addBrewing(sourceFluid, Item.getItemFromBlock(raw), meta, resultFluid, time, amount, residue);
	}

	public void addBrewing(Fluid sourceFluid, Item raw, Fluid resultFluid, int time, int amount, Residue residue)
	{
		addBrewing(sourceFluid, raw, ItemKey.WILDCARD_VALUE, resultFluid, time, amount, residue);
	}

	public void addBrewing(Fluid sourceFluid, Block raw, Fluid resultFluid, int time, int amount, Residue residue)
	{
		addBrewing(sourceFluid, raw, ItemKey.WILDCARD_VALUE, resultFluid, time, amount, residue);
	}

	public void addBrewing(String sourceFluid, Item raw, String resultFluid, int time, int amount, Residue residue)
	{
		addBrewing(sourceFluid, raw, ItemKey.WILDCARD_VALUE, resultFluid, time, amount, residue);
	}

	public void addBrewing(String sourceFluid, Block raw, String resultFluid, int time, int amount, Residue residue)
	{
		addBrewing(sourceFluid, raw, ItemKey.WILDCARD_VALUE, resultFluid, time, amount, residue);
	}

	public BrewingResult getBrewingResult(FluidStack fluidstack, ItemStack itemstack)
	{
		if (itemstack == null || fluidstack == null) return null;

		final Fluid f = CellarRegistry.instance().booze().maybeAlternateBooze(fluidstack.getFluid());
		final BrewingResult ret = brewingList.get(new BrewingKey(f, itemstack.getItem(), itemstack.getItemDamage()));
		if (ret != null) return ret;

		return brewingList.get(new BrewingKey(f, itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}

	public boolean isBrewingRecipe(FluidStack fluidstack, ItemStack itemstack)
	{
		return this.getBrewingResult(fluidstack, itemstack) != null;
	}

	public boolean isItemBrewingIngredient(ItemStack itemstack)
	{
		if (itemstack == null) return false;

		return brewingIngredients.contains(new ItemKey(itemstack)) ||
			brewingIngredients.contains(new ItemKey(itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}

	public FluidStack getBrewingFluidStack(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = this.getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return null;

		return brewresults.asFluidStack(1);
	}

	public int getBrewingTime(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = this.getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return 0;

		return brewresults.time;
	}

	public int getBrewingAmount(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = this.getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return 0;

		return brewresults.getAmount();
	}

	public Residue getBrewingResidue(FluidStack fluidstack, ItemStack itemstack)
	{
		final BrewingResult brewresults = this.getBrewingResult(fluidstack, itemstack);
		if (brewresults == null) return null;

		return brewresults.residue;
	}
}
