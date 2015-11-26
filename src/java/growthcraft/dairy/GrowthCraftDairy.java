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
package growthcraft.dairy;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.dairy.client.event.TextureStitchEventDairy;
import growthcraft.dairy.common.CommonProxy;
import growthcraft.dairy.init.GrcDairyBlocks;
import growthcraft.dairy.init.GrcDairyBooze;
import growthcraft.dairy.init.GrcDairyItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;

@Mod(
	modid = GrowthCraftDairy.MOD_ID,
	name = GrowthCraftDairy.MOD_NAME,
	version = GrowthCraftDairy.MOD_VERSION,
	dependencies = GrowthCraftDairy.MOD_DEPENDENCIES
)
public class GrowthCraftDairy
{
	public static final String MOD_ID = "Growthcraft|Dairy";
	public static final String MOD_NAME = "Growthcraft Dairy";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Growthcraft;required-after:Growthcraft|Cellar";

	@Instance(MOD_ID)
	public static GrowthCraftDairy instance;

	public static GrcDairyBlocks blocks = new GrcDairyBlocks();
	public static GrcDairyItems items = new GrcDairyItems();
	public static GrcDairyBooze booze = new GrcDairyBooze();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcDairyConfig config = new GrcDairyConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcDairyConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/dairy.conf");

		modules.add(blocks);
		modules.add(items);
		modules.add(booze);
		//if (config.enableThaumcraftIntegration) modules.add(new growthcraft.dairy.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();

		modules.preInit();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventDairy());
		register();
	}

	private void register()
	{
		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();

		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
