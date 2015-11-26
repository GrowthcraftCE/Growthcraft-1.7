package growthcraft.api.cellar;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.brewing.BrewingRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.api.cellar.heatsource.HeatSourceRegistry;
import growthcraft.api.cellar.heatsource.IHeatSourceRegistry;
import growthcraft.api.cellar.pressing.PressingRegistry;

/**
 * Gwafu:
 *
 * Main API. Consult the javadocs of each functions for more information.
 *
 **/
public class CellarRegistry implements ILoggable
{
	private static final CellarRegistry INSTANCE = new CellarRegistry();

	private ILogger logger = NullLogger.INSTANCE;
	private final BoozeRegistry boozeRegistry = new BoozeRegistry();
	private final BrewingRegistry brewingRegistry = new BrewingRegistry();
	private final PressingRegistry pressingRegistry = new PressingRegistry();
	private final FermentingRegistry fermentingRegistry = new FermentingRegistry();
	private final IHeatSourceRegistry heatSourceRegistry = new HeatSourceRegistry();

	public static final CellarRegistry instance()
	{
		return INSTANCE;
	}

	public void setLogger(ILogger l)
	{
		this.logger = l;
		boozeRegistry.setLogger(logger);
		pressingRegistry.setLogger(logger);
	}

	public BoozeRegistry booze()
	{
		return boozeRegistry;
	}

	public BrewingRegistry brewing()
	{
		return brewingRegistry;
	}

	public PressingRegistry pressing()
	{
		return pressingRegistry;
	}

	public FermentingRegistry fermenting()
	{
		return fermentingRegistry;
	}

	public IHeatSourceRegistry heatSource()
	{
		return heatSourceRegistry;
	}
}
