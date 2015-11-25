package growthcraft.dairy;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
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
