package growthcraft.core;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.core.common.AchievementPageGrowthcraft;
import growthcraft.core.common.block.BlockFenceRope;
import growthcraft.core.common.block.BlockRope;
import growthcraft.core.common.CommonProxy;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.creativetab.CreativeTabsGrowthcraft;
import growthcraft.core.event.EventHandlerBucketFill;
import growthcraft.core.event.HarvestDropsEventCore;
import growthcraft.core.event.PlayerInteractEventAmazingStick;
import growthcraft.core.event.PlayerInteractEventPaddy;
import growthcraft.core.event.TextureStitchEventCore;
import growthcraft.core.init.GrcCoreItems;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

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
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[10.13.4.1566,)";

	@Instance(MOD_ID)
	public static GrowthCraftCore instance;

	@SideOnly(Side.CLIENT)
	public static IIcon liquidSmoothTexture;
	@SideOnly(Side.CLIENT)
	public static IIcon liquidBlobsTexture;

	public static CreativeTabs tab;

	public static BlockDefinition fenceRope;
	public static BlockDefinition ropeBlock;
	public static GrcCoreItems items = new GrcCoreItems();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcCoreConfig config = new GrcCoreConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcCoreConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/core.conf");
		if (config.debugEnabled) logger.info("Pre-Initializing %s", MOD_ID);

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.core.integration.ThaumcraftModule());
		if (config.enableWailaIntegration) modules.add(new growthcraft.core.integration.Waila());
		if (config.enableAppleCoreIntegration) modules.add(new growthcraft.core.integration.AppleCore());

		if (config.debugEnabled) modules.setLogger(logger);

		tab =  new CreativeTabsGrowthcraft("tabGrowthCraft");

		//====================
		// INIT
		//====================
		fenceRope = new BlockDefinition(new BlockFenceRope());
		ropeBlock = new BlockDefinition(new BlockRope());

		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fenceRope.getBlock(), "grc.fenceRope");
		GameRegistry.registerBlock(ropeBlock.getBlock(), "grc.ropeBlock");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(fenceRope.getBlock(), 5, 20);

		NEI.hideItem(fenceRope.asStack());
		NEI.hideItem(ropeBlock.asStack());

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventCore());

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();
		AchievementPageGrowthcraft.init();

		ItemUtils.init();

		modules.init();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(EventHandlerBucketFill.instance());
		MinecraftForge.EVENT_BUS.register(new HarvestDropsEventCore());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventPaddy());
		if (config.useAmazingStick)
		{
			MinecraftForge.EVENT_BUS.register(new PlayerInteractEventAmazingStick());
		}

		modules.postInit();
	}
}
