package growthcraft.cellar;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.block.BlockBrewKettle;
import growthcraft.cellar.block.BlockFermentBarrel;
import growthcraft.cellar.block.BlockFruitPress;
import growthcraft.cellar.block.BlockFruitPresser;
import growthcraft.cellar.event.ItemCraftedEventCellar;
import growthcraft.cellar.event.LivingUpdateEventCellar;
import growthcraft.cellar.network.CommonProxy;
import growthcraft.cellar.network.PacketPipeline;
import growthcraft.cellar.potion.PotionCellar;
import growthcraft.cellar.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.tileentity.TileEntityFruitPress;
import growthcraft.cellar.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.village.ComponentVillageTavern;
import growthcraft.cellar.village.VillageHandlerCellar;
import growthcraft.core.AchievementPageGrowthcraft;
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
import cpw.mods.fml.common.SidedProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftCellar.MOD_ID,
	name = GrowthCraftCellar.MOD_NAME,
	version = GrowthCraftCellar.MOD_VERSION,
	dependencies = "required-after:Growthcraft"
)
public class GrowthCraftCellar
{
	public static final String MOD_ID = "Growthcraft|Cellar";
	public static final String MOD_NAME = "Growthcraft Cellar";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftCellar instance;

	@SidedProxy(clientSide="growthcraft.cellar.network.ClientProxy", serverSide="growthcraft.cellar.network.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tab;

	public static BlockDefinition fruitPress;
	public static BlockDefinition fruitPresser;
	public static BlockDefinition brewKettle;
	public static BlockDefinition fermentBarrel;

	public static Potion potionTipsy;

	// Constants
	public static ItemStack residue = new ItemStack(Items.dye, 1, 15);
	public static final int BOTTLE_VOLUME = 333;
	public static final ItemStack EMPTY_BOTTLE = new ItemStack(Items.glass_bottle);

	// Achievments
	public static ItemDefinition chievItemDummy;

	public static Achievement craftBarrel;
	public static Achievement fermentBooze;
	public static Achievement getDrunk;

	public static final PacketPipeline packetPipeline = new PacketPipeline();

	private growthcraft.cellar.Config config;

	public static growthcraft.cellar.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.cellar.Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/cellar.conf");

		//====================
		// INIT
		//====================
		tab = new CreativeTabCellar("tabGrCCellar");
		fermentBarrel = new BlockDefinition(new BlockFermentBarrel());
		fruitPress    = new BlockDefinition(new BlockFruitPress());
		fruitPresser  = new BlockDefinition(new BlockFruitPresser());
		brewKettle    = new BlockDefinition(new BlockBrewKettle());

		chievItemDummy = new ItemDefinition(new ItemChievDummy());

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fruitPress.getBlock(), "grc.fruitPress");
		GameRegistry.registerBlock(fruitPresser.getBlock(), "grc.fruitPresser");
		GameRegistry.registerBlock(brewKettle.getBlock(), "grc.brewKettle");
		GameRegistry.registerBlock(fermentBarrel.getBlock(), "grc.fermentBarrel");

		GameRegistry.registerItem(chievItemDummy.getItem(), "grc.chievItemDummy");

		GameRegistry.registerTileEntity(TileEntityFruitPress.class, "grc.tileentity.fruitPress");
		GameRegistry.registerTileEntity(TileEntityFruitPresser.class, "grc.tileentity.fruitPresser");
		GameRegistry.registerTileEntity(TileEntityBrewKettle.class, "grc.tileentity.brewKettle");
		GameRegistry.registerTileEntity(TileEntityFermentBarrel.class, "grc.tileentity.fermentBarrel");

		MapGenHelper.registerVillageStructure(ComponentVillageTavern.class, "grc.tavern");

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fruitPress.asStack(), "ABA", "CCC", "AAA", 'A', "plankWood", 'B', Blocks.piston,'C', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(brewKettle.asStack(), "A", 'A', Items.cauldron));
		GameRegistry.addRecipe(new ShapedOreRecipe(fermentBarrel.asStack(), "AAA", "BBB", "AAA", 'B', "plankWood", 'A', "ingotIron"));

		//====================
		// POTION
		//====================
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

		potionTipsy = (new PotionCellar(config.potionTipsyID, false, 0)).setIconIndex(0, 0).setPotionName("grc.potion.tipsy");

		//====================
		// ACHIEVEMENTS
		//====================
		craftBarrel  = (new Achievement("grc.achievement.craftBarrel", "craftBarrel", -4, -4, fermentBarrel.getBlock(), (Achievement)null)).initIndependentStat().registerStat();
		fermentBooze = (new Achievement("grc.achievement.fermentBooze", "fermentBooze", -2, -4, Items.nether_wart, craftBarrel)).registerStat();
		getDrunk     = (new Achievement("grc.achievement.getDrunk", "getDrunk", 0, -4, chievItemDummy.asStack(), fermentBooze)).setSpecial().registerStat();

		AchievementPageGrowthcraft.chievMasterList.add(craftBarrel);
		AchievementPageGrowthcraft.chievMasterList.add(fermentBooze);
		AchievementPageGrowthcraft.chievMasterList.add(getDrunk);

		registerHeatSources();

		NEI.hideItem(fruitPresser.asStack());
		NEI.hideItem(chievItemDummy.asStack());
	}

	private void registerHeatSources()
	{
		CellarRegistry.instance().heatSource().addHeatSource(Blocks.fire, -1, 1.0f);
		CellarRegistry.instance().heatSource().addHeatSource(Blocks.lava, -1, 0.5f);
		CellarRegistry.instance().heatSource().addHeatSource(Blocks.flowing_lava, -1, 0.5f);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		packetPipeline.initialise();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerCellar());

		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerCellar());
		proxy.registerVillagerSkin();

		new growthcraft.cellar.integration.Waila();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
		MinecraftForge.EVENT_BUS.register(new ItemCraftedEventCellar());
		MinecraftForge.EVENT_BUS.register(new LivingUpdateEventCellar());
		if (config.enableThaumcraftIntegration) new growthcraft.cellar.integration.ThaumcraftModule().init();
	}
}
