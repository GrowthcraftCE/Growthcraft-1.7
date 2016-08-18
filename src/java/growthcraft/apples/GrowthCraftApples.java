package growthcraft.apples;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.apples.common.CommonProxy;
import growthcraft.apples.common.village.ComponentVillageAppleFarm;
import growthcraft.apples.common.village.VillageHandlerApples;
import growthcraft.apples.handler.AppleFuelHandler;
import growthcraft.apples.init.GrcApplesBlocks;
import growthcraft.apples.init.GrcApplesItems;
import growthcraft.apples.init.GrcApplesFluids;
import growthcraft.apples.init.GrcApplesRecipes;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.MapGenHelper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(
	modid = GrowthCraftApples.MOD_ID,
	name = GrowthCraftApples.MOD_NAME,
	version = GrowthCraftApples.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftApples
{
	public static final String MOD_ID = "Growthcraft|Apples";
	public static final String MOD_NAME = "Growthcraft Apples";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftApples instance;
	public static CreativeTabs creativeTab;
	public static final GrcApplesBlocks blocks = new GrcApplesBlocks();
	public static final GrcApplesItems items = new GrcApplesItems();
	public static final GrcApplesFluids fluids = new GrcApplesFluids();

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcApplesConfig config = new GrcApplesConfig();
	private final ModuleContainer modules = new ModuleContainer();
	private final GrcApplesRecipes recipes = new GrcApplesRecipes();

	public static GrcApplesConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		creativeTab = GrowthCraftCore.creativeTab;
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/apples.conf");
		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);
		modules.add(recipes);
		if (config.enableForestryIntegration) modules.add(new growthcraft.apples.integration.ForestryModule());
		if (config.enableMFRIntegration) modules.add(new growthcraft.apples.integration.MFRModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.apples.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		modules.preInit();
		register();
	}

	public void register()
	{
		MapGenHelper.registerVillageStructure(ComponentVillageAppleFarm.class, "grc.applefarm");

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(items.appleSeeds.asStack(), Items.apple);

		MinecraftForge.EVENT_BUS.register(this);

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new AppleFuelHandler());

		NEI.hideItem(blocks.appleBlock.asStack());

		modules.register();
	}

	private void initVillageHandlers()
	{
		final VillageHandlerApples handler = new VillageHandlerApples();
		final int brewerID = GrowthCraftCellar.getConfig().villagerBrewerID;
		if (brewerID > 0)
			VillagerRegistry.instance().registerVillageTradeHandler(brewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.init();
		if (config.enableVillageGen) initVillageHandlers();
		modules.init();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < fluids.appleCiderBooze.length; ++i)
			{
				fluids.appleCiderBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
