package growthcraft.grapes;

import java.io.File;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.CoreRegistry;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.block.BlockGrapeBlock;
import growthcraft.grapes.block.BlockGrapeLeaves;
import growthcraft.grapes.block.BlockGrapeVine0;
import growthcraft.grapes.block.BlockGrapeVine1;
import growthcraft.grapes.event.BonemealEventGrapes;
import growthcraft.grapes.item.ItemGrapes;
import growthcraft.grapes.item.ItemGrapeSeeds;
import growthcraft.grapes.village.ComponentVillageGrapeVineyard;
import growthcraft.grapes.village.VillageHandlerGrapes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "Growthcraft|Grapes",name = "Growthcraft Grapes",version = "@VERSION@",dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar")
public class GrowthCraftGrapes
{
	@Instance("Growthcraft|Grapes")
	public static GrowthCraftGrapes instance;

	@SidedProxy(clientSide="growthcraft.grapes.ClientProxy", serverSide="growthcraft.grapes.CommonProxy")
	public static CommonProxy proxy;

	public static BlockGrapeVine0 grapeVine0;
	public static BlockGrapeVine1 grapeVine1;
	public static Block grapeLeaves;
	public static Block grapeBlock;
	public static BlockFluidBooze[] grapeWineFluids;
	public static Item grapes;
	public static Item grapeSeeds;
	public static Item grapeWine;
	public static Item grapeWineBucket_deprecated;
	public static ItemBucketBooze[] grapeWineBuckets;

	public static Fluid[] grapeWineBooze;

	private growthcraft.grapes.Config config;

	public static growthcraft.grapes.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.grapes.Config(event.getModConfigurationDirectory(), "growthcraft/grapes.conf");

		//====================
		// INIT
		//====================
		grapeVine0  = new BlockGrapeVine0();
		grapeVine1  = new BlockGrapeVine1();
		grapeLeaves = new BlockGrapeLeaves();
		grapeBlock  = new BlockGrapeBlock();

		grapes     = new ItemGrapes();
		grapeSeeds = new ItemGrapeSeeds();

		grapeWineBooze = new Booze[4];
		grapeWineFluids = new BlockFluidBooze[grapeWineBooze.length];
		grapeWineBuckets = new ItemBucketBooze[grapeWineBooze.length];
		BoozeRegistryHelper.initializeBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, "grc.grapeWine", config.grapeWineColor);

		grapeWine        = (new ItemBoozeBottle(2, -0.3F, grapeWineBooze))
			.setColor(config.grapeWineColor)
			.setTipsy(0.60F, 900)
			.setPotionEffects(new int[] {Potion.resistance.id}, new int[] {3600});
		grapeWineBucket_deprecated = (new ItemBoozeBucketDEPRECATED(grapeWineBooze))
			.setColor(config.grapeWineColor);

		//====================
		// REGISTRIES
		//====================

		GameRegistry.registerBlock(grapeVine0, "grc.grapeVine0");
		GameRegistry.registerBlock(grapeVine1, "grc.grapeVine1");
		GameRegistry.registerBlock(grapeLeaves, "grc.grapeLeaves");
		GameRegistry.registerBlock(grapeBlock, "grc.grapeBlock");

		GameRegistry.registerItem(grapes, "grc.grapes");
		GameRegistry.registerItem(grapeSeeds, "grc.grapeSeeds");
		GameRegistry.registerItem(grapeWine, "grc.grapeWine");
		GameRegistry.registerItem(grapeWineBucket_deprecated, "grc.grapeWine_bucket");

		BoozeRegistryHelper.registerBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, grapeWine, "grc.grapeWine", grapeWineBucket_deprecated);

		CellarRegistry.instance().pressing().addPressing(grapes, grapeWineBooze[0], config.grapeWinePressingTime, 40, 0.3F);

		CoreRegistry.instance().addVineDrop(new ItemStack(grapes), config.vineGrapeDropRarity);

		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(grapes), 1, 2, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(grapes), 1, 2, 10));

		try
		{
			MapGenStructureIO.func_143031_a(ComponentVillageGrapeVineyard.class, "grc.grapevineyard");
		}
		catch (Throwable e) {}

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(grapeLeaves, 30, 60);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropGrapes", grapes);
		OreDictionary.registerOre("foodGrapes", grapes);
		OreDictionary.registerOre("seedGrapes", grapeSeeds);
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("cropGrape", grapes);
		OreDictionary.registerOre("seedGrape", grapeSeeds);
		OreDictionary.registerOre("foodGrapejuice", new ItemStack(grapeWine, 1, 0));
		OreDictionary.registerOre("listAllseed", grapeSeeds);
		OreDictionary.registerOre("listAllfruit", grapes);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(new ItemStack(grapeSeeds, 1), grapes);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		VillageHandlerGrapes handler = new VillageHandlerGrapes();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);

		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(grapeBlock));
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
		}

		modid = "Thaumcraft";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(grapeSeeds.itemID, -1, new AspectList().add(Aspect.SEED, 1));
				ThaumcraftApi.registerObjectTag(grapes.itemID, -1, new AspectList().add(Aspect.CROP, 2).add(Aspect.HUNGER, 1));

				for (int i = 0; i < grapeWineBooze.length; ++i)
				{
					if (i == 0 || i == 4)
					{
						ThaumcraftApi.registerObjectTag(grapeWine.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(grapeWineBucket_deprecated.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(grapeWine.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(grapeWineBucket_deprecated.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
					}
				}

				FMLLog.info("[Growthcraft|Grapes] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Grapes] Thaumcraft not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
