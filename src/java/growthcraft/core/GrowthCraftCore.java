package growthcraft.core;

import java.util.List;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.api.core.fluids.user.UserFluidDictionaryConfig;
import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.schema.BlockKeySchema;
import growthcraft.api.core.vines.user.UserVinesConfig;
import growthcraft.core.common.AchievementPageGrowthcraft;
import growthcraft.core.common.CommonProxy;
import growthcraft.core.common.item.crafting.ShapelessItemComparableRecipe;
import growthcraft.core.creativetab.CreativeTabsGrowthcraft;
import growthcraft.core.eventhandler.EventHandlerBucketFill;
import growthcraft.core.eventhandler.EventHandlerItemCraftedEventCore;
import growthcraft.core.eventhandler.EventHandlerLivingDeathCore;
import growthcraft.core.eventhandler.HarvestDropsEventCore;
import growthcraft.core.eventhandler.PlayerInteractEventPaddy;
import growthcraft.core.eventhandler.TextureStitchEventCore;
import growthcraft.core.init.GrcCoreBlocks;
import growthcraft.core.init.GrcCoreFluids;
import growthcraft.core.init.GrcCoreItems;
import growthcraft.core.init.GrcCoreRecipes;
import growthcraft.core.integration.bop.BopPlatform;
import growthcraft.core.stats.GrcCoreAchievements;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.FMLCommonHandler;
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
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

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
	public static GrcCoreAchievements achievements;
	public static CreativeTabs creativeTab;

	public static final GrcCoreBlocks blocks = new GrcCoreBlocks();
	public static final GrcCoreItems items = new GrcCoreItems();
	public static final GrcCoreFluids fluids = new GrcCoreFluids();
	public static final GrcCoreRecipes recipes = new GrcCoreRecipes();

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
	public void preInit(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/core.conf");
		if (config.debugEnabled) logger.info("Pre-Initializing %s", MOD_ID);

		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);
		modules.add(recipes);

		userVinesConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/core/vines.json");
		userFluidDictionary.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/core/fluid_dictionary.json");
		modules.add(userVinesConfig);
		modules.add(userFluidDictionary);

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.core.integration.ThaumcraftModule());
		if (config.enableWailaIntegration) modules.add(new growthcraft.core.integration.Waila());
		if (config.enableAppleCoreIntegration) modules.add(new growthcraft.core.integration.AppleCore());
		if (config.enableNEIIntegration) modules.add(new growthcraft.core.integration.nei.NEIModule());

		if (config.debugEnabled)
		{
			CoreRegistry.instance().setLogger(logger);
			modules.setLogger(logger);
		}

		creativeTab = new CreativeTabsGrowthcraft("creative_tab_grccore");

		EMPTY_BOTTLE = new ItemStack(Items.glass_bottle);
		if (config.changeWaterBottleCapacity)
		{
			final List<FluidContainerData> dataList = FluidUtils.getFluidData().get(FluidRegistry.WATER);
			for (FluidContainerData data : dataList)
				if (OreDictionary.itemMatches(data.filledContainer, new ItemStack(Items.potionitem, 1, 0), true))
					data.fluid.amount = config.bottleCapacity;

			// Reset the fluidData cache, as we are loading it super early here
			FluidUtils.getFluidData().clear();
		}
		if (config.changeWaterBottleContainer) Items.potionitem.setContainerItem(Items.glass_bottle);

		RecipeSorter.register("grcShaplessComparable", ShapelessItemComparableRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		modules.preInit();
		register();
	}

	private void register()
	{
		MinecraftForge.EVENT_BUS.register(new TextureStitchEventCore());

		modules.register();
		achievements = new GrcCoreAchievements();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		userFluidDictionary.loadUserConfig();
		CommonProxy.instance.initRenders();
		AchievementPageGrowthcraft.init();

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
	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(EventHandlerBucketFill.instance());
		MinecraftForge.EVENT_BUS.register(new HarvestDropsEventCore());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventPaddy());
		MinecraftForge.EVENT_BUS.register(new EventHandlerLivingDeathCore());
		FMLCommonHandler.instance().bus().register(new EventHandlerItemCraftedEventCore());

		modules.postInit();
		if (config.dumpGameRegistry) growthcraft.core.util.GameRegistryDumper.run();
	}
}
