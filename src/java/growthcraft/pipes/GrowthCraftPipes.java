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
package growthcraft.pipes;

import growthcraft.pipes.common.CommonProxy;
import growthcraft.pipes.init.GrcPipesBlocks;
import growthcraft.pipes.init.GrcPipesItems;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.GrcLogger;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod;

/**
 * The pipe module attempts to add simple fluid pipes for use with the cellar
 * blocks.
 */
@Mod(
	modid = GrowthCraftPipes.MOD_ID,
	name = GrowthCraftPipes.MOD_NAME,
	version = GrowthCraftPipes.MOD_VERSION,
	dependencies = GrowthCraftPipes.MOD_DEPENDENCIES
)
public class GrowthCraftPipes
{
	public static final String MOD_ID = "Growthcraft|Pipes";
	public static final String MOD_NAME = "Growthcraft Pipes";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@";

	@Mod.Instance(MOD_ID)
	public static GrowthCraftPipes instance;
	public static GrcPipesBlocks blocks = new GrcPipesBlocks();
	public static GrcPipesItems items = new GrcPipesItems();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcPipesConfig config = new GrcPipesConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcPipesConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/pipes.conf");

		modules.add(blocks);
		modules.add(items);

		if (config.enableWailaIntegration) modules.add(new growthcraft.pipes.integration.Waila());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.pipes.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		modules.freeze();

		modules.preInit();
		modules.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.instance.registerRenderers();
		modules.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
