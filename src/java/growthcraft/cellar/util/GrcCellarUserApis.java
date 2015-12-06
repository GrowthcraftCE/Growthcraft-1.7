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
package growthcraft.cellar.util;

import growthcraft.api.cellar.brewing.UserBrewingRecipes;
import growthcraft.api.cellar.fermenting.UserFermentingRecipes;
import growthcraft.api.cellar.heatsource.UserHeatSources;
import growthcraft.api.cellar.pressing.UserPressingRecipes;
import growthcraft.api.cellar.yeast.UserYeastEntries;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.core.common.GrcModuleBase;

public class GrcCellarUserApis extends GrcModuleBase
{
	private UserBrewingRecipes userBrewingRecipes;
	private UserFermentingRecipes userFermentingRecipes;
	private UserHeatSources userHeatSources;
	private UserPressingRecipes userPressingRecipes;
	private UserYeastEntries userYeastEntries;
	private ModuleContainer modules;

	public GrcCellarUserApis()
	{
		this.modules = new ModuleContainer();
		this.userBrewingRecipes = new UserBrewingRecipes();
		this.userFermentingRecipes = new UserFermentingRecipes();
		this.userHeatSources = new UserHeatSources();
		this.userPressingRecipes = new UserPressingRecipes();
		this.userYeastEntries = new UserYeastEntries();
		modules.add(userBrewingRecipes);
		modules.add(userFermentingRecipes);
		modules.add(userHeatSources);
		modules.add(userPressingRecipes);
		modules.add(userYeastEntries);
	}

	@Override
	public void setLogger(ILogger log)
	{
		super.setLogger(log);
		modules.setLogger(log);
	}

	public GrcCellarUserApis setUserYeastEntries(UserYeastEntries usr)
	{
		this.userYeastEntries = usr;
		return this;
	}

	public GrcCellarUserApis setUserHeatSources(UserHeatSources usr)
	{
		this.userHeatSources = usr;
		return this;
	}

	public GrcCellarUserApis setUserPressingRecipes(UserPressingRecipes usr)
	{
		this.userPressingRecipes = usr;
		return this;
	}

	public GrcCellarUserApis setUserBrewingRecipes(UserBrewingRecipes usr)
	{
		this.userBrewingRecipes = usr;
		return this;
	}

	public GrcCellarUserApis setUserFermentingRecipes(UserFermentingRecipes usr)
	{
		this.userFermentingRecipes = usr;
		return this;
	}

	public UserYeastEntries getUserYeastEntries()
	{
		return this.userYeastEntries;
	}

	public UserHeatSources getUserHeatSources()
	{
		return this.userHeatSources;
	}

	public UserPressingRecipes getUserPressingRecipes()
	{
		return this.userPressingRecipes;
	}

	public UserBrewingRecipes getUserBrewingRecipes()
	{
		return this.userBrewingRecipes;
	}

	public UserFermentingRecipes getUserFermentingRecipes()
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
}
