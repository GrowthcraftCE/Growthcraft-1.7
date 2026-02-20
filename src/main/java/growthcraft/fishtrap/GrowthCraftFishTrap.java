package growthcraft.fishtrap;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.user.UserBaitConfig;
import growthcraft.api.fishtrap.user.UserCatchGroupConfig;
import growthcraft.api.fishtrap.user.UserFishTrapConfig;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.GrcGuiProvider;
import growthcraft.fishtrap.common.block.BlockFishTrap;
import growthcraft.fishtrap.common.CommonProxy;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftFishTrap.MOD_ID,
	name = GrowthCraftFishTrap.MOD_NAME,
	version = GrowthCraftFishTrap.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@"
)
public class GrowthCraftFishTrap
{
	public static final String MOD_ID = "Growthcraft|Fishtrap";
	public static final String MOD_NAME = "Growthcraft Fishtrap";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftFishTrap instance;
	public static BlockDefinition fishTrap;
	public static final GrcGuiProvider guiProvider = new GrcGuiProvider(new GrcLogger(MOD_ID + ":GuiProvider"));

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcFishtrapConfig config = new GrcFishtrapConfig();
	private final ModuleContainer modules = new ModuleContainer();
	private final UserBaitConfig userBaitConfig = new UserBaitConfig();
	private final UserCatchGroupConfig userCatchGroupConfig = new UserCatchGroupConfig();
	private final UserFishTrapConfig userFishTrapConfig = new UserFishTrapConfig();

	public static GrcFishtrapConfig getConfig()
	{
		return instance.config;
	}

	public static ILogger getLogger()
	{
		return instance.logger;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/fishtrap.conf");
		userBaitConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/baits.json");
		userCatchGroupConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/catch_groups.json");
		userFishTrapConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/entries.json");
		modules.add(userBaitConfig);
		modules.add(userCatchGroupConfig);
		modules.add(userFishTrapConfig);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.fishtrap.integration.ThaumcraftModule());
		modules.add(CommonProxy.instance);
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		fishTrap = new BlockDefinition(new BlockFishTrap());
		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fishTrap.getBlock(), "grc.fishTrap");

		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "grc.tileentity.fishTrap");

		// Gives a +0.2 bonus on base and a 1.1f bonus multiplier
		userBaitConfig.addDefault(new ItemStack(Items.rotten_flesh), 0.2f, 1.1f);

		// Catch Groups
		userCatchGroupConfig.addDefault("junk", 7, "Useless Stuff");
		userCatchGroupConfig.addDefault("treasure", 2, "Fancy Stuff");
		userCatchGroupConfig.addDefault("fish", 20, "Fishes");
		userCatchGroupConfig.addDefault("mineral", 3, "Ingots and other metallic stuff");
		userCatchGroupConfig.addDefault("legendary", 1, "Stuff you probably would never find on average");

		// Will use same chances as Fishing Rods
		// Junk
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.leather_boots), 5).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.leather), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.bone), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.potionitem), 3));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.string), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.fishing_rod), 2).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.bowl), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.stick), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.dye, 5), 1));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.rotten_flesh), 5));
		// Treasure
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Blocks.waterlily), 3));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.name_tag), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.saddle), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.bow), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.fishing_rod), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.book), 1).setEnchantable());
		// Fishes
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 60));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13));
		// Minerals
		userFishTrapConfig.addDefault("mineral", new FishTrapEntry(new ItemStack(Blocks.tripwire_hook), 1));
		userFishTrapConfig.addDefault("mineral", new FishTrapEntry(new ItemStack(Items.iron_ingot), 10));
		userFishTrapConfig.addDefault("mineral", new FishTrapEntry(new ItemStack(Items.gold_nugget), 5));
		// Legendary
		userFishTrapConfig.addDefault("legendary", new FishTrapEntry(new ItemStack(Items.gold_ingot), 1));
		userFishTrapConfig.addDefault("legendary", new FishTrapEntry(new ItemStack(Items.diamond), 10));

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fishTrap.asStack(1), "ACA", "CBC", "ACA", 'A', "plankWood", 'B', Items.lead, 'C', Items.string));

		modules.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		userBaitConfig.loadUserConfig();
		userCatchGroupConfig.loadUserConfig();
		userFishTrapConfig.loadUserConfig();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiProvider);
		modules.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
