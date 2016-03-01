package growthcraft.core;

import growthcraft.api.core.fluids.user.UserFluidDictionaryConfig;
import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.schema.BlockKeySchema;
import growthcraft.api.core.vines.user.UserVinesConfig;
import growthcraft.core.common.AchievementPageGrowthcraft;
import growthcraft.core.common.CommonProxy;
import growthcraft.core.creativetab.CreativeTabsGrowthcraft;
import growthcraft.core.eventhandler.EventHandlerBucketFill;
import growthcraft.core.eventhandler.EventHandlerSpecialBucketFill;
import growthcraft.core.eventhandler.HarvestDropsEventCore;
import growthcraft.core.eventhandler.PlayerInteractEventAmazingStick;
import growthcraft.core.eventhandler.PlayerInteractEventPaddy;
import growthcraft.core.eventhandler.TextureStitchEventCore;
import growthcraft.core.init.GrcCoreBlocks;
import growthcraft.core.init.GrcCoreFluids;
import growthcraft.core.init.GrcCoreItems;
import growthcraft.core.integration.bop.BopPlatform;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;

@Mod(
	modid = GrowthCraftCore.MOD_ID,
	name = GrowthCraftCore.MOD_NAME,
	version = GrowthCraftCore.MOD_VERSION,
	acceptedMinecraftVersions = GrowthCraftCore.MOD_ACC_MINECRAFT,
	dependencies = GrowthCraftCore.MOD_DEPENDENCIES
)
public class GrowthCraftCore
{
	public static final String MOD_ID = "Growthcraft";
	public static final String MOD_NAME = "Growthcraft";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_ACC_MINECRAFT = "[@GRC_MC_VERSION@]";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[10.13.4.1566,);required-after:AppleCore@[1.3.0,);required-after:Forestry@[4.2.8,)";

	@Instance(MOD_ID)
	public static GrowthCraftCore instance;

	@SideOnly(Side.CLIENT)
	public static IIcon liquidSmoothTexture;
	@SideOnly(Side.CLIENT)
	public static IIcon liquidBlobsTexture;

	public static CreativeTabs creativeTab;

	public static GrcCoreBlocks blocks = new GrcCoreBlocks();
	public static GrcCoreItems items = new GrcCoreItems();
	public static GrcCoreFluids fluids = new GrcCoreFluids();
	public static final EventBus CORE_BUS = new EventBus();

	// Constants
	public static ItemStack EMPTY_BOTTLE;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcCoreConfig config = new GrcCoreConfig();
	private ModuleContainer modules = new ModuleContainer();
	private UserFluidDictionaryConfig userFluidDictionary = new UserFluidDictionaryConfig();
	private UserVinesConfig userVinesConfig = new UserVinesConfig();

	public static GrcCoreConfig getConfig()
	{
		return instance.config;
	}

	public static ILogger getLogger()
	{
		return instance.logger;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/core.conf");
		if (config.debugEnabled) logger.info("Pre-Initializing %s", MOD_ID);

		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);

		userVinesConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/core/vines.json");
		userFluidDictionary.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/core/fluid_dictionary.json");
		modules.add(userVinesConfig);
		modules.add(userFluidDictionary);

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.core.integration.ThaumcraftModule());
		if (config.enableWailaIntegration) modules.add(new growthcraft.core.integration.Waila());
		if (config.enableAppleCoreIntegration) modules.add(new growthcraft.core.integration.AppleCore());

		if (config.debugEnabled) modules.setLogger(logger);

		creativeTab = new CreativeTabsGrowthcraft("tabGrowthCraft");

		EMPTY_BOTTLE = new ItemStack(Items.glass_bottle);

		modules.preInit();
		register();
	}

	private void register()
	{
		MinecraftForge.EVENT_BUS.register(new TextureStitchEventCore());

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		userFluidDictionary.loadUserConfig();
		CommonProxy.instance.initRenders();
		AchievementPageGrowthcraft.init();

		ItemUtils.init();

		userVinesConfig.addDefault(Blocks.vine);
		if (BopPlatform.isLoaded())
		{
			userVinesConfig.addDefault(new BlockKeySchema(BopPlatform.MOD_ID, "willow", ItemKey.WILDCARD_VALUE));
			userVinesConfig.addDefault(new BlockKeySchema(BopPlatform.MOD_ID, "ivy", ItemKey.WILDCARD_VALUE));
		}
		modules.init();
		userVinesConfig.loadUserConfig();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(EventHandlerBucketFill.instance());
		MinecraftForge.EVENT_BUS.register(EventHandlerSpecialBucketFill.instance());
		MinecraftForge.EVENT_BUS.register(new HarvestDropsEventCore());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventPaddy());
		if (config.useAmazingStick)
		{
			MinecraftForge.EVENT_BUS.register(new PlayerInteractEventAmazingStick());
		}

		modules.postInit();
		if (config.dumpGameRegistry) growthcraft.core.util.GameRegistryDumper.run();
	}
}
