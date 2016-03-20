/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.grains;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.grains.creativetab.GrcGrainsCreativeTabs;
import growthcraft.grains.init.GrcGrainsBlocks;
import growthcraft.grains.init.GrcGrainsFluids;
import growthcraft.grains.init.GrcGrainsItems;
import growthcraft.grains.init.GrcGrainsRecipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import net.minecraft.creativetab.CreativeTabs;

@Mod(
	modid = GrowthCraftGrains.MOD_ID,
	name = GrowthCraftGrains.MOD_NAME,
	version = GrowthCraftGrains.MOD_VERSION,
	dependencies = GrowthCraftGrains.MOD_DEPENDENCIES
)
public class GrowthCraftGrains
{
	public static final String MOD_ID = "Growthcraft|Grains";
	public static final String MOD_NAME = "Growthcraft Grains";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Growthcraft";
	public static final EventBus GRAINS_BUS = new EventBus();
	public static final GrcGrainsBlocks blocks = new GrcGrainsBlocks();
	public static final GrcGrainsFluids fluids = new GrcGrainsFluids();
	public static final GrcGrainsItems items = new GrcGrainsItems();
	public static final GrcGrainsRecipes recipes = new GrcGrainsRecipes();

	@Instance(MOD_ID)
	public static GrowthCraftGrains instance;
	public static CreativeTabs creativeTab;

	private GrcGrainsConfig config = new GrcGrainsConfig();
	private ILogger logger = new GrcLogger(MOD_ID);
	private ModuleContainer modules = new ModuleContainer();

	public static GrcGrainsConfig getConfig()
	{
		return instance.config;
	}

	public static ILogger getLogger()
	{
		return instance.logger;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/milk.conf");
		creativeTab = new GrcGrainsCreativeTabs("creative_tab_grcgrains");
		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);
		modules.add(recipes);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.milk.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		modules.preInit();
		modules.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		modules.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
