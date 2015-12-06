package growthcraft.cellar;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.booze.effect.EffectTipsy;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.heatsource.UserHeatSources.UserHeatSourceEntry;
import growthcraft.api.cellar.heatsource.UserHeatSources;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.cellar.common.block.BlockBrewKettle;
import growthcraft.cellar.common.block.BlockFermentBarrel;
import growthcraft.cellar.common.block.BlockFermentJar;
import growthcraft.cellar.common.block.BlockFruitPress;
import growthcraft.cellar.common.block.BlockFruitPresser;
import growthcraft.cellar.common.booze.ModifierFunctionExtended;
import growthcraft.cellar.common.booze.ModifierFunctionHyperExtended;
import growthcraft.cellar.common.booze.ModifierFunctionPotent;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.common.item.ItemChievDummy;
import growthcraft.cellar.common.item.ItemWaterBag;
import growthcraft.cellar.common.item.ItemYeast;
import growthcraft.cellar.common.potion.PotionCellar;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.common.village.ComponentVillageTavern;
import growthcraft.cellar.common.village.VillageHandlerCellar;
import growthcraft.cellar.creativetab.CreativeTabsCellar;
import growthcraft.cellar.event.EventHandlerCauldronUseItem;
import growthcraft.cellar.event.ItemCraftedEventCellar;
import growthcraft.cellar.event.LivingUpdateEventCellar;
import growthcraft.cellar.handler.GuiHandlerCellar;
import growthcraft.cellar.network.PacketPipeline;
import growthcraft.cellar.stats.CellarAchievement;
import growthcraft.cellar.stats.GrcCellarAchievements;
import growthcraft.cellar.util.CellarBoozeBuilderFactory;
import growthcraft.cellar.util.GrcCellarUserApis;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.MapGenHelper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod(
	modid = GrowthCraftCellar.MOD_ID,
	name = GrowthCraftCellar.MOD_NAME,
	version = GrowthCraftCellar.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@"
)
public class GrowthCraftCellar
{
	public static final String MOD_ID = "Growthcraft|Cellar";
	public static final String MOD_NAME = "Growthcraft Cellar";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftCellar instance;

	public static CreativeTabs tab;

	public static BlockDefinition fruitPress;
	public static BlockDefinition fruitPresser;
	public static BlockDefinition brewKettle;
	public static BlockDefinition fermentBarrel;
	public static BlockDefinition fermentJar;

	public static ItemDefinition yeast;
	public static ItemDefinition waterBag;

	public static Potion potionTipsy;

	// Constants
	public static ItemStack EMPTY_BOTTLE;

	// Achievments
	public static ItemDefinition chievItemDummy;
	public static GrcCellarAchievements achievements;

	// Network
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	public static CellarBoozeBuilderFactory boozeBuilderFactory;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcCellarConfig config = new GrcCellarConfig();
	private GrcCellarUserApis userApis = new GrcCellarUserApis();
	private ModuleContainer modules = new ModuleContainer();

	public static UserHeatSources getUserHeatSources()
	{
		return instance.userApis.getUserHeatSources();
	}

	public static GrcCellarConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/cellar.conf");

		if (config.debugEnabled)
		{
			logger.info("Pre-Initializing %s", MOD_ID);
			CellarRegistry.instance().setLogger(logger);
		}

