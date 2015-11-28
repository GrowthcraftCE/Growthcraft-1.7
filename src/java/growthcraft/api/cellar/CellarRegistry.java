package growthcraft.api.cellar;

import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.brewing.BrewingRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.api.cellar.heatsource.HeatSourceRegistry;
import growthcraft.api.cellar.heatsource.IHeatSourceRegistry;
import growthcraft.api.cellar.pressing.IPressingRegistry;
import growthcraft.api.cellar.pressing.PressingRegistry;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

public class CellarRegistry implements ILoggable
{
	private static final CellarRegistry INSTANCE = new CellarRegistry();

	private final BoozeRegistry boozeRegistry = new BoozeRegistry();
	private final BrewingRegistry brewingRegistry = new BrewingRegistry();
	private final IPressingRegistry pressingRegistry = new PressingRegistry();
	private final FermentingRegistry fermentingRegistry = new FermentingRegistry();
	private final IHeatSourceRegistry heatSourceRegistry = new HeatSourceRegistry();
	private ILogger logger = NullLogger.INSTANCE;

	/**
	 * @return current instrance of the CellarRegistry
	 */
	public static final CellarRegistry instance()
	{
		return INSTANCE;
	}

	/**
	 * @param l - logger to set
	 */
	@Override
	public void setLogger(ILogger l)
	{
		this.logger = l;
		boozeRegistry.setLogger(logger);
		pressingRegistry.setLogger(logger);
	}

	/**
	 * @return instance of the BoozeRegistry
	 */
	public BoozeRegistry booze()
	{
		return boozeRegistry;
	}

	/**
	 * @return instance of the BrewingRegistry
	 */
	public BrewingRegistry brewing()
	{
		return brewingRegistry;
	}

	/**
	 * @return instance of the PressingRegistry
	 */
	public IPressingRegistry pressing()
	{
		return pressingRegistry;
	}

	/**
	 * @return instance of the FermentingRegistry
	 */
	public FermentingRegistry fermenting()
	{
		return fermentingRegistry;
	}

	/**
	 * @return instance of the HeatSourceRegistry
	 */
	public IHeatSourceRegistry heatSource()
	{
		return heatSourceRegistry;
	}
}
