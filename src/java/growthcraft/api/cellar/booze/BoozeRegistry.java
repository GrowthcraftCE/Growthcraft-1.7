package growthcraft.api.cellar.booze;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

import growthcraft.api.cellar.util.FluidUtils;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
 * 		FluidStack fluidstack = new FluidStack(appleCider_booze[i], FluidContainerRegistry.BUCKET_VOLUME);
 * 		FluidContainerRegistry.registerFluidContainer(fluidstack, new ItemStack(appleCider_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);
 * }
 */
public class BoozeRegistry
{
	static class BoozeEntry
	{
		final Fluid[] fluids;
		final int index;

		public BoozeEntry(Fluid[] flus, int ind)
		{
			this.fluids = flus;
			this.index = ind;
		}
	}

	// because damage is almost never -1
	private final int NO_META = -1;

	// May consider using an enum instead of a String once the features settle a bit
	private Map<Fluid, Set<String>> fluidTags = new HashMap<Fluid, Set<String>>();
	private Map<Fluid, BoozeEntry> boozeMap = new HashMap<Fluid, BoozeEntry>();
	private Map<Fluid[], String> boozeNames = new HashMap<Fluid[], String>();
	private Map<Fluid, Fluid> altBoozeMap = new HashMap<Fluid, Fluid>();

	private void ensureFluidsAreValid(Fluid[] fluids)
	{
		if (fluids.length < 4)
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] One of the fluids being created as Booze has an array length of " + fluids.length + ". The array lengths should be 4 or more.");
		}

		if (!FluidUtils.doesFluidsExist(fluids))
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] One of the fluids being created as Booze is not registered to the FluidRegistry.");
		}
	}

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
	public void createBooze(@Nonnull Fluid[] fluids, String unlocalizedName)
	{
		ensureFluidsAreValid(fluids);

		if (!areFluidsBooze(fluids))
		{
			for (int i = 0; i < fluids.length; ++i)
			{
				boozeMap.put(fluids[i], new BoozeEntry(fluids, i));
			}
			boozeNames.put(fluids, unlocalizedName);
		}
		else
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] One of the fluids being created as Booze is already registered to the CellarRegistry.");
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
	public void addBoozeAlternative(@Nonnull Fluid altfluid, @Nonnull Fluid fluid)
	{
		if (FluidUtils.doesFluidExist(altfluid))
		{
			if (isFluidBooze(fluid))
			{
				altBoozeMap.put(altfluid, fluid);
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
			addBoozeAlternative(altfluid, FluidRegistry.getFluid(fluid));
		}
	}

	public void addBoozeAlternative(String altfluid, String fluid)
	{
		if (FluidUtils.doesFluidExist(altfluid) && FluidUtils.doesFluidExist(fluid))
		{
			addBoozeAlternative(FluidRegistry.getFluid(altfluid), FluidRegistry.getFluid(fluid));
		}
	}

	// BOOZE /////////////////////////////////////////////////////////
	public boolean isFluidBooze(Fluid f)
	{
		if (f == null) return false;
		return boozeMap.get(f) != null || isAlternateBooze(f);
	}

	public boolean isFluidBooze(FluidStack fluidStack)
	{
		if (fluidStack == null) return false;
		return isFluidBooze(fluidStack.getFluid());
	}

	public boolean areFluidsBooze(Fluid[] fluid)
	{
		for (int i = 0; i < fluid.length; ++i)
		{
			if (!isFluidBooze(fluid[i]))
			{
				return false;
			}
		}
		return true;
	}

	public Fluid[] getBoozeArray(Fluid f)
	{
		if (isAlternateBooze(f))
		{
			final Fluid alt = getAlternateBooze(f);
			return boozeMap.get(alt).fluids;
		}
		return boozeMap.get(f).fluids;
	}

	public int getBoozeIndex(Fluid f)
	{
		if (isFluidBooze(f))
		{
			if (isAlternateBooze(f))
			{
				final Fluid alt = getAlternateBooze(f);
				return boozeMap.get(alt).index;
			}
			return boozeMap.get(f).index;
		}
		return 0;
	}

	public String getBoozeName(Fluid[] f)
	{
		if (areFluidsBooze(f))
		{
			return boozeNames.get(f);
		}
		return "";
	}

	public boolean isAlternateBooze(Fluid f)
	{
		if (f == null)
		{
			return false;
		}
		return altBoozeMap.get(f) != null;
	}

	public Fluid getAlternateBooze(Fluid f)
	{
		if (isAlternateBooze(f))
		{
			return altBoozeMap.get(f);
		}
		return null;
	}

	/**
	 * @param f - source fluid
	 * @return if an alternate booze exists, that will be returned, else returns the fluid passed in
	 */
	public Fluid maybeAlternateBooze(Fluid f)
	{
		if (isAlternateBooze(f))
		{
			return this.altBoozeMap.get(f);
		}
		return f;
	}

	public FluidStack maybeAlternateBoozeStack(FluidStack stack)
	{
		return new FluidStack(maybeAlternateBooze(stack.getFluid()), stack.amount);
	}

	public void addTags(@Nonnull Fluid fluid, String... tags)
	{
		if (!fluidTags.containsKey(fluid))
		{
			fluidTags.put(fluid, new HashSet<String>());
		}
		Set<String> setTags = fluidTags.get(fluid);
		for (String tag : tags)
		{
			setTags.add(tag);
		}
	}

	public Set<String> getTags(Fluid fluid)
	{
		if (fluid == null) return null;
		return fluidTags.get(fluid);
	}

	public Set<String> getTags(FluidStack stack)
	{
		if (stack == null) return null;
		return getTags(stack.getFluid());
	}

	public boolean hasTags(Fluid fluid, String... tags)
	{
		Set<String> setTags = getTags(fluid);
		if (setTags != null)
		{
			for (String tag : tags)
			{
				if (!setTags.contains(tag)) return false;
			}
			return true;
		}
		return false;
	}

	public boolean hasTags(FluidStack stack, String... tags)
	{
		if (stack == null) return false;
		return hasTags(stack.getFluid(), tags);
	}
}
