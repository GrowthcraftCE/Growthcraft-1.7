package growthcraft.apples;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.apples.common.block.BlockApple;
import growthcraft.apples.common.block.BlockAppleLeaves;
import growthcraft.apples.common.block.BlockAppleSapling;
import growthcraft.apples.common.CommonProxy;
import growthcraft.apples.common.item.ItemAppleSeeds;
import growthcraft.apples.common.village.ComponentVillageAppleFarm;
import growthcraft.apples.common.village.VillageHandlerApples;
import growthcraft.apples.handler.AppleFuelHandler;
import growthcraft.apples.init.GrcApplesFluids;
import growthcraft.apples.init.GrcApplesRecipes;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

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

	public static BlockDefinition appleSapling;
	public static BlockDefinition appleLeaves;
	public static BlockDefinition appleBlock;
	public static ItemDefinition appleSeeds;
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
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/apples.conf");

		creativeTab = GrowthCraftCore.creativeTab;
		if (config.enableForestryIntegration) modules.add(new growthcraft.apples.integration.ForestryModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.apples.integration.ThaumcraftModule());

		modules.add(fluids);
		modules.add(recipes);
		if (config.debugEnabled) modules.setLogger(logger);

		appleSapling = new BlockDefinition(new BlockAppleSapling());
		appleLeaves = new BlockDefinition(new BlockAppleLeaves());
		appleBlock = new BlockDefinition(new BlockApple());

		appleSeeds = new ItemDefinition(new ItemAppleSeeds());

		modules.preInit();
		register();
	}

	public void register()
	{
		appleSapling.register("grc.appleSapling");
		appleLeaves.register("grc.appleLeaves");
		appleBlock.register("grc.appleBlock");
		appleSeeds.register("grc.appleSeeds");

		MapGenHelper.registerVillageStructure(ComponentVillageAppleFarm.class, "grc.applefarm");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(appleLeaves.getBlock(), 30, 60);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("saplingTree", appleSapling.getItem());
		OreDictionary.registerOre("treeSapling", appleSapling.getItem());
		OreDictionary.registerOre("seedApple", appleSeeds.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", appleSeeds.getItem());
		// Common
		OreDictionary.registerOre("foodFruit", Items.apple);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(appleSeeds.asStack(), Items.apple);

		MinecraftForge.EVENT_BUS.register(this);

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new AppleFuelHandler());

		NEI.hideItem(appleBlock.asStack());

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();
		final VillageHandlerApples handler = new VillageHandlerApples();
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
