package growthcraft.api.cellar;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class PressingRegistry
{
	// because damage is almost never -1
	private final int NO_META = -1;

	class PressResults
	{
		public final Fluid f; //fluid
		public final int t;   //time
		public final int a;   //amount
		public final float r; //residue
		public PressResults(Fluid f, int t, int a, float r)
		{
			this.f = f;
			this.t = t;
			this.a = a;
			this.r = r;
		}
	}

	private Map<List, PressResults> pressingList = new HashMap<List, PressResults>();
	public Map<List, PressResults> getPressingList() { return pressingList; }

	/**
	 * addPressing()
	 *
	 * Example Usage:
	 * CellarRegistry.instance().addPressing(Item.appleRed, appleCider_booze[0], 20, 37, 0.3F);
	 *
	 * @param raw     - The source/input Block/Item/ID
	 * @param meta    - The metadata of @param raw
	 * @param fluid   - The resulting fluid.
	 * @param time    - The time needed for the item/block to be pressed.
	 * @param amount  - The amount of booze the item/block produces.
	 * @param residue - The amount of residue this will produce.
	 */
	public void addPressing(Item raw, int meta, Fluid fluid, int time, int amount, float residue)
	{
		this.pressingList.put(Arrays.asList(raw, meta), new PressResults(fluid, time, amount, residue));
	}

	public void addPressing(Block raw, int meta, Fluid fluid, int time, int amount, float residue)
	{
		this.addPressing(Item.getItemFromBlock(raw), meta, fluid, time, amount, residue);
	}

	public void addPressing(Block raw, int meta, String fluid, int time, int amount, float residue)
	{
		this.addPressing(Item.getItemFromBlock(raw), meta, fluid, time, amount, residue);
	}

	public void addPressing(Item raw, int meta, String fluid, int time, int amount, float residue)
	{
		if (FluidUtils.doesFluidExist(fluid))
		{
			this.addPressing(raw, meta, FluidRegistry.getFluid(fluid), time, amount, residue);
		}
	}

	/**
	 * addPressing()
	 *
	 * Example Usage:
	 * CellarRegistry.instance().addPressing(Item.appleRed, appleCider_booze[0], 20, 37, 0.3F);
	 *
	 * @param raw     - The source/input Block/Item/ID
	 * @param fluid   - The resulting fluid.
	 * @param time    - The time needed for the item/block to be pressed.
	 * @param amount  - The amount of booze the item/block produces.
	 * @param residue - The amount of residue this will produce.
	 */
	public void addPressing(Item raw, Fluid fluid, int time, int amount, float residue)
	{
		addPressing(raw, NO_META, fluid, time, amount, residue);
	}

	public void addPressing(Item raw, String fluid, int time, int amount, float residue)
	{
		addPressing(raw, NO_META, fluid, time, amount, residue);
	}

	public void addPressing(Block raw, Fluid fluid, int time, int amount, float residue)
	{
		addPressing(Item.getItemFromBlock(raw), NO_META, fluid, time, amount, residue);
	}

	public void addPressing(Block raw, String fluid, int time, int amount, float residue)
	{
		addPressing(Item.getItemFromBlock(raw), NO_META, fluid, time, amount, residue);
	}

	public boolean isPressingRecipe(ItemStack itemstack)
	{
		return this.getPressingResults(itemstack)!= null;
	}

	public PressResults getPressingResults(ItemStack itemstack)
	{
		if (itemstack == null)
		{
			return null;
		}
		PressResults ret = (PressResults)pressingList.get(Arrays.asList(itemstack.getItem(), itemstack.getItemDamage()));
		if (ret != null)
		{
			return ret;
		}
		return (PressResults)pressingList.get(Arrays.asList(itemstack.getItem(), NO_META));
	}

	public FluidStack getPressingFluidStack(ItemStack itemstack)
	{
		PressResults pressresults = this.getPressingResults(itemstack);
		if (itemstack == null || pressresults == null)
		{
			return null;
		}
		return new FluidStack(pressresults.f, 1);
	}

	public int getPressingTime(ItemStack itemstack)
	{
		PressResults pressresults = this.getPressingResults(itemstack);
		if (itemstack == null || pressresults == null)
		{
			return 0;
		}
		return pressresults.t;
	}

	public int getPressingAmount(ItemStack itemstack)
	{
		PressResults pressresults = this.getPressingResults(itemstack);
		if (itemstack == null || pressresults == null)
		{
			return 0;
		}
		return pressresults.a;
	}

	public float getPressingResidue(ItemStack itemstack)
	{
		PressResults pressresults = this.getPressingResults(itemstack);
		if (itemstack == null || pressresults == null)
		{
			return 0.0F;
		}
		return pressresults.r;
	}
}
