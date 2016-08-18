package growthcraft.hops;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.MapGenHelper;
import growthcraft.hops.common.CommonProxy;
import growthcraft.hops.common.village.ComponentVillageHopVineyard;
import growthcraft.hops.common.village.VillageHandlerHops;
import growthcraft.hops.init.GrcHopsBlocks;
import growthcraft.hops.init.GrcHopsFluids;
import growthcraft.hops.init.GrcHopsItems;

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
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
	modid = GrowthCraftHops.MOD_ID,
	name = GrowthCraftHops.MOD_NAME,
	version = GrowthCraftHops.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftHops
{
	public static final String MOD_ID = "Growthcraft|Hops";
	public static final String MOD_NAME = "Growthcraft Hops";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftHops instance;
	public static final GrcHopsBlocks blocks = new GrcHopsBlocks();
	public static final GrcHopsItems items = new GrcHopsItems();
	public static final GrcHopsFluids fluids = new GrcHopsFluids();

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcHopsConfig config = new GrcHopsConfig();
	private final ModuleContainer modules = new ModuleContainer();

	public static GrcHopsConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/hops.conf");
		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);
		if (config.enableForestryIntegration) modules.add(new growthcraft.hops.integration.ForestryModule());
		if (config.enableMFRIntegration) modules.add(new growthcraft.hops.integration.MFRModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.hops.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		modules.preInit();
		register();
	}

	private void register()
	{
		modules.register();
		CoreRegistry.instance().vineDrops().addDropEntry(items.hops.asStack(2), config.hopsVineDropRarity);

		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(items.hops.asStack(), 1, 2, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(items.hops.asStack(), 1, 2, 10));

		MapGenHelper.registerVillageStructure(ComponentVillageHopVineyard.class, "grc.hopvineyard");

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropHops", items.hops.getItem());
		OreDictionary.registerOre("materialHops", items.hops.getItem());
		OreDictionary.registerOre("conesHops", items.hops.getItem());
		OreDictionary.registerOre("seedHops", items.hopSeeds.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", items.hopSeeds.getItem());

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(items.hopSeeds.asStack(), items.hops.getItem());

		NEI.hideItem(blocks.hopVine.asStack());

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void initVillageHandlers()
	{
		final VillageHandlerHops handler = new VillageHandlerHops();
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
			for (Booze bz : fluids.hopAleBooze)
			{
				bz.setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
			for (Booze bz : fluids.lagerBooze)
			{
				bz.setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
