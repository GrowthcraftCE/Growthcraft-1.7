package growthcraft.api.cellar;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BrewRegistry
{
	class BrewResults
	{
		public final Fluid f; //fluid
		public final int t;   //time
		public final int a;   //amount
		public final float r; //residue
		public BrewResults(Fluid f, int t, int a, float r)
		{
			this.f = f;
			this.t = t;
			this.a = a;
			this.r = r;
		}
	}

	// because damage is almost never -1
	private final int NO_META = -1;
	private HashMap<List, BrewResults> brewingList = new HashMap<List, BrewResults>();
	private List<List> brewingIngredients = new ArrayList<List>();

	public Map<List, BrewResults> getBrewingList() { return brewingList; }
	public List<List> getBrewingIngredients() { return brewingIngredients; }

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
	public void addBrewing(Fluid sourceFluid, Item raw, int meta, Fluid resultFluid, int time, int amount, float residue)
	{
		this.brewingList.put(Arrays.asList(sourceFluid, raw, meta), new BrewResults(resultFluid, time, amount, residue));
		this.brewingIngredients.add(Arrays.asList(raw, meta));
	}

	public void addBrewing(Fluid sourceFluid, Block raw, int meta, Fluid resultFluid, int time, int amount, float residue)
	{
		addBrewing(sourceFluid, Item.getItemFromBlock(raw), meta, resultFluid, time, amount, residue);
	}

	public void addBrewing(String sourceFluid, Item raw, int meta, String resultFluid, int time, int amount, float residue)
	{
		if (FluidUtils.doesFluidExist(sourceFluid) && FluidUtils.doesFluidExist(resultFluid))
		{
			addBrewing(FluidRegistry.getFluid(sourceFluid), raw, meta, FluidRegistry.getFluid(resultFluid), time, amount, residue);
		}
	}

	public void addBrewing(String sourceFluid, Block raw, int meta, String resultFluid, int time, int amount, float residue)
	{
		addBrewing(sourceFluid, Item.getItemFromBlock(raw), meta, resultFluid, time, amount, residue);
	}

	public void addBrewing(Fluid sourceFluid, Item raw, Fluid resultFluid, int time, int amount, float residue)
	{
		addBrewing(sourceFluid, raw, NO_META, resultFluid, time, amount, residue);
	}

	public void addBrewing(Fluid sourceFluid, Block raw, Fluid resultFluid, int time, int amount, float residue)
	{
		addBrewing(sourceFluid, raw, NO_META, resultFluid, time, amount, residue);
	}

	public void addBrewing(String sourceFluid, Item raw, String resultFluid, int time, int amount, float residue)
	{
		addBrewing(sourceFluid, raw, NO_META, resultFluid, time, amount, residue);
	}

	public void addBrewing(String sourceFluid, Block raw, String resultFluid, int time, int amount, float residue)
	{
		addBrewing(sourceFluid, raw, NO_META, resultFluid, time, amount, residue);
	}

	public boolean isBrewingRecipe(FluidStack fluidstack, ItemStack itemstack)
	{
		return this.getBrewingResults(fluidstack, itemstack)!= null;
	}

	public BrewResults getBrewingResults(FluidStack fluidstack, ItemStack itemstack)
	{
		if (itemstack == null || fluidstack == null)
		{
			return null;
		}
		Fluid f = CellarRegistry.instance().booze().maybeAlternateBooze(fluidstack.getFluid());
		BrewResults ret = brewingList.get(Arrays.asList(f, itemstack.getItem(), itemstack.getItemDamage()));
		if (ret != null)
		{
			return ret;
		}
		return brewingList.get(Arrays.asList(f, itemstack.getItem(), NO_META));
	}

	public boolean isItemBrewingIngredient(ItemStack itemstack)
	{
		if (itemstack == null)
		{
			return false;
		}
		return brewingIngredients.contains(Arrays.asList(itemstack.getItem(), itemstack.getItemDamage())) ||
			brewingIngredients.contains(Arrays.asList(itemstack.getItem(), NO_META));
	}

	public FluidStack getBrewingFluidStack(FluidStack fluidstack, ItemStack itemstack)
	{
		BrewResults brewresults = this.getBrewingResults(fluidstack, itemstack);
		if (itemstack == null || fluidstack == null || brewresults == null)
		{
			return null;
		}
		return new FluidStack(brewresults.f, 1);
	}

	public int getBrewingTime(FluidStack fluidstack, ItemStack itemstack)
	{
		BrewResults brewresults = this.getBrewingResults(fluidstack, itemstack);
		if (itemstack == null || fluidstack == null || brewresults == null)
		{
			return 0;
		}
		return brewresults.t;
	}

	public int getBrewingAmount(FluidStack fluidstack, ItemStack itemstack)
	{
		BrewResults brewresults = this.getBrewingResults(fluidstack, itemstack);
		if (itemstack == null || fluidstack == null || brewresults == null)
		{
			return 0;
		}
		return brewresults.a;
	}

	public float getBrewingResidue(FluidStack fluidstack, ItemStack itemstack)
	{
		BrewResults brewresults = this.getBrewingResults(fluidstack, itemstack);
		if (itemstack == null || fluidstack == null || brewresults == null)
		{
			return 0.0F;
		}
		return brewresults.r;
	}
}
