package growthcraft.api.cellar;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;

/**
 * Gwafu:
 *
 * Main API. Consult the javadocs of each functions for more information.
 *
 **/
public class CellarRegistry
{

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

	private static final CellarRegistry INSTANCE = new CellarRegistry();

	private final BoozeRegistry boozeRegistry = new BoozeRegistry();
	private final BrewRegistry brewRegistry = new BrewRegistry();
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

	public BrewRegistry brew()
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