		if (config.enableWailaIntegration) modules.add(new growthcraft.cellar.integration.Waila());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.cellar.integration.ThaumcraftModule());
		// ALWAYS set the user modules as last, this ensures that other modules are given a chance to setup defaults and such.
		modules.add(userApis);

		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();

		userApis.getUserBrewingRecipes().setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/brewing.json");
		userApis.getUserFermentingRecipes().setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/fermenting.json");
		userApis.getUserHeatSources().setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/heatsources.json");
		userApis.getUserPressingRecipes().setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/pressing.json");
		userApis.getUserYeastEntries().setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/yeast.json");

		registerBoozeModifierFunctions();
		boozeBuilderFactory = new CellarBoozeBuilderFactory(userApis);

		//====================
		// INIT
		//====================
		EMPTY_BOTTLE = new ItemStack(Items.glass_bottle);
		tab = new CreativeTabsCellar("tabGrCCellar");
		fermentBarrel = new BlockDefinition(new BlockFermentBarrel());
		fermentJar    = new BlockDefinition(new BlockFermentJar());
		fruitPress    = new BlockDefinition(new BlockFruitPress());
		fruitPresser  = new BlockDefinition(new BlockFruitPresser());
		brewKettle    = new BlockDefinition(new BlockBrewKettle());

		yeast = new ItemDefinition(new ItemYeast());
		waterBag = new ItemDefinition(new ItemWaterBag());
		chievItemDummy = new ItemDefinition(new ItemChievDummy());

		addDefaultHeatSources();
		modules.preInit();
		register();
	}

	private void addDefaultHeatSources()
	{
		userApis.getUserHeatSources().addDefault("minecraft", "fire", UserHeatSourceEntry.newWildcardHeat(1.0f))
			.setComment("Fire!");
		userApis.getUserHeatSources().addDefault("minecraft", "flowing_lava", UserHeatSourceEntry.newWildcardHeat(0.7f))
			.setComment("We need to register both states of lava, this when its flowing");
		userApis.getUserHeatSources().addDefault("minecraft", "lava", UserHeatSourceEntry.newWildcardHeat(0.7f))
			.setComment("And when its a still pool.");
	}

	private void registerBoozeModifierFunctions()
	{
		BoozeTag.POTENT.setModifierFunction(new ModifierFunctionPotent());
		BoozeTag.EXTENDED.setModifierFunction(new ModifierFunctionExtended());
		BoozeTag.HYPER_EXTENDED.setModifierFunction(new ModifierFunctionHyperExtended());
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fruitPress.getBlock(), "grc.fruitPress");
		GameRegistry.registerBlock(fruitPresser.getBlock(), "grc.fruitPresser");
		GameRegistry.registerBlock(brewKettle.getBlock(), "grc.brewKettle");
		GameRegistry.registerBlock(fermentBarrel.getBlock(), "grc.fermentBarrel");
		GameRegistry.registerBlock(fermentJar.getBlock(), "grc.fermentJar");

		GameRegistry.registerItem(yeast.getItem(), "grc.yeast");
		GameRegistry.registerItem(waterBag.getItem(), "grc.waterBag");
		GameRegistry.registerItem(chievItemDummy.getItem(), "grc.chievItemDummy");

		GameRegistry.registerTileEntity(TileEntityFruitPress.class, "grc.tileentity.fruitPress");
		GameRegistry.registerTileEntity(TileEntityFruitPresser.class, "grc.tileentity.fruitPresser");
		GameRegistry.registerTileEntity(TileEntityBrewKettle.class, "grc.tileentity.brewKettle");
		GameRegistry.registerTileEntity(TileEntityFermentBarrel.class, "grc.tileentity.fermentBarrel");
		GameRegistry.registerTileEntity(TileEntityFermentJar.class, "grc.tileentity.fermentJar");

		MapGenHelper.registerVillageStructure(ComponentVillageTavern.class, "grc.tavern");

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fruitPress.asStack(), "ABA", "CCC", "AAA", 'A', "plankWood", 'B', Blocks.piston,'C', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(brewKettle.asStack(), "A", 'A', Items.cauldron));
		GameRegistry.addRecipe(new ShapedOreRecipe(fermentBarrel.asStack(), "AAA", "BBB", "AAA", 'B', "plankWood", 'A', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(fermentJar.asStack(), "GAG", "G G", "GGG", 'A', "plankWood", 'G', "paneGlass"));

		GameRegistry.addRecipe(new ShapedOreRecipe(waterBag.asStack(1, 16), "AAA", "ABA", "AAA", 'A', Items.leather, 'B', "materialRope"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 0), "dyeWhite", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 1), "dyeOrange", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 2), "dyeMagenta", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 3), "dyeLightBlue", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 4), "dyeYellow", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 5), "dyeLime", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 6), "dyePink", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 7), "dyeGray", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 8), "dyeLightGray", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 9), "dyeCyan", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 10), "dyePurple", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 11), "dyeBlue", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 12), "dyeBrown", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 13), "dyeGreen", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 14), "dyeRed", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 15), "dyeBlack", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));

		//====================
		// POTION
		//====================
		registerPotions();
		potionTipsy = (new PotionCellar(config.potionTipsyID, false, 0)).setIconIndex(0, 0).setPotionName("grc.potion.tipsy");
		EffectTipsy.potionTipsy = potionTipsy;
		EffectTipsy.achievement = CellarAchievement.GET_DRUNK;

		//====================
		// ACHIEVEMENTS
		//====================
		achievements = new GrcCellarAchievements();

		NEI.hideItem(fruitPresser.asStack());
		NEI.hideItem(chievItemDummy.asStack());

		modules.register();
	}

	private void registerPotions()
	{
		Potion[] potionTypes = null;

		for (Field f : Potion.class.getDeclaredFields())
		{
			f.setAccessible(true);
			try
			{
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
				{
					final Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

					potionTypes = (Potion[])f.get(null);
					final Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
				}
			}
			catch (Exception e)
			{
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}
	}

	private void registerOres()
	{
		OreDictionary.registerOre("materialYeast", yeast.getItem());
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		registerOres();

		packetPipeline.initialise();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerCellar());

		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerCellar());

		CommonProxy.instance.init();

		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
		MinecraftForge.EVENT_BUS.register(new ItemCraftedEventCellar());
		MinecraftForge.EVENT_BUS.register(new LivingUpdateEventCellar());
		MinecraftForge.EVENT_BUS.register(new EventHandlerCauldronUseItem());

		modules.postInit();
	}
}
