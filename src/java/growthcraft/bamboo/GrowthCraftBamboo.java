package growthcraft.bamboo;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.bamboo.common.CommonProxy;
import growthcraft.bamboo.common.entity.EntityBambooRaft;
import growthcraft.bamboo.common.item.ItemBamboo;
import growthcraft.bamboo.common.item.ItemBambooCoal;
import growthcraft.bamboo.common.item.ItemBambooDoor;
import growthcraft.bamboo.common.item.ItemBambooRaft;
import growthcraft.bamboo.common.item.ItemBambooShoot;
import growthcraft.bamboo.common.village.ComponentVillageBambooYard;
import growthcraft.bamboo.common.village.VillageHandlerBamboo;
import growthcraft.bamboo.common.world.BiomeGenBamboo;
import growthcraft.bamboo.common.world.WorldGeneratorBamboo;
import growthcraft.bamboo.event.BonemealEventBamboo;
import growthcraft.bamboo.handler.BambooFuelHandler;
import growthcraft.bamboo.init.GrcBambooBlocks;
import growthcraft.core.common.definition.ItemDefinition;
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

	public static GrcBambooBlocks blocks = new GrcBambooBlocks();

	public static ItemDefinition bamboo;
	public static ItemDefinition bambooDoorItem;
	public static ItemDefinition bambooRaft;
	public static ItemDefinition bambooCoal;
	public static ItemDefinition bambooShootFood;

	public static BiomeGenBase bambooBiome;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcBambooConfig config = new GrcBambooConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcBambooConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/bamboo.conf");

		modules.add(blocks);
		if (config.enableForestryIntegration) modules.add(new growthcraft.bamboo.integration.ForestryModule());
		if (config.enableMFRIntegration) modules.add(new growthcraft.bamboo.integration.MFRModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.bamboo.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);

		modules.preInit();

		bamboo = new ItemDefinition(new ItemBamboo());
		bambooDoorItem = new ItemDefinition(new ItemBambooDoor());
		bambooRaft = new ItemDefinition(new ItemBambooRaft());
		bambooCoal = new ItemDefinition(new ItemBambooCoal());
		bambooShootFood = new ItemDefinition(new ItemBambooShoot());

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
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerItem(bamboo.getItem(), "grc.bamboo");
		GameRegistry.registerItem(bambooDoorItem.getItem(), "grc.bambooDoorItem");
		GameRegistry.registerItem(bambooRaft.getItem(), "grc.bambooRaft");
		GameRegistry.registerItem(bambooCoal.getItem(), "grc.bambooCoal");
		GameRegistry.registerItem(bambooShootFood.getItem(), "grc.bambooShootFood");

		modules.register();

		if (config.generateBambooBiome)
		{
			//GameRegistry.addBiome(bambooBiome);
			BiomeManager.addSpawnBiome(bambooBiome);
			BiomeDictionary.registerBiomeType(bambooBiome, Type.FOREST);
		}

		GameRegistry.registerWorldGenerator(new WorldGeneratorBamboo(), 0);

		EntityRegistry.registerModEntity(EntityBambooRaft.class, "bambooRaft", 1, this, 80, 3, true);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapedRecipe(blocks.bambooWall.asStack(6), "###", "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooStairs.asStack(4), "#  ", "## ", "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooSingleSlab.asStack(6), "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(bambooDoorItem.asStack(), "##", "##", "##", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(bambooRaft.asStack(), "A A", "AAA", 'A', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooBlock.asStack(), "A", "A", 'A', blocks.bambooSingleSlab.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooBlock.asStack(), "AA", "AA", 'A', bamboo.getItem());
		GameRegistry.addShapedRecipe(blocks.bambooFence.asStack(3), "AAA", "AAA", 'A', bamboo.getItem());
		GameRegistry.addShapedRecipe(blocks.bambooFenceGate.asStack(), "ABA", "ABA", 'A', bamboo.getItem(), 'B', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooScaffold.asStack(16), "BBB", " A ", "A A", 'A', bamboo.getItem(), 'B', blocks.bambooBlock.getBlock());
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 2), new Object[] {"A", "B", 'A', bambooCoal.getItem(), 'B', "stickWood"}));

		MapGenHelper.registerVillageStructure(ComponentVillageBambooYard.class, "grc.bambooyard");

		registerOres();

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new BambooFuelHandler());
		GameRegistry.addSmelting(bamboo.getItem(), bambooCoal.asStack(), 0.075f);
	}

	public void registerOres()
	{
		/*
		 * ORE DICTIONARY
		 */

		// General ore dictionary names
		OreDictionary.registerOre("stickWood", bamboo.getItem());
		OreDictionary.registerOre("woodStick", bamboo.getItem());
		OreDictionary.registerOre("plankWood", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("woodPlank", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("slabWood", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("woodSlab", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("stairWood", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("woodStair", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("leavesTree", blocks.bambooLeaves.getBlock());
		OreDictionary.registerOre("treeLeaves", blocks.bambooLeaves.getBlock());


		// Bamboo specific
		OreDictionary.registerOre("cropBamboo", bamboo.getItem());
		OreDictionary.registerOre("materialBamboo", bamboo.getItem());
		OreDictionary.registerOre("bamboo", bamboo.getItem());
		OreDictionary.registerOre("plankBamboo", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("slabBamboo", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("stairBamboo", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("treeBambooLeaves", blocks.bambooLeaves.getBlock());

		OreDictionary.registerOre("foodBambooshoot", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("foodBambooshoot", bambooShootFood.getItem());

		/*
		 * For Pam's HarvestCraft
		 *   Uses the same OreDict. names as HarvestCraft
		 */
		OreDictionary.registerOre("cropBambooshoot", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("listAllveggie", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("cropBambooshoot", bambooShootFood.getItem());
		OreDictionary.registerOre("listAllveggie", bambooShootFood.getItem());
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();
		final VillageHandlerBamboo handler = new VillageHandlerBamboo();
		VillagerRegistry.instance().registerVillageCreationHandler(handler);

		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventBamboo());

		modules.postInit();
	}
}
