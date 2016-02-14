package growthcraft.grapes;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.MapGenHelper;
import growthcraft.grapes.common.block.BlockGrapeBlock;
import growthcraft.grapes.common.block.BlockGrapeLeaves;
import growthcraft.grapes.common.block.BlockGrapeVine0;
import growthcraft.grapes.common.block.BlockGrapeVine1;
import growthcraft.grapes.common.CommonProxy;
import growthcraft.grapes.common.item.ItemGrapes;
import growthcraft.grapes.common.item.ItemGrapeSeeds;
import growthcraft.grapes.common.village.ComponentVillageGrapeVineyard;
import growthcraft.grapes.common.village.VillageHandlerGrapes;
import growthcraft.grapes.event.BonemealEventGrapes;
import growthcraft.grapes.init.GrcGrapesBooze;

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

	public static BlockTypeDefinition<BlockGrapeVine0> grapeVine0;
	public static BlockTypeDefinition<BlockGrapeVine1> grapeVine1;
	public static BlockTypeDefinition<BlockGrapeLeaves> grapeLeaves;
	public static BlockDefinition grapeBlock;
	public static ItemDefinition grapes;
	public static ItemDefinition grapeSeeds;
	public static GrcGrapesBooze booze = new GrcGrapesBooze();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcGrapesConfig config = new GrcGrapesConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcGrapesConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/grapes.conf");

		modules.add(booze);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.grapes.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		//====================
		// INIT
		//====================
		grapeVine0  = new BlockTypeDefinition<BlockGrapeVine0>(new BlockGrapeVine0());
		grapeVine1  = new BlockTypeDefinition<BlockGrapeVine1>(new BlockGrapeVine1());
		grapeLeaves = new BlockTypeDefinition<BlockGrapeLeaves>(new BlockGrapeLeaves());
		grapeBlock  = new BlockDefinition(new BlockGrapeBlock());

		grapes     = new ItemDefinition(new ItemGrapes());
		grapeSeeds = new ItemDefinition(new ItemGrapeSeeds());

		grapeVine1.getBlock().setItemDrop(grapeSeeds.asStack(1));

		modules.preInit();
		register();
	}

	private void register()
	{
		GameRegistry.registerBlock(grapeVine0.getBlock(), "grc.grapeVine0");
		GameRegistry.registerBlock(grapeVine1.getBlock(), "grc.grapeVine1");
		GameRegistry.registerBlock(grapeLeaves.getBlock(), "grc.grapeLeaves");
		GameRegistry.registerBlock(grapeBlock.getBlock(), "grc.grapeBlock");

		GameRegistry.registerItem(grapes.getItem(), "grc.grapes");
		GameRegistry.registerItem(grapeSeeds.getItem(), "grc.grapeSeeds");

		CoreRegistry.instance().vineDrops().addEntry(grapes.asStack(), config.vineGrapeDropRarity);

		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(grapes.asStack(), 1, 2, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(grapes.asStack(), 1, 2, 10));

		MapGenHelper.registerVillageStructure(ComponentVillageGrapeVineyard.class, "grc.grapevineyard");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(grapeLeaves.getBlock(), 30, 60);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropGrapes", grapes.getItem());
		OreDictionary.registerOre("foodGrapes", grapes.getItem());
		OreDictionary.registerOre("seedGrapes", grapeSeeds.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("cropGrape", grapes.getItem());
		OreDictionary.registerOre("seedGrape", grapeSeeds.getItem());
		OreDictionary.registerOre("listAllseed", grapeSeeds.getItem());
		OreDictionary.registerOre("listAllfruit", grapes.getItem());

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(grapeSeeds.asStack(), grapes.getItem());

		NEI.hideItem(grapeVine0.asStack());
		NEI.hideItem(grapeVine1.asStack());
		NEI.hideItem(grapeBlock.asStack());

		MinecraftForge.EVENT_BUS.register(this);
		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();

		final VillageHandlerGrapes handler = new VillageHandlerGrapes();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);

		modules.init();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < booze.grapeWineBooze.length; ++i)
			{
				booze.grapeWineBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventGrapes());

		modules.postInit();
	}
}
