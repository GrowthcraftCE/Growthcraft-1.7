package growthcraft.api.cellar.booze;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.fluids.FluidTag;
import growthcraft.api.core.fluids.FluidUtils;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BoozeRegistry implements IBoozeRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private Map<Fluid, BoozeEntry> boozeMap = new HashMap<Fluid, BoozeEntry>();
	private Map<FluidTag, IModifierFunction> tagModifierFunctions = new HashMap<FluidTag, IModifierFunction>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	public IModifierFunction getModifierFunction(@Nullable FluidTag tag)
	{
		return tagModifierFunctions.get(tag);
	}

	public void setModifierFunction(@Nonnull FluidTag tag, IModifierFunction func)
	{
		tagModifierFunctions.put(tag, func);
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
}
