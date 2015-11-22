package growthcraft.nether;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.nether.client.event.TextureStitchEventHandler;
import growthcraft.nether.common.CommonProxy;
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
import net.minecraftforge.common.MinecraftForge;

@Mod(
	modid = GrowthCraftNether.MOD_ID,
	name = GrowthCraftNether.MOD_NAME,
	version = GrowthCraftNether.MOD_VERSION,
	dependencies = GrowthCraftNether.MOD_DEPENDENCIES
)
public class GrowthCraftNether
{
	public static final String MOD_ID = "Growthcraft|Nether";
	public static final String MOD_NAME = "Growthcraft Nether";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Growthcraft;required-after:Growthcraft|Cellar";

	@Instance(MOD_ID)
	public static GrowthCraftNether instance;

	public static CreativeTabs tab;
	public static GrcNetherBlocks blocks = new GrcNetherBlocks();
	public static GrcNetherItems items = new GrcNetherItems();
	public static GrcNetherBooze booze = new GrcNetherBooze();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcNetherConfig config = new GrcNetherConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcNetherConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/nether.conf");

		tab = new CreativeTabsGrowthcraftNether();

		modules.add(blocks);
		modules.add(items);
		modules.add(booze);

		if (config.debugEnabled) modules.setLogger(logger);

		modules.freeze();

		modules.preInit();
		modules.register();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();

		modules.init();
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
