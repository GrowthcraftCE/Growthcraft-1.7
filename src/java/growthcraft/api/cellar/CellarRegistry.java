/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.api.cellar;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.IBoozeRegistry;
import growthcraft.api.cellar.brewing.BrewingRegistry;
import growthcraft.api.cellar.brewing.IBrewingRegistry;
import growthcraft.api.cellar.culturing.CulturingRegistry;
import growthcraft.api.cellar.culturing.ICulturingRegistry;
import growthcraft.api.cellar.distilling.DistilleryRegistry;
import growthcraft.api.cellar.distilling.IDistilleryRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.api.cellar.fermenting.IFermentingRegistry;
import growthcraft.api.cellar.heatsource.HeatSourceRegistry;
import growthcraft.api.cellar.heatsource.IHeatSourceRegistry;
import growthcraft.api.cellar.init.CellarEffects;
import growthcraft.api.cellar.pressing.IPressingRegistry;
import growthcraft.api.cellar.pressing.PressingRegistry;
import growthcraft.api.cellar.yeast.IYeastRegistry;
import growthcraft.api.cellar.yeast.YeastRegistry;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

public class CellarRegistry implements ILoggable
{
	private static final CellarRegistry INSTANCE = new CellarRegistry().initialize();

	private final IBoozeRegistry boozeRegistry = new BoozeRegistry();
	private final IBrewingRegistry brewingRegistry = new BrewingRegistry();
	private final ICulturingRegistry culturingRegistry = new CulturingRegistry();
	private final IDistilleryRegistry distilleryRegistry = new DistilleryRegistry();
	private final IFermentingRegistry fermentingRegistry = new FermentingRegistry();
	private final IHeatSourceRegistry heatSourceRegistry = new HeatSourceRegistry();
	private final IPressingRegistry pressingRegistry = new PressingRegistry();
	private final IYeastRegistry yeastRegistry = new YeastRegistry();
	private ILogger logger = NullLogger.INSTANCE;

	/**
	 * @return current instrance of the CellarRegistry
	 */
	public static final CellarRegistry instance()
	{
		return INSTANCE;
	}

	private CellarRegistry initialize()
	{
		CellarEffects.init();
		return this;
	}

	/**
	 * @param l - logger to set
	 */
	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
		boozeRegistry.setLogger(logger);
		brewingRegistry.setLogger(logger);
		culturingRegistry.setLogger(logger);
		distilleryRegistry.setLogger(logger);
		fermentingRegistry.setLogger(logger);
		heatSourceRegistry.setLogger(logger);
		pressingRegistry.setLogger(logger);
		yeastRegistry.setLogger(logger);
	}

	/**
	 * @return instance of the BoozeRegistry
	 */
	public IBoozeRegistry booze()
	{
		return boozeRegistry;
	}

	/**
	 * @return instance of the BrewingRegistry
	 */
	public IBrewingRegistry brewing()
	{
		return brewingRegistry;
	}

	/**
	 * @return instance of the CulturingRegistry
	 */
	public ICulturingRegistry culturing()
	{
		return culturingRegistry;
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
	public IFermentingRegistry fermenting()
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

	/**
	 * @return instance of the DistilleryRegistry
	 */
	public IDistilleryRegistry distilling()
	{
		return distilleryRegistry;
	}

	/**
	 * @return instance of the YeastRegistry
	 */
	public IYeastRegistry yeast()
	{
		return yeastRegistry;
	}
}
