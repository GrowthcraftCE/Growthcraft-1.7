package growthcraft.api.cellar;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BrewingRegistry;
import growthcraft.api.cellar.booze.PressingRegistry;
import growthcraft.api.cellar.booze.HeatSourceRegistry;
import growthcraft.api.cellar.booze.IHeatSourceRegistry;

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
	private final BrewingRegistry brewRegistry = new BrewingRegistry();
	private final PressingRegistry pressingRegistry = new PressingRegistry();
	private final IHeatSourceRegistry heatSourceRegistry = new HeatSourceRegistry();

	public static final CellarRegistry instance()
	{
		return INSTANCE;
	}

	public BoozeRegistry booze()
	{
		return boozeRegistry;
	}

	public BrewingRegistry brew()
	{
		return brewRegistry;
	}

	public PressingRegistry pressing()
	{
		return pressingRegistry;
	}

	public IHeatSourceRegistry heatSource()
	{
		return heatSourceRegistry;
	}
}
