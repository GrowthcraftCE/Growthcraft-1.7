package growthcraft.bees;

import java.io.File;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.bees.block.BlockBeeBox;
import growthcraft.bees.block.BlockBeeHive;
import growthcraft.bees.gui.GuiHandlerBees;
import growthcraft.bees.item.ItemBee;
import growthcraft.bees.item.ItemHoneyComb;
import growthcraft.bees.item.ItemHoneyJar;
import growthcraft.bees.tileentity.TileEntityBeeBox;
import growthcraft.bees.village.ComponentVillageApiarist;
import growthcraft.bees.village.VillageHandlerBees;
import growthcraft.bees.village.VillageHandlerBeesApiarist;
import growthcraft.bees.world.WorldGeneratorBees;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.utils.MapGenHelper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

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

	@Instance("Growthcraft|Bees")
	public static GrowthCraftBees instance;

	@SidedProxy(clientSide="growthcraft.bees.ClientProxy", serverSide="growthcraft.bees.CommonProxy")
	public static CommonProxy proxy;

	public static BlockDefinition beeBox;
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

		//====================
		// INIT
		//====================
		beeBox  = new BlockDefinition(new BlockBeeBox());
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

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(beeBox.getBlock(), "grc.beeBox");
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
		for (int i = 0; i < 9; ++i) {
			BeesRegistry.instance().addFlower(Blocks.red_flower, i);
		}
		BeesRegistry.instance().addFlower(Blocks.yellow_flower, 0);

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
		GameRegistry.addRecipe(new ShapedOreRecipe(beeBox.asStack(), " A ", "A A", "AAA", 'A', "plankWood"));
		ItemStack honeyStack = honeyComb.asStack(1, 1);
		GameRegistry.addShapelessRecipe(honeyJar.asStack(), honeyStack, honeyStack, honeyStack, honeyStack, honeyStack, honeyStack, Items.flower_pot);
		GameRegistry.addShapelessRecipe(honeyMeadBucket_deprecated.asStack(), Items.water_bucket, honeyJar.getItem(), Items.bucket);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();
		proxy.initSounds();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerBees());

		VillageHandlerBeesApiarist handler = new VillageHandlerBeesApiarist();
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, new VillageHandlerBees());
		VillagerRegistry.instance().registerVillageTradeHandler(config.villagerApiaristID, handler);

		proxy.registerVillagerSkin();
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
		/*String modid;

		modid = "Forestry";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				Item item = GameRegistry.findItem(modid, "beeQueenGE");
				if (item != null)
				{
					BeesRegistry.instance().addBee(item.itemID);
				}

				item = GameRegistry.findItem(modid, "beeDroneGE");
				if (item != null)
				{
					BeesRegistry.instance().addBee(item.itemID);
				}

				item = GameRegistry.findItem(modid, "beePrincessGE");
				if (item != null)
				{
					BeesRegistry.instance().addBee(item.itemID);
				}

				CellarRegistry.instance().addBoozeAlternative("short.mead", "grc.honeyMead0");

				item = GameRegistry.findItem(modid, "beeswax");
				if (item != null && RecipeManagers.centrifugeManager != null)
				{
					RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(honeyComb, 1, 0), new ItemStack(item));
					Item item2 = GameRegistry.findItem(modid, "honeyDrop");
					Item item3 = GameRegistry.findItem(modid, "honeydew");
					if (item != null && item2 != null)
					{
						RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(honeyComb, 1, 1), new ItemStack[] { new ItemStack(item3), new ItemStack(item), new ItemStack(item2) }, new int[] { 60, 20, 30 });
						//RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(honeyComb, 1, 1), new ItemStack(item), new ItemStack(item2), 90);
					}
				}

				FMLLog.info("[Growthcraft|Bees] Successfully integrated with Forestry.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Bees] Forestry not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
