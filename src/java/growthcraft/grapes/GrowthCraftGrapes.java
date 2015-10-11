package growthcraft.grapes;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.CoreRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEI;
import growthcraft.core.utils.MapGenHelper;
import growthcraft.grapes.block.BlockGrapeBlock;
import growthcraft.grapes.block.BlockGrapeLeaves;
import growthcraft.grapes.block.BlockGrapeVine0;
import growthcraft.grapes.block.BlockGrapeVine1;
import growthcraft.grapes.event.BonemealEventGrapes;
import growthcraft.grapes.item.ItemGrapes;
import growthcraft.grapes.item.ItemGrapeSeeds;
import growthcraft.grapes.village.ComponentVillageGrapeVineyard;
import growthcraft.grapes.village.VillageHandlerGrapes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
	modid = GrowthCraftGrapes.MOD_ID,
	name = GrowthCraftGrapes.MOD_NAME,
	version = GrowthCraftGrapes.MOD_VERSION,
	dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar"
)
public class GrowthCraftGrapes
{
	public static final String MOD_ID = "Growthcraft|Grapes";
	public static final String MOD_NAME = "Growthcraft Grapes";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance("Growthcraft|Grapes")
	public static GrowthCraftGrapes instance;

	@SidedProxy(clientSide="growthcraft.grapes.ClientProxy", serverSide="growthcraft.grapes.CommonProxy")
	public static CommonProxy proxy;

	public static BlockTypeDefinition<BlockGrapeVine0> grapeVine0;
	public static BlockTypeDefinition<BlockGrapeVine1> grapeVine1;
	public static BlockDefinition grapeLeaves;
	public static BlockDefinition grapeBlock;
	public static BlockBoozeDefinition[] grapeWineFluids;
	public static ItemDefinition grapes;
	public static ItemDefinition grapeSeeds;
	public static ItemDefinition grapeWine;
	public static ItemDefinition grapeWineBucket_deprecated;
	public static ItemBucketBoozeDefinition[] grapeWineBuckets;

	public static Fluid[] grapeWineBooze;

	private Config config;

	public static Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/grapes.conf");

		//====================
		// INIT
		//====================
		grapeVine0  = new BlockTypeDefinition<BlockGrapeVine0>(new BlockGrapeVine0());
		grapeVine1  = new BlockTypeDefinition<BlockGrapeVine1>(new BlockGrapeVine1());
		grapeLeaves = new BlockDefinition(new BlockGrapeLeaves());
		grapeBlock  = new BlockDefinition(new BlockGrapeBlock());

		grapes     = new ItemDefinition(new ItemGrapes());
		grapeSeeds = new ItemDefinition(new ItemGrapeSeeds());

		grapeVine1.getBlock().setItemDrop(grapeSeeds.asStack(1));

		grapeWineBooze = new Booze[4];
		grapeWineFluids = new BlockBoozeDefinition[grapeWineBooze.length];
		grapeWineBuckets = new ItemBucketBoozeDefinition[grapeWineBooze.length];
		BoozeRegistryHelper.initializeBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, "grc.grapeWine", config.grapeWineColor);

		grapeWine        = new ItemDefinition(new ItemBoozeBottle(2, -0.3F, grapeWineBooze)
			.setColor(config.grapeWineColor)
			.setTipsy(0.60F, 900)
			.setPotionEffects(new int[] {Potion.resistance.id}, new int[] {3600}));
		grapeWineBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(grapeWineBooze)
			.setColor(config.grapeWineColor));

		//====================
		// REGISTRIES
		//====================

		GameRegistry.registerBlock(grapeVine0.getBlock(), "grc.grapeVine0");
		GameRegistry.registerBlock(grapeVine1.getBlock(), "grc.grapeVine1");
		GameRegistry.registerBlock(grapeLeaves.getBlock(), "grc.grapeLeaves");
		GameRegistry.registerBlock(grapeBlock.getBlock(), "grc.grapeBlock");

		GameRegistry.registerItem(grapes.getItem(), "grc.grapes");
		GameRegistry.registerItem(grapeSeeds.getItem(), "grc.grapeSeeds");
		GameRegistry.registerItem(grapeWine.getItem(), "grc.grapeWine");
		GameRegistry.registerItem(grapeWineBucket_deprecated.getItem(), "grc.grapeWine_bucket");

		BoozeRegistryHelper.registerBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, grapeWine, "grc.grapeWine", grapeWineBucket_deprecated);

		CellarRegistry.instance().pressing().addPressing(grapes.getItem(), grapeWineBooze[0], config.grapeWinePressingTime, 40, 0.3F);

		CoreRegistry.instance().addVineDrop(grapes.asStack(), config.vineGrapeDropRarity);

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
		OreDictionary.registerOre("foodGrapejuice", grapeWine.asStack(1, 0));
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
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		final VillageHandlerGrapes handler = new VillageHandlerGrapes();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < grapeWineBooze.length; ++i)
			{
				grapeWineBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventGrapes());

		/*String modid;

		modid = "Forestry";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				if (RecipeManagers.squeezerManager != null && ForestryAPI.activeMode != null)
				{
					if (FluidRegistry.isFluidRegistered("seedoil"))
					{
						int amount = ForestryAPI.activeMode.getIntegerSetting("squeezer.liquid.seed");
						RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[] {new ItemStack(grapeSeeds)}, FluidRegistry.getFluidStack("seedoil", amount));
					}

					Item item = GameRegistry.findItem("Forestry", "mulch");
					if (FluidRegistry.isFluidRegistered("juice") && item != null);
					{
						int amount = ForestryAPI.activeMode.getIntegerSetting("squeezer.liquid.apple");
						RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[] {new ItemStack(grapes)}, FluidRegistry.getFluidStack("juice", amount), new ItemStack(item), 10);
					}
				}

				if (BackpackManager.backpackItems[2] != null)
				{
					BackpackManager.backpackItems[2].add(new ItemStack(grapes));
					BackpackManager.backpackItems[2].add(new ItemStack(grapeSeeds));
				}

				FMLLog.info("[Growthcraft|Grapes] Successfully integrated with Forestry.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Grapes] Forestry not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
