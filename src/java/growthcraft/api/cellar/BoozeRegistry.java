package growthcraft.api.cellar;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Example Usage: Creating Boozes
 *
 * Create a fluid array with a length of 4 (minimum, you can go over bu not under).
 * Don't forget to register it to the FluidRegistry!
 *
 * {@code
 *		appleCider_booze = new Booze[4];
 *		for (int i = 0; i < appleCider_booze.length; ++i)
 *		{
 *			appleCider_booze[i]  = (new Booze("grc.appleCider" + i));
 *			FluidRegistry.registerFluid(appleCider_booze[i]);
 *		}
 * }
 *
 * Use the function createBooze() to register the fluid you just created into Cellar.
 *
 * {@code
 *		CellarRegistry.instance().createBooze(appleCider_booze, this.color, "fluid.grc.appleCider");
 * }
 *
 * Thats it!
 * For fluid containers, just google how to make them.
 * Anyway, here's an example.
 * {@code
 * 		FluidStack fluidstack = new FluidStack(appleCider_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
 * 		FluidContainerRegistry.registerFluidContainer(fluidstack, new ItemStack(appleCider_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);
 * }
 */
public class BoozeRegistry
{
	// because damage is almost never -1
	private final int NO_META = -1;

	private Map<Integer, List> boozeMap = new HashMap<Integer, List>();
	private Map<Fluid[], String> boozeNames = new HashMap<Fluid[], String>();
	private Map<Integer, Integer> altBoozeMap = new HashMap<Integer, Integer>();

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

	/**
	 * @param f - source fluid
	 * @return if an alternate booze exists, that will be returned, else returns the fluid passed in
	 */
	public Fluid maybeAlternateBooze(Fluid f)
	{
		if (this.isAlternateBooze(f))
		{
			return FluidRegistry.getFluid(this.altBoozeMap.get(f.getID()));
		}
		return f;
	}
}
