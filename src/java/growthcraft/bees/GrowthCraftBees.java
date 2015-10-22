package growthcraft.bees;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.api.cellar.booze.Booze;
import growthcraft.bees.client.gui.GuiHandlerBees;
import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.bees.common.block.BlockBeeHive;
import growthcraft.bees.common.block.BlockMaliceBeeBox;
import growthcraft.bees.common.CommonProxy;
import growthcraft.bees.common.item.ItemBee;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.common.item.ItemHoneyComb;
import growthcraft.bees.common.item.ItemHoneyJar;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.common.village.ComponentVillageApiarist;
import growthcraft.bees.common.village.VillageHandlerBees;
import growthcraft.bees.common.village.VillageHandlerBeesApiarist;
import growthcraft.bees.common.world.WorldGeneratorBees;
import growthcraft.bees.creativetab.CreativeTabsGrowthcraftBees;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.MapGenHelper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftBees.MOD_ID,
	name = GrowthCraftBees.MOD_NAME,
	version = GrowthCraftBees.MOD_VERSION,
	dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar"
)
public class GrowthCraftBees
{
	public static final String MOD_ID = "Growthcraft|Bees";
	public static final String MOD_NAME = "Growthcraft Bees";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftBees instance;

	public static CreativeTabs tab;

	public static BlockDefinition beeBox;
	public static BlockDefinition maliceBeeBox;
	public static BlockDefinition beeHive;
	public static BlockBoozeDefinition[] honeyMeadFluids;
	public static ItemDefinition honeyComb;
	public static ItemDefinition honeyJar;
	public static ItemDefinition bee;
	public static ItemDefinition honeyMead;
	public static ItemDefinition honeyMeadBucket_deprecated;
	public static ItemBucketBoozeDefinition[] honeyMeadBuckets;

	public static Fluid[] honeyMeadBooze;

	private growthcraft.bees.Config config;

	public static growthcraft.bees.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.bees.Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/bees.conf");

		tab = new CreativeTabsGrowthcraftBees();

		//====================
		// INIT
		//====================
		beeBox  = new BlockDefinition(new BlockBeeBox());
		maliceBeeBox  = new BlockDefinition(new BlockMaliceBeeBox());
		beeHive = new BlockDefinition(new BlockBeeHive());

		honeyComb = new ItemDefinition(new ItemHoneyComb());
		honeyJar  = new ItemDefinition(new ItemHoneyJar());
		bee       = new ItemDefinition(new ItemBee());

		honeyMeadBooze = new Booze[4];
		honeyMeadFluids = new BlockBoozeDefinition[honeyMeadBooze.length];
		honeyMeadBuckets = new ItemBucketBoozeDefinition[honeyMeadBooze.length];
		BoozeRegistryHelper.initializeBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets, "grc.honeyMead", config.honeyMeadColor);

		honeyMead        = new ItemDefinition(new ItemBoozeBottle(6, -0.45F, honeyMeadBooze)
			.setColor(config.honeyMeadColor)
			.setTipsy(0.60F, 900)
			.setPotionEffects(new int[] {Potion.regeneration.id}, new int[] {900}));
		honeyMeadBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(honeyMeadBooze)
			.setColor(config.honeyMeadColor));
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(beeBox.getBlock(), ItemBlockBeeBox.class, "grc.beeBox");
		GameRegistry.registerBlock(maliceBeeBox.getBlock(), "grc.maliceBeeBox");
		GameRegistry.registerBlock(beeHive.getBlock(), "grc.beeHive");

		GameRegistry.registerItem(honeyComb.getItem(), "grc.honeyComb");
		GameRegistry.registerItem(honeyJar.getItem(), "grc.honeyJar");
		GameRegistry.registerItem(bee.getItem(), "grc.bee");
		GameRegistry.registerItem(honeyMead.getItem(), "grc.honeyMead");
		GameRegistry.registerItem(honeyMeadBucket_deprecated.getItem(), "grc.honeyMead_bucket");

		BoozeRegistryHelper.registerBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets, honeyMead, "grc.honeyMead", honeyMeadBucket_deprecated);

		GameRegistry.registerTileEntity(TileEntityBeeBox.class, "grc.tileentity.beeBox");
		//CellarRegistry.instance().addBoozeAlternative(FluidRegistry.LAVA, honeyMeadBooze[0]);

		BeesRegistry.instance().addBee(bee.getItem());
		BeesRegistry.instance().addFlower(Blocks.red_flower);
		BeesRegistry.instance().addFlower(Blocks.yellow_flower);

		GameRegistry.registerWorldGenerator(new WorldGeneratorBees(), 0);

		MapGenHelper.registerVillageStructure(ComponentVillageApiarist.class, "grc.apiarist");

		//====================
		// ORE DICTIONARY
		//====================
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("beeQueen", bee.getItem());
		OreDictionary.registerOre("materialWaxcomb", honeyComb.asStack(1, 0));
		OreDictionary.registerOre("materialHoneycomb", honeyComb.asStack(1, 1));
		OreDictionary.registerOre("honeyDrop", honeyJar.getItem());
		OreDictionary.registerOre("dropHoney", honeyJar.getItem());

		//====================
		// CRAFTING
		//====================
		final BlockDefinition planks = new BlockDefinition(Blocks.planks);
		for (int i = 0; i < 6; ++i)
		{
			GameRegistry.addRecipe(beeBox.asStack(1, i), new Object[] { " A ", "A A", "AAA", 'A', planks.asStack(1, i) });
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(beeBox.asStack(), " A ", "A A", "AAA", 'A', "plankWood"));
		// plankMaliceWood is registered by the Growthcraft|Nether module, and is a non-flammable plank
		GameRegistry.addRecipe(new ShapedOreRecipe(maliceBeeBox.asStack(), " A ", "A A", "AAA", 'A', "plankMaliceWood"));

		final ItemStack honeyStack = honeyComb.asStack(1, 1);
		GameRegistry.addShapelessRecipe(honeyJar.asStack(), honeyStack, honeyStack, honeyStack, honeyStack, honeyStack, honeyStack, Items.flower_pot);

		GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(), Items.water_bucket, honeyJar.getItem(), Items.bucket);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		register();
		CommonProxy.instance.initRenders();
		CommonProxy.instance.initSounds();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerBees());

		final VillageHandlerBeesApiarist handler = new VillageHandlerBeesApiarist();
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, new VillageHandlerBees());
		VillagerRegistry.instance().registerVillageTradeHandler(config.villagerApiaristID, handler);

		CommonProxy.instance.registerVillagerSkin();
		new growthcraft.bees.integration.Waila();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < honeyMeadBooze.length; ++i)
			{
				honeyMeadBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		if (config.enableForestryIntegration) new growthcraft.bees.integration.ForestryModule().init();
		if (config.enableThaumcraftIntegration) new growthcraft.bees.integration.ThaumcraftModule().init();
	}
}
