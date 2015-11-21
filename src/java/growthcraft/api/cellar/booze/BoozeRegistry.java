package growthcraft.api.cellar.booze;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;
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
public class BoozeRegistry implements ILoggable
{
	private ILogger logger = NullLogger.INSTANCE;
	private Map<String, IModifierFunction> modifiers = new HashMap<String, IModifierFunction>();
	private Map<Fluid, BoozeEntry> boozeMap = new HashMap<Fluid, BoozeEntry>();
	private Map<Fluid, Fluid> altBoozeMap = new HashMap<Fluid, Fluid>();

	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	public void setModifierFunction(String name, IModifierFunction func)
	{
		modifiers.put(name, func);
	}

	public IModifierFunction getModifierFunction(String name)
	{
		return modifiers.get(name);
	}

	public Collection<BoozeEntry> getBoozeEntries()
	{
		return boozeMap.values();
	}

	private void ensureFluidIsValid(Fluid fluid)
	{
		if (!FluidUtils.doesFluidExist(fluid))
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] The fluid being registered as a Booze is not registered to the FluidRegistry.");
		}
	}

	@Nullable
	public BoozeEntry getBoozeEntry(Fluid fluid)
	{
		if (fluid == null) return null;
		return boozeMap.get(fluid);
	}

	@Nonnull
	public BoozeEntry fetchBoozeEntry(Fluid fluid)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		if (entry == null)
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] The fluid being tagged does not have a valid booze entry.");
		}
		return entry;
	}

	@Nullable
	public BoozeEffect getEffect(Fluid fluid)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		return entry != null ? entry.getEffect() : null;
	}

	public boolean isFluidBooze(Fluid f)
	{
		if (f == null) return false;
		return getBoozeEntry(f) != null || isAlternateBooze(f);
	}

	public boolean isFluidBooze(FluidStack fluidStack)
	{
		if (fluidStack == null) return false;
		return isFluidBooze(fluidStack.getFluid());
	}

	/**
	 * Registers a Booze to the CellarRegistry.
	 *
	 * Example Usage:
	 * CellarRegistry.instance().registerBooze(new Booze().setColor(0xFFAABB));
	 *
	 * @param fluid           - The fluid to be registered.
	 * @param color           - The color of the fluid.
	 **/
	public void registerBooze(@Nonnull Fluid fluid)
	{
		ensureFluidIsValid(fluid);

		if (!isFluidBooze(fluid))
		{
			logger.info("Registering booze %s", fluid.getName());
			boozeMap.put(fluid, new BoozeEntry(fluid));
		}
		else
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] The fluid being registered as a Booze is already registered to the CellarRegistry.");
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
				logger.info("Registering alt-booze %s as %s", altfluid, fluid);
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
		else
		{
			logger.error("Fluid %s does not exist, cannot add alternative booze!", fluid);
		}
	}

	public void addBoozeAlternative(String altfluid, String fluid)
	{
		if (!FluidUtils.doesFluidExist(altfluid))
		{
			logger.error("Fluid %s does not exist, cannot add alternative booze!", altfluid);
			return;
		}

		if (!FluidUtils.doesFluidExist(fluid))
		{
			logger.error("Fluid %s does not exist, cannot add alternative booze!", fluid);
			return;
		}

		addBoozeAlternative(FluidRegistry.getFluid(altfluid), FluidRegistry.getFluid(fluid));
	}

	// BOOZE /////////////////////////////////////////////////////////
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
		final Fluid alt = getAlternateBooze(f);
		return alt != null ? alt : f;
	}

	public FluidStack maybeAlternateBoozeStack(FluidStack stack)
	{
		return new FluidStack(maybeAlternateBooze(stack.getFluid()), stack.amount);
	}

	public void addTags(@Nonnull Fluid fluid, String... tags)
	{
		fetchBoozeEntry(fluid).addTags(tags);
	}

	@Nullable
	public Collection<String> getTags(Fluid fluid)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		return entry != null ? entry.getTags() : null;
	}

	@Nullable
	public Collection<String> getTags(FluidStack stack)
	{
		if (stack == null) return null;
		return getTags(stack.getFluid());
	}

	public boolean hasTags(Fluid fluid, String... tags)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		if (entry != null)
		{
			return entry.hasTags(tags);
		}
		return false;
	}

	public boolean hasTags(FluidStack stack, String... tags)
	{
		if (stack == null) return false;
		return hasTags(stack.getFluid(), tags);
	}
}
