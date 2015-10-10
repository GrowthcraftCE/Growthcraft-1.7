package growthcraft.pipes;

import growthcraft.pipes.proxy.CommonProxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

@Mod(
	modid = GrowthCraftPipes.MOD_ID,
	name = GrowthCraftPipes.MOD_NAME,
	version = GrowthCraftPipes.MOD_VERSION,
	dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar"
)
public class GrowthCraftPipes
{
	public static final String MOD_ID = "Growthcraft|Pipes";
	public static final String MOD_NAME = "Growthcraft Pipes";
	public static final String MOD_VERSION = "@VERSION@";

	@Mod.Instance(MOD_ID)
	public static GrowthCraftPipes INSTANCE;

	public Blocks blocks = new Blocks();
	public Items items = new Items();
	private Config config;

	public static Config getConfig()
	{
		return INSTANCE.config;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/pipes.conf");

		if (config.enabled)
		{
			blocks.preInit();
			items.preInit();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (config.enabled)
		{
			CommonProxy.INSTANCE.registerRenderers();
			new growthcraft.pipes.integration.Waila();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
