package growthcraft.api.cellar.booze;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.cellar.util.FluidUtils;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BoozeRegistry implements IBoozeRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private Map<Fluid, BoozeEntry> boozeMap = new HashMap<Fluid, BoozeEntry>();
	private Map<Fluid, Fluid> altBoozeMap = new HashMap<Fluid, Fluid>();

	public void setLogger(ILogger l)
	{
		this.logger = l;
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

	@Override
	@Nullable
	public BoozeEntry getBoozeEntry(Fluid fluid)
	{
		if (fluid == null) return null;
		return boozeMap.get(fluid);
	}

	@Override
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

	@Override
	@Nullable
	public BoozeEffect getEffect(Fluid fluid)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		return entry != null ? entry.getEffect() : null;
	}

	@Override
	public boolean isFluidBooze(Fluid f)
	{
		if (f == null) return false;
		return getBoozeEntry(f) != null;
	}

	@Override
	public boolean isFluidBooze(FluidStack fluidStack)
	{
		if (fluidStack == null) return false;
		return isFluidBooze(fluidStack.getFluid());
	}

	protected void registerBooze(@Nonnull Fluid fluid, @Nonnull BoozeEntry entry)
	{
		boozeMap.put(fluid, entry);
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
	@Override
	public void registerBooze(@Nonnull Fluid fluid)
	{
		ensureFluidIsValid(fluid);

		if (!isFluidBooze(fluid))
		{
			logger.info("Registering booze %s", fluid.getName());
			registerBooze(fluid, new BoozeEntry(fluid));
		}
		else
		{
			throw new IllegalArgumentException("[Growthcraft|Cellar] The fluid being registered as a Booze is already registered to the CellarRegistry.");
		}
	}

	@Override
	public void addBoozeAlternative(@Nonnull Fluid altfluid, @Nonnull Fluid fluid)
	{
		if (FluidUtils.doesFluidExist(altfluid))
		{
			if (isFluidBooze(fluid))
			{
				logger.info("Aliasing booze %s as %s", fluid.getName(), altfluid.getName());
				altBoozeMap.put(altfluid, fluid);
				registerBooze(altfluid, getBoozeEntry(fluid));
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

	@Override
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

	@Override
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

	@Override
	public boolean isAlternateBooze(Fluid f)
	{
		if (f == null)
		{
			return false;
		}
		return altBoozeMap.get(f) != null;
	}

	@Override
	public Fluid getAlternateBooze(Fluid f)
	{
		if (isAlternateBooze(f))
		{
			return altBoozeMap.get(f);
		}
		return null;
	}

	@Override
	public Fluid maybeAlternateBooze(Fluid f)
	{
		final Fluid alt = getAlternateBooze(f);
		return alt != null ? alt : f;
	}

	@Override
	public FluidStack maybeAlternateBoozeStack(FluidStack stack)
	{
		return new FluidStack(maybeAlternateBooze(stack.getFluid()), stack.amount);
	}

	@Override
	public void addTags(@Nonnull Fluid fluid, BoozeTag... tags)
	{
		fetchBoozeEntry(fluid).addTags(tags);
	}

	@Override
	@Nullable
	public Collection<BoozeTag> getTags(Fluid fluid)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		return entry != null ? entry.getTags() : null;
	}

	@Override
	@Nullable
	public Collection<BoozeTag> getTags(FluidStack stack)
	{
		if (stack == null) return null;
		return getTags(stack.getFluid());
	}

	@Override
	public boolean hasTags(Fluid fluid, BoozeTag... tags)
	{
		final BoozeEntry entry = getBoozeEntry(fluid);
		if (entry != null)
		{
			return entry.hasTags(tags);
		}
		return false;
	}

	@Override
	public boolean hasTags(FluidStack stack, BoozeTag... tags)
	{
		if (stack == null) return false;
		return hasTags(stack.getFluid(), tags);
	}
}
