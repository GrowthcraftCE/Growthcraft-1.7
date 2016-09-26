package growthcraft.bamboo;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.bamboo.common.CommonProxy;
import growthcraft.bamboo.common.entity.EntityBambooRaft;
import growthcraft.bamboo.common.village.ComponentVillageBambooYard;
import growthcraft.bamboo.common.village.VillageHandlerBamboo;
import growthcraft.bamboo.common.world.BiomeGenBamboo;
import growthcraft.bamboo.common.world.WorldGeneratorBamboo;
import growthcraft.bamboo.creativetab.CreativeTabsGrowthcraftBamboo;
import growthcraft.bamboo.event.BonemealEventBamboo;
import growthcraft.bamboo.handler.BambooFuelHandler;
import growthcraft.bamboo.init.GrcBambooBlocks;
import growthcraft.bamboo.init.GrcBambooItems;
import growthcraft.core.util.MapGenHelper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftBamboo.MOD_ID,
	name = GrowthCraftBamboo.MOD_NAME,
	version = GrowthCraftBamboo.MOD_VERSION,
	dependencies = "required-after:Growthcraft"
)
public class GrowthCraftBamboo
{
	public static final String MOD_ID = "Growthcraft|Bamboo";
	public static final String MOD_NAME = "Growthcraft Bamboo";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftBamboo instance;

	public static CreativeTabs creativeTab;
	public static final GrcBambooBlocks blocks = new GrcBambooBlocks();
	public static final GrcBambooItems items = new GrcBambooItems();

	public static BiomeGenBase bambooBiome;

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcBambooConfig config = new GrcBambooConfig();
	private final ModuleContainer modules = new ModuleContainer();

	public static GrcBambooConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/bamboo.conf");
		modules.add(blocks);
		modules.add(items);
		if (config.enableForestryIntegration) modules.add(new growthcraft.bamboo.integration.ForestryModule());
		if (config.enableMFRIntegration) modules.add(new growthcraft.bamboo.integration.MFRModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.bamboo.integration.ThaumcraftModule());
		modules.add(CommonProxy.instance);
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		creativeTab = new CreativeTabsGrowthcraftBamboo("creative_tab_grcbamboo");
		modules.preInit();

		if (config.generateBambooBiome)
		{
			bambooBiome = (new BiomeGenBamboo(config.bambooBiomeID))
				.setColor(353825)
				.setBiomeName("BambooForest")
				.func_76733_a(5159473)
				.setTemperatureRainfall(0.7F, 0.8F);
		}

		register();
	}

	private void register()
	{
		modules.register();

		if (config.generateBambooBiome)
		{
			//GameRegistry.addBiome(bambooBiome);
			BiomeManager.addSpawnBiome(bambooBiome);
			BiomeDictionary.registerBiomeType(bambooBiome, Type.DENSE, Type.LUSH, Type.FOREST);
		}

		GameRegistry.registerWorldGenerator(new WorldGeneratorBamboo(), 0);

		EntityRegistry.registerModEntity(EntityBambooRaft.class, "bambooRaft", 1, this, 80, 3, true);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapedRecipe(blocks.bambooWall.asStack(6), "###", "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooStairs.asStack(4), "#  ", "## ", "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooSingleSlab.asStack(6), "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(items.bambooDoorItem.asStack(), "##", "##", "##", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(items.bambooRaft.asStack(), "A A", "AAA", 'A', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooBlock.asStack(), "A", "A", 'A', blocks.bambooSingleSlab.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooBlock.asStack(), "AA", "AA", 'A', items.bamboo.getItem());
		GameRegistry.addShapedRecipe(blocks.bambooFence.asStack(3), "AAA", "AAA", 'A', items.bamboo.getItem());
		GameRegistry.addShapedRecipe(blocks.bambooFenceGate.asStack(), "ABA", "ABA", 'A', items.bamboo.getItem(), 'B', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooScaffold.asStack(16), "BBB", " A ", "A A", 'A', items.bamboo.getItem(), 'B', blocks.bambooBlock.getBlock());
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 2), new Object[] {"A", "B", 'A', items.bambooCoal.getItem(), 'B', "stickWood"}));

		MapGenHelper.registerVillageStructure(ComponentVillageBambooYard.class, "grc.bambooyard");

		registerOres();

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new BambooFuelHandler());
		GameRegistry.addSmelting(items.bamboo.getItem(), items.bambooCoal.asStack(), 0.075f);
	}

	public void registerOres()
	{
		/*
		 * ORE DICTIONARY
		 */

		// General ore dictionary names
		OreDictionary.registerOre("stickWood", items.bamboo.getItem());
		OreDictionary.registerOre("woodStick", items.bamboo.getItem());
		OreDictionary.registerOre("plankWood", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("woodPlank", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("slabWood", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("woodSlab", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("stairWood", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("woodStair", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("leavesTree", blocks.bambooLeaves.getBlock());
		OreDictionary.registerOre("treeLeaves", blocks.bambooLeaves.getBlock());


		// Bamboo specific
		OreDictionary.registerOre("cropBamboo", items.bamboo.getItem());
		OreDictionary.registerOre("materialBamboo", items.bamboo.getItem());
		OreDictionary.registerOre("bamboo", items.bamboo.getItem());
		OreDictionary.registerOre("plankBamboo", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("slabBamboo", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("stairBamboo", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("treeBambooLeaves", blocks.bambooLeaves.getBlock());

		OreDictionary.registerOre("foodBambooshoot", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("foodBambooshoot", items.bambooShootFood.getItem());

		/*
		 * For Pam's HarvestCraft
		 *   Uses the same OreDict. names as HarvestCraft
		 */
		OreDictionary.registerOre("cropBambooshoot", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("listAllveggie", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("cropBambooshoot", items.bambooShootFood.getItem());
		OreDictionary.registerOre("listAllveggie", items.bambooShootFood.getItem());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		final VillageHandlerBamboo handler = new VillageHandlerBamboo();
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		modules.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventBamboo());

		modules.postInit();
	}
}
