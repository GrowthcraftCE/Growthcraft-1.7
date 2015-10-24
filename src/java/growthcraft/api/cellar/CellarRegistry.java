package growthcraft.api.cellar;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.brewing.BrewingRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.api.cellar.heatsource.HeatSourceRegistry;
import growthcraft.api.cellar.heatsource.IHeatSourceRegistry;
import growthcraft.api.cellar.pressing.PressingRegistry;

import net.minecraft.block.Block;

/**
 * Gwafu:
 *
 * Main API. Consult the javadocs of each functions for more information.
 *
 **/
public class CellarRegistry
{
	private static final CellarRegistry INSTANCE = new CellarRegistry();

	private final BoozeRegistry boozeRegistry = new BoozeRegistry();
	private final BrewingRegistry brewingRegistry = new BrewingRegistry();
	private final PressingRegistry pressingRegistry = new PressingRegistry();
	private final FermentingRegistry fermentingRegistry = new FermentingRegistry();
	private final IHeatSourceRegistry heatSourceRegistry = new HeatSourceRegistry();

	public static final CellarRegistry instance()
	{
		return INSTANCE;
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
