package growthcraft.rice;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.eventhandler.PlayerInteractEventPaddy;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.MapGenHelper;
import growthcraft.rice.common.block.BlockPaddy;
import growthcraft.rice.common.block.BlockRice;
import growthcraft.rice.common.CommonProxy;
import growthcraft.rice.common.item.ItemRice;
import growthcraft.rice.common.item.ItemRiceBall;
import growthcraft.rice.common.village.ComponentVillageRiceField;
import growthcraft.rice.common.village.VillageHandlerRice;
import growthcraft.rice.event.BonemealEventRice;
import growthcraft.rice.init.GrcRiceFluids;

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
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftRice.MOD_ID,
	name = GrowthCraftRice.MOD_NAME,
	version = GrowthCraftRice.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftRice
{
	public static final String MOD_ID = "Growthcraft|Rice";
	public static final String MOD_NAME = "Growthcraft Rice";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftRice instance;

	public static BlockTypeDefinition<BlockRice> riceBlock;
	public static BlockDefinition paddyField;
	public static ItemDefinition rice;
	public static ItemDefinition riceBall;

	public static GrcRiceFluids fluids = new GrcRiceFluids();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcRiceConfig config = new GrcRiceConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcRiceConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/rice.conf");

		modules.add(fluids);
		if (config.enableForestryIntegration) modules.add(new growthcraft.rice.integration.ForestryModule());
		if (config.enableMFRIntegration) modules.add(new growthcraft.rice.integration.MFRModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.rice.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		//====================
		// INIT
		//====================
		riceBlock = new BlockTypeDefinition<BlockRice>(new BlockRice());
		paddyField = new BlockDefinition(new BlockPaddy());

		rice     = new ItemDefinition(new ItemRice());
		riceBall = new ItemDefinition(new ItemRiceBall());

		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(riceBlock.getBlock(), "grc.riceBlock");
		GameRegistry.registerBlock(paddyField.getBlock(), "grc.paddyField");

		GameRegistry.registerItem(rice.getItem(), "grc.rice");
		GameRegistry.registerItem(riceBall.getItem(), "grc.riceBall");

		MinecraftForge.addGrassSeed(rice.asStack(), config.riceSeedDropRarity);

		MapGenHelper.registerVillageStructure(ComponentVillageRiceField.class, "grc.ricefield");

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropRice", rice.getItem());
		OreDictionary.registerOre("seedRice", rice.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", rice.getItem());

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(riceBall.asStack(1), "###", "###", '#', "cropRice"));

		NEI.hideItem(riceBlock.asStack());

		MinecraftForge.EVENT_BUS.register(this);

		modules.register();
	}

	private void initVillageHandlers()
	{
		final VillageHandlerRice handler = new VillageHandlerRice();
		final int brewerID = GrowthCraftCellar.getConfig().villagerBrewerID;
		if (brewerID > 0)
			VillagerRegistry.instance().registerVillageTradeHandler(brewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		PlayerInteractEventPaddy.paddyBlocks.put(Blocks.farmland, paddyField.getBlock());
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
			for (int i = 0; i < fluids.riceSakeBooze.length; ++i)
			{
				fluids.riceSakeBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventRice());

		modules.postInit();
	}
}
