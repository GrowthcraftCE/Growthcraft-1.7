/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.cellar.util;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.brewing.user.UserBrewingRecipesConfig;
import growthcraft.api.cellar.fermenting.user.UserFermentingRecipesConfig;
import growthcraft.api.cellar.heatsource.user.UserHeatSourcesConfig;
import growthcraft.api.cellar.pressing.user.UserPressingRecipesConfig;
import growthcraft.api.cellar.yeast.user.UserYeastEntriesConfig;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.IModule;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.core.common.GrcModuleBase;

public class GrcCellarUserApis extends GrcModuleBase
{
	private UserBrewingRecipesConfig userBrewingRecipes;
	private UserFermentingRecipesConfig userFermentingRecipes;
	private UserHeatSourcesConfig userHeatSources;
	private UserPressingRecipesConfig userPressingRecipes;
	private UserYeastEntriesConfig userYeastEntries;
	private ModuleContainer modules;

	public GrcCellarUserApis()
	{
		this.modules = new ModuleContainer();
		this.userBrewingRecipes = new UserBrewingRecipesConfig();
		this.userFermentingRecipes = new UserFermentingRecipesConfig();
		this.userHeatSources = new UserHeatSourcesConfig();
		this.userPressingRecipes = new UserPressingRecipesConfig();
		this.userYeastEntries = new UserYeastEntriesConfig();
		modules.add(userBrewingRecipes);
		modules.add(userFermentingRecipes);
		modules.add(userHeatSources);
		modules.add(userPressingRecipes);
		modules.add(userYeastEntries);
	}

	@Override
	public void setLogger(@Nonnull ILogger log)
	{
		super.setLogger(log);
		modules.setLogger(log);
	}

	public UserYeastEntriesConfig getUserYeastEntries()
	{
		return this.userYeastEntries;
	}

	public UserHeatSourcesConfig getUserHeatSources()
	{
		return this.userHeatSources;
	}

	public UserPressingRecipesConfig getUserPressingRecipes()
	{
		return this.userPressingRecipes;
	}

	public UserBrewingRecipesConfig getUserBrewingRecipes()
	{
		return this.userBrewingRecipes;
	}

	public UserFermentingRecipesConfig getUserFermentingRecipes()
	{
		return this.userFermentingRecipes;
	}

	@Override
	public void preInit()
	{
		modules.preInit();
	}

	@Override
	public void register()
	{
		modules.register();
	}

	@Override
	public void init()
	{
		modules.init();
	}

	@Override
	public void postInit()
	{
		modules.postInit();
	}

	public void loadConfigs()
	{
		for (IModule module : modules)
		{
			if (module instanceof AbstractUserJSONConfig)
			{
				((AbstractUserJSONConfig)module).loadUserConfig();
			}
		}
	}
}
