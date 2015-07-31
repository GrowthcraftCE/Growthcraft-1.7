package growthcraft.api.cellar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class CellarRegistry
{
	/**
	 * Gwafu:
	 *
	 * Main API. Consult the javadocs of each functions for more information.
	 *
	 **/
	/** Example Usage: Creating Boozes **/
	/**
	 * Create a fluid array with a length of 4 (minimum, you can go over bu not under).
	 * Don't forget to register it to the FluidRegistry!
	 **/
	//appleCider_booze = new Booze[4];
	//for (int i = 0; i < appleCider_booze.length; ++i)
	//{
	//	appleCider_booze[i]  = (new Booze("grc.appleCider" + i));
	//	FluidRegistry.registerFluid(appleCider_booze[i]);
	//}
	/**
	 * Use the function createBooze() to register the fluid you just created into Cellar.
	 **/
	//CellarRegistry.instance().createBooze(appleCider_booze, this.color, "fluid.grc.appleCider");
	/**
	 * Thats it!
	 * For fluid containers, just google how to make them.
	 * Anyway, here's an example.
	 */
	//FluidStack fluidstack = new FluidStack(appleCider_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
	//FluidContainerRegistry.registerFluidContainer(fluidstack, new ItemStack(appleCider_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);
	/** Example end **/


	/**
	 * Fluid strings (for use with the 'String' versions of the methods.
	 *
	 * Apple Cider
	 * - "grc.appleCider0' - young
	 * - "grc.appleCider1' - fermented
	 * - "grc.appleCider2' - fermented, potent
	 * - "grc.appleCider3' - fermented, extended
	 *
	 * Grape Wine
	 * - "grc.grapeWine0' - young
	 * - "grc.grapeWine1' - fermented
	 * - "grc.grapeWine2' - fermented, potent
	 * - "grc.grapeWine3' - fermented, extended
	 *
	 * Ale
	 * - "grc.hopAle0' - hopped, young
	 * - "grc.hopAle1' - hopped, fermented
	 * - "grc.hopAle2' - hopped, fermented, potent
	 * - "grc.hopAle3' - hopped, fermented, extended
	 * - "grc.hopAle4' - no hops, young
	 *
	 * Sake
	 * - "grc.riceSake0' - young
	 * - "grc.riceSake1' - fermented
	 * - "grc.riceSake2' - fermented, potent
	 * - "grc.riceSake3' - fermented, extended
	 *
	 * Mead
	 * - "grc.honeyMead0' - young
	 * - "grc.honeyMead1' - fermented
	 * - "grc.honeyMead2' - fermented, potent
	 * - "grc.honeyMead3' - fermented, extended
	 **/

	private static final CellarRegistry instance = new CellarRegistry();
	public static final CellarRegistry instance()
	{
		return instance;
	}

	//////////////////////////////////////////////////////////////////
	// HEAT SOURCE ///////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	/**
	 * addHeatSource()
	 *
	 * Adds a valid heat source (like fire, lava, etc.)
	 * Currently only used by Brew Kettle.
	 *
	 * @param block - The block. (This is not metadata sensitive, and will never be).
	 **/
	public void addHeatSource(Block block)
	{
		this.heatSourceList.add(block);
	}

	//////////////////////////////////////////////////////////////////
	// BOOZE /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	/**
	 * createBooze()
	 *
	 * Creates a Booze to the CellarRegistry.
	 *
	 * Example Usage:
	 * CellarRegistry.instance().createBooze(appleCider_booze, 8737829, "fluid.grc.appleCider");
	 *
	 * @param fluid           - The fluid array to be registered.
	 * @param color           - The color of the fluids.
	 * @param unlocalizedName - The unlocalized name to be used as the 'main name' of the fluids/boozes.
	 **/
	public void createBooze(Fluid[] fluid, int color, String unlocalizedName)
	{
		if (fluid.length >= 4)
		{
			if (FluidUtils.doesFluidsExist(fluid))
			{
				if (!this.areFluidsBooze(fluid))
				{
					for (int i = 0; i < fluid.length; ++i)
					{
						this.boozeMap.put(fluid[i].getID(), Arrays.asList(fluid, color, i));
					}
					this.boozeNames.put(fluid, unlocalizedName);
				}
				else
				{
					throw new IllegalArgumentException("[Growthcraft|Cellar] One of the fluids being created as Booze is already registered to the CellarRegistry.");
				}
			}
			else
			{
				throw new IllegalArgumentException("[Growthcraft|Cellar] One of the fluids being created as Booze is not registered to the FluidRegistry.");
			}
		}
		else
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] One of the fluids being created as Booze has an array length of " + fluid.length + ". The array lengths should be 4 or more.");
		}
	}

	/**
	 * addBoozeAlternative()
	 *
	 * Adds an alternative fluid to the mod that will act as an alternative for the booze.
	 * You will almost always want to use this if you dont want to go into the trouble of creating boozes.
	 *
	 * Example Usage:
	 * CellarRegistry.instance().addBoozeAlternative(appleCider_booze_alt, appleCider_booze);
	 *
	 * @param altfluid - The alternate fluid.
	 * @param fluid    - The main fluid/booze.
	 **/
	public void addBoozeAlternative(Fluid altfluid, Fluid fluid)
	{
		if (FluidUtils.doesFluidExist(altfluid))
		{
			if (this.isFluidBooze(fluid))
			{
				this.altBoozeMap.put(altfluid.getID(), fluid.getID());
			}
			else
			{
				throw new IllegalArgumentException("[Growthcraft|Cellar] An alternative fluid/booze is being registered to a fluid which is not registered as a booze.");
			}
		}
		else
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] An alternative fluid/booze is being registered which does not exist.");
		}
	}

	// String
	public void addBoozeAlternative(Fluid altfluid, String fluid)
	{
		if (FluidUtils.doesFluidExist(fluid))
		{
			this.addBoozeAlternative(altfluid, FluidRegistry.getFluid(fluid));
		}
	}

	public void addBoozeAlternative(String altfluid, String fluid)
	{
		if (FluidUtils.doesFluidExist(altfluid) && FluidUtils.doesFluidExist(fluid))
		{
			this.addBoozeAlternative(FluidRegistry.getFluid(altfluid), FluidRegistry.getFluid(fluid));
		}
	}

	////////////////////////////////////////////////////////////////////////
	// FRUIT PRESS /////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
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
	// NON-META
	public void addPressing(Block raw, Fluid fluid, int time, int amount, float residue)
	{
		this.addPressing(Item.getItemFromBlock(raw), fluid, time, amount, residue);
	}

	public void addPressing(Item raw, Fluid fluid, int time, int amount, float residue)
	{
		this.pressingList.put(raw, new PressResults(fluid, time, amount, residue));
	}

	// Fluid Parameters as Strings
	public void addPressing(Block raw, String fluid, int time, int amount, float residue)
	{
		this.addPressing(Item.getItemFromBlock(raw), fluid, time, amount, residue);
	}

	public void addPressing(Item raw, String fluid, int time, int amount, float residue)
	{
		if (FluidUtils.doesFluidExist(fluid))
		{
			this.addPressing(raw, FluidRegistry.getFluid(fluid), time, amount, residue);
		}
	}

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
	public void addPressing(Block raw, int meta, Fluid fluid, int time, int amount, float residue)
	{
		this.addPressing(Item.getItemFromBlock(raw), meta, fluid, time, amount, residue);
	}

	public void addPressing(Item raw, int meta, Fluid fluid, int time, int amount, float residue)
	{
		this.metaPressingList.put(Arrays.asList(raw, meta), new PressResults(fluid, time, amount, residue));
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

	////////////////////////////////////////////////////////////////////////
	// BREW KETTLE /////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	/**
	 * addBrewing()
	 *
	 * Example Usage:
	 * CellarRegistry.instance().addBrewing(FluidRegistry.WATER, Item.wheat, hopAle_booze, 20, 37, 0.3F);
	 *
	 * @param sourceFluid - The source Fluid.
	 * @param raw         - The source/input Item/BlockItemStack.
	 * @param resultFluid - The resulting Fluid.
	 * @param time        - The time needed for the item/block to be brewed.
	 * @param amount      - The amount of booze the item/block produces.
	 * @param residue     - The amount of residue this will produce.
	 */
	//NON-META
	public void addBrewing(Fluid sourceFluid, Block raw, Fluid resultFluid, int time, int amount, float residue)
	{
		this.addBrewing(sourceFluid, Item.getItemFromBlock(raw), resultFluid, time, amount, residue);
	}
	public void addBrewing(Fluid sourceFluid, Item raw, Fluid resultFluid, int time, int amount, float residue)
	{
		this.brewingList.put(Arrays.asList(sourceFluid, raw), new BrewResults(resultFluid, time, amount, residue));
		this.brewingIngredients.add(raw);
	}

	//META
	public void addBrewing(Fluid sourceFluid, Block raw, int meta, Fluid resultFluid, int time, int amount, float residue)
	{
		this.addBrewing(sourceFluid, Item.getItemFromBlock(raw), resultFluid, time, amount, residue);
	}
	public void addBrewing(Fluid sourceFluid, Item raw, int meta, Fluid resultFluid, int time, int amount, float residue)
	{
		this.metaBrewingList.put(Arrays.asList(sourceFluid, raw, meta), new BrewResults(resultFluid, time, amount, residue));
		this.metaBrewingIngredients.add(Arrays.asList(raw, meta));
	}

	// Fluid Parameters as Strings
	//NON-META
	public void addBrewing(String sourceFluid, Block raw, String resultFluid, int time, int amount, float residue)
	{
		this.addBrewing(sourceFluid, Item.getItemFromBlock(raw), resultFluid, time, amount, residue);
	}
	public void addBrewing(String sourceFluid, Item raw, String resultFluid, int time, int amount, float residue)
	{
		if (FluidUtils.doesFluidExist(sourceFluid) && FluidUtils.doesFluidExist(resultFluid))
		{
			this.addBrewing(FluidRegistry.getFluid(sourceFluid), raw, FluidRegistry.getFluid(resultFluid), time, amount, residue);
		}
	}

	//META
	public void addBrewing(String sourceFluid, Block raw, int meta, String resultFluid, int time, int amount, float residue)
	{
		this.addBrewing(sourceFluid, Item.getItemFromBlock(raw), meta, resultFluid, time, amount, residue);
	}
	public void addBrewing(String sourceFluid, Item raw, int meta, String resultFluid, int time, int amount, float residue)
	{
		if (FluidUtils.doesFluidExist(sourceFluid) && FluidUtils.doesFluidExist(resultFluid))
		{
			this.addBrewing(FluidRegistry.getFluid(sourceFluid), raw, meta, FluidRegistry.getFluid(resultFluid), time, amount, residue);
		}
	}

	////////////////////////////////////////////////////////////////////////
	// LISTS AND MAPS ////YOU ARE NOT SUPPOSED TO TOUCH THIS D:<////////////
	////////////////////////////////////////////////////////////////////////

	// HEAT SOURCE ///////////////////////////////////////////////////
	private List<Block> heatSourceList = new ArrayList<Block>();
	public List<Block> getHeatSourceList() { return heatSourceList; }

	// BOOZE /////////////////////////////////////////////////////////
	private HashMap<Integer, List> boozeMap = new HashMap<Integer, List>();
	private HashMap<Fluid[], String> boozeNames = new HashMap<Fluid[], String>();
	private HashMap<Integer, Integer> altBoozeMap = new HashMap<Integer, Integer>();

	// FRUIT PRESS ///////////////////////////////////////////////////
	private Map pressingList = new HashMap();
	private HashMap<List, PressResults> metaPressingList = new HashMap<List, PressResults>();
	public Map getPressingList() { return pressingList; }
	public Map<List, PressResults> getMetaPressingList() { return metaPressingList; }

	// BREW KETTLE ///////////////////////////////////////////////////
	private HashMap<List, BrewResults> brewingList = new HashMap<List, BrewResults>();
	private HashMap<List, BrewResults> metaBrewingList = new HashMap<List, BrewResults>();
	private List brewingIngredients = new ArrayList();
	private List metaBrewingIngredients = new ArrayList();
	public Map<List, BrewResults> getBrewingList() { return brewingList; }
	public Map<List, BrewResults> getMetaBrewinfList() { return metaBrewingList; }
	public List getBrewingIngredients() { return brewingIngredients; }
	public List getMetaBrewingIngredients() { return metaBrewingIngredients; }

	////////////////////////////////////////////////////////////////////////
	// UTIL FUNCTIONS ////YOU ARE NOT SUPPOSED TO TOUCH THIS D:<////////////
	////////////////////////////////////////////////////////////////////////

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

	// HEAT SOURCE ///////////////////////////////////////////////////
	public boolean isBlockHeatSource(Block block)
	{
		return this.heatSourceList.contains(block);
	}

	// BOOZE /////////////////////////////////////////////////////////
	public boolean isFluidBooze(Fluid f)
	{
		//return this.boozeMap.get(f.getID()) != null;
		return this.boozeMap.get(f.getID()) != null || this.isAlternateBooze(f);
	}

	public boolean areFluidsBooze(Fluid[] fluid)
	{
		for (int i = 0; i < fluid.length; ++i)
		{
			if (!this.isFluidBooze(fluid[i]))
			{
				return false;
			}
		}
		return true;
	}

	public Fluid[] getBoozeArray(Fluid f)
	{
		if (this.isAlternateBooze(f))
		{
			Fluid alt = this.getAlternateBooze(f);
			return (Fluid[])this.boozeMap.get(alt.getID()).get(0);
		}
		return (Fluid[])this.boozeMap.get(f.getID()).get(0);
	}

	public int getBoozeColor(Fluid f)
	{
		if (this.isFluidBooze(f))
		{
			return (Integer)this.boozeMap.get(f.getID()).get(1);
		}
		return 16777215;
	}

	public int getBoozeIndex(Fluid f)
	{
		if (this.isFluidBooze(f))
		{
			if (this.isAlternateBooze(f))
			{
				Fluid alt = this.getAlternateBooze(f);
				return (Integer)this.boozeMap.get(alt.getID()).get(2);
			}
			return (Integer)this.boozeMap.get(f.getID()).get(2);
		}
		return 0;
	}

	public String getBoozeName(Fluid[] f)
	{
		if (this.areFluidsBooze(f))
		{
			return (String)this.boozeNames.get(f);
		}
		return "";
	}

	public boolean isAlternateBooze(Fluid f)
	{
		if (f == null)
		{
			return false;
		}
		return this.altBoozeMap.get(f.getID()) != null;
	}

	public Fluid getAlternateBooze(Fluid f)
	{
		if (this.isAlternateBooze(f))
		{
			return FluidRegistry.getFluid(this.altBoozeMap.get(f.getID()));
		}
		return null;
	}

	// FRUIT PRESS ///////////////////////////////////////////////////
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
		PressResults ret = (PressResults)metaPressingList.get(Arrays.asList(itemstack.getItem(), itemstack.getItemDamage()));
		if (ret != null)
		{
			return ret;
		}
		return (PressResults)pressingList.get(itemstack.getItem());
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

	// BREW KETTLE ///////////////////////////////////////////////////
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
		Fluid f = fluidstack.getFluid();
		if (this.isAlternateBooze(f))
		{
			f = this.getAlternateBooze(f);
		}
		BrewResults ret = (BrewResults)metaBrewingList.get(Arrays.asList(f, itemstack.getItem(), itemstack.getItemDamage()));
		if (ret != null)
		{
			return ret;
		}
		return (BrewResults)brewingList.get(Arrays.asList(f, itemstack.getItem()));
	}

	public boolean isItemBrewingIngredient(ItemStack itemstack)
	{
		if (itemstack == null)
		{
			return false;
		}
		boolean ret = metaBrewingIngredients.contains(Arrays.asList(itemstack.getItem(), itemstack.getItemDamage()));
		if (ret)
		{
			return ret;
		}
		return brewingIngredients.contains(itemstack.getItem());
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
