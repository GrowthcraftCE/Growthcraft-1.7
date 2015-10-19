package growthcraft.rice;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.event.PlayerInteractEventPaddy;
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
import net.minecraft.potion.Potion;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftRice.MOD_ID,
	name = GrowthCraftRice.MOD_NAME,
	version = GrowthCraftRice.MOD_VERSION,
	dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar"
)
public class GrowthCraftRice
{
	public static final String MOD_ID = "Growthcraft|Rice";
	public static final String MOD_NAME = "Growthcraft Rice";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftRice instance;

	public static BlockDefinition riceBlock;
	public static BlockDefinition paddyField;
	public static BlockBoozeDefinition[] riceSakeFluids;
	public static ItemDefinition rice;
	public static ItemDefinition riceSake;
	public static ItemDefinition riceSakeBucket_deprecated;
	public static ItemDefinition riceBall;
	public static ItemBucketBoozeDefinition[] riceSakeBuckets;

	public static Fluid[] riceSakeBooze;

	private Config config;

	public static Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/rice.conf");

		//====================
		// INIT
		//====================
		riceBlock = new BlockDefinition(new BlockRice());
		paddyField = new BlockDefinition(new BlockPaddy());

		rice     = new ItemDefinition(new ItemRice());
		riceBall = new ItemDefinition(new ItemRiceBall());

		riceSakeBooze = new Booze[4];
		riceSakeFluids = new BlockBoozeDefinition[riceSakeBooze.length];
		riceSakeBuckets = new ItemBucketBoozeDefinition[riceSakeBooze.length];
		BoozeRegistryHelper.initializeBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, "grc.riceSake", config.riceSakeColor);

		riceSake = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, riceSakeBooze)
			.setColor(config.riceSakeColor)
			.setTipsy(0.65F, 900)
			.setPotionEffects(new int[] {Potion.moveSpeed.id, Potion.jump.id}, new int[] {3600, 3600}));
		riceSakeBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(riceSakeBooze).setColor(config.riceSakeColor));

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(riceBlock.getBlock(), "grc.riceBlock");
		GameRegistry.registerBlock(paddyField.getBlock(), "grc.paddyField");

		GameRegistry.registerItem(rice.getItem(), "grc.rice");
		GameRegistry.registerItem(riceSake.getItem(), "grc.riceSake");
		GameRegistry.registerItem(riceSakeBucket_deprecated.getItem(), "grc.riceSake_bucket");
		GameRegistry.registerItem(riceBall.getItem(), "grc.riceBall");
		BoozeRegistryHelper.registerBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, riceSake, "grc.riceSake", riceSakeBucket_deprecated);

		CellarRegistry.instance().brewing().addBrewing(FluidRegistry.WATER, rice.getItem(), riceSakeBooze[0], config.riceSakeBrewingTime, 25, Residue.newDefault(0.2F));

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
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		PlayerInteractEventPaddy.paddyBlocks.put(Blocks.farmland, paddyField.getBlock());

		CommonProxy.instance.initRenders();

		final VillageHandlerRice handler = new VillageHandlerRice();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < riceSakeBooze.length; ++i)
			{
				riceSakeBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventRice());

		if (config.enableThaumcraftIntegration) new growthcraft.rice.integration.ThaumcraftModule().init();
	}
}
