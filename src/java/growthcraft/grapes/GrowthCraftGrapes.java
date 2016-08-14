package growthcraft.grapes;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.MapGenHelper;
import growthcraft.grapes.common.CommonProxy;
import growthcraft.grapes.common.village.ComponentVillageGrapeVineyard;
import growthcraft.grapes.common.village.VillageHandlerGrapes;
import growthcraft.grapes.creativetab.CreativeTabsGrowthcraftGrapes;
import growthcraft.grapes.event.BonemealEventGrapes;
import growthcraft.grapes.init.GrcGrapesBlocks;
import growthcraft.grapes.init.GrcGrapesFluids;
import growthcraft.grapes.init.GrcGrapesItems;

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
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
	modid = GrowthCraftGrapes.MOD_ID,
	name = GrowthCraftGrapes.MOD_NAME,
	version = GrowthCraftGrapes.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftGrapes
{
	public static final String MOD_ID = "Growthcraft|Grapes";
	public static final String MOD_NAME = "Growthcraft Grapes";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftGrapes instance;

	public static CreativeTabs creativeTab;
	public static final GrcGrapesBlocks blocks = new GrcGrapesBlocks();
	public static final GrcGrapesItems items = new GrcGrapesItems();
	public static final GrcGrapesFluids fluids = new GrcGrapesFluids();

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcGrapesConfig config = new GrcGrapesConfig();
	private final ModuleContainer modules = new ModuleContainer();

	public static GrcGrapesConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);

		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/grapes.conf");

		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);
		if (config.enableForestryIntegration) modules.add(new growthcraft.grapes.integration.ForestryModule());
		if (config.enableMFRIntegration) modules.add(new growthcraft.grapes.integration.MFRModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.grapes.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		creativeTab = new CreativeTabsGrowthcraftGrapes("creative_tab_grcgrapes");
		modules.preInit();
		register();
		blocks.grapeVine1.getBlock().setItemDrop(items.grapeSeeds.asStack(1));
	}

	private void register()
	{
		modules.register();

		CoreRegistry.instance().vineDrops().addDropEntry(items.grapes.asStack(), config.vineGrapeDropRarity);

		MapGenHelper.registerVillageStructure(ComponentVillageGrapeVineyard.class, "grc.grapevineyard");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(blocks.grapeLeaves.getBlock(), 30, 60);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropGrapes", items.grapes.getItem());
		OreDictionary.registerOre("foodGrapes", items.grapes.getItem());
		OreDictionary.registerOre("seedGrapes", items.grapeSeeds.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("cropGrape", items.grapes.getItem());
		OreDictionary.registerOre("seedGrape", items.grapeSeeds.getItem());
		OreDictionary.registerOre("listAllseed", items.grapeSeeds.getItem());
		OreDictionary.registerOre("listAllfruit", items.grapes.getItem());
		//
		OreDictionary.registerOre("foodFruit", items.grapes.getItem());

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(items.grapeSeeds.asStack(), items.grapes.getItem());
		NEI.hideItem(blocks.grapeVine0.asStack());
		NEI.hideItem(blocks.grapeVine1.asStack());
		NEI.hideItem(blocks.grapeBlock.asStack());
		NEI.hideItem(blocks.grapeLeaves.asStack());
	}

	private void initVillageHandlers()
	{
		final VillageHandlerGrapes handler = new VillageHandlerGrapes();
		final int brewerID = GrowthCraftCellar.getConfig().villagerBrewerID;
		if (brewerID > 0)
			VillagerRegistry.instance().registerVillageTradeHandler(brewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();

		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(items.grapes.asStack(), 1, 2, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(items.grapes.asStack(), 1, 2, 10));

		if (config.enableVillageGen) initVillageHandlers();
		modules.init();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < fluids.grapeWineBooze.length; ++i)
			{
				fluids.grapeWineBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventGrapes());

		modules.postInit();
	}
}
