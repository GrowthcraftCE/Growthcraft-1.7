package growthcraft.nether;

import growthcraft.nether.creativetab.CreativeTabsGrowthcraftNether;
import growthcraft.nether.init.GrcNetherBlocks;
import growthcraft.nether.init.GrcNetherBooze;
import growthcraft.nether.init.GrcNetherItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import net.minecraft.creativetab.CreativeTabs;

@Mod(
	modid = GrowthCraftNether.MOD_ID,
	name = GrowthCraftNether.MOD_NAME,
	version = GrowthCraftNether.MOD_VERSION,
	dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar"
)
public class GrowthCraftNether
{
	public static final String MOD_ID = "Growthcraft|Nether";
	public static final String MOD_NAME = "Growthcraft Nether";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftNether instance;

	public static CreativeTabs tab;
	public static GrcNetherBooze booze;
	public static GrcNetherBlocks blocks;
	public static GrcNetherItems items;
	private Config config;

	public static Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		config = new Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/nether.conf");

		tab = new CreativeTabsGrowthcraftNether();

		booze = new GrcNetherBooze();
		blocks = new GrcNetherBlocks();
		items = new GrcNetherItems();

		blocks.preInit();
		items.preInit();
		booze.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		blocks.init();
		items.init();
		booze.init();
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
	}
}
