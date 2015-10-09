package growthcraft.pipes;

import growthcraft.pipes.proxy.CommonProxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod;

@Mod(
	modid = GrowthCraftPipes.MOD_ID,
	name = GrowthCraftPipes.MOD_NAME,
	version = GrowthCraftPipes.MOD_VERSION,
	dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar"
)
public class GrowthCraftPipes
{
	static public final String MOD_ID = "Growthcraft|Pipes";
	static public final String MOD_NAME = "Growthcraft Pipes";
	static public final String MOD_VERSION = "@VERSION@";

	@Mod.Instance(MOD_ID)
	public static GrowthCraftPipes INSTANCE;

	public Blocks blocks = new Blocks();
	private Config config;

	public static Config getConfig()
	{
		return INSTANCE.config;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Config(event.getModConfigurationDirectory(), "growthcraft/pipes.conf");

		blocks.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.INSTANCE.registerRenderers();
		new growthcraft.pipes.integration.Waila();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
