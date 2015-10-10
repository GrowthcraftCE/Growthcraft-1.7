package growthcraft.hops;

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
import growthcraft.core.integration.NEI;
import growthcraft.hops.block.BlockHops;
import growthcraft.hops.event.BonemealEventHops;
import growthcraft.hops.item.ItemHops;
import growthcraft.hops.item.ItemHopSeeds;
import growthcraft.hops.village.ComponentVillageHopVineyard;
import growthcraft.hops.village.VillageHandlerHops;

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
import net.minecraft.init.Items;
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

@Mod(modid = "Growthcraft|Hops",name = "Growthcraft Hops",version = "@VERSION@",dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar")
public class GrowthCraftHops
{
	@Instance("Growthcraft|Hops")
	public static GrowthCraftHops instance;

	@SidedProxy(clientSide="growthcraft.hops.ClientProxy", serverSide="growthcraft.hops.CommonProxy")
	public static CommonProxy proxy;

	public static BlockHops hopVine;
	public static BlockFluidBooze[] hopAleFluids;

	public static Item hops;
	public static Item hopSeeds;
	public static Item hopAle;
	public static Item hopAleBucket_deprecated;
	public static ItemBucketBooze[] hopAleBuckets;

	public static Fluid[] hopAleBooze;

	private growthcraft.hops.Config config;

	public static growthcraft.hops.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.hops.Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/hops.conf");

		//====================
		// INIT
		//====================
		hopVine  = new BlockHops();

		hops     = new ItemHops();
		hopSeeds = new ItemHopSeeds();

		hopAleBooze = new Booze[5];
		hopAleFluids = new BlockFluidBooze[hopAleBooze.length];
		hopAleBuckets = new ItemBucketBooze[hopAleBooze.length];
		BoozeRegistryHelper.initializeBooze(hopAleBooze, hopAleFluids, hopAleBuckets, "grc.hopAle", config.hopAleColor);

		hopAle        = (new ItemBoozeBottle(5, -0.6F, hopAleBooze))
			.setColor(config.hopAleColor)
			.setTipsy(0.70F, 900)
			.setPotionEffects(new int[] {Potion.digSpeed.id}, new int[] {3600});
		hopAleBucket_deprecated = (new ItemBoozeBucketDEPRECATED(hopAleBooze))
			.setColor(config.hopAleColor);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(hopVine, "grc.hopVine");

		GameRegistry.registerItem(hops, "grc.hops");
		GameRegistry.registerItem(hopSeeds, "grc.hopSeeds");
		GameRegistry.registerItem(hopAle, "grc.hopAle");
		GameRegistry.registerItem(hopAleBucket_deprecated, "grc.hopAle_bucket");

		BoozeRegistryHelper.registerBooze(hopAleBooze, hopAleFluids, hopAleBuckets, hopAle, "grc.hopAle", hopAleBucket_deprecated);

		CellarRegistry.instance().brew().addBrewing(FluidRegistry.WATER, Items.wheat, hopAleBooze[4], config.hopAleBrewTime, 40, 0.3F);
		CellarRegistry.instance().brew().addBrewing(hopAleBooze[4], hops, hopAleBooze[0], config.hopAleHoppedBrewTime, 40, 0.0F);

		CoreRegistry.instance().addVineDrop(new ItemStack(hops, 2), config.hopsVineDropRarity);

		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(hops), 1, 2, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(hops), 1, 2, 10));

		try
		{
			MapGenStructureIO.func_143031_a(ComponentVillageHopVineyard.class, "grc.hopvineyard");
		}
		catch (Throwable e) {}

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropHops", hops);
		OreDictionary.registerOre("materialHops", hops);
		OreDictionary.registerOre("seedHops", hopSeeds);
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", hopSeeds);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(new ItemStack(hopSeeds, 1), hops);

		NEI.hideItem(new ItemStack(hopVine));

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		VillageHandlerHops handler = new VillageHandlerHops();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);

		FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", new ItemStack(hopVine, 1, 3));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < hopAleBooze.length; ++i)
			{
				hopAleBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventHops());

		/*String modid;

		modid = "Forestry";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				if (RecipeManagers.fermenterManager != null)
				{
					addFermenterRecipe(new ItemStack(hops), 100, "biomass");
				}

				if (RecipeManagers.squeezerManager != null && ForestryAPI.activeMode != null)
				{
					if (FluidRegistry.isFluidRegistered("seedoil"))
					{
						int amount = ForestryAPI.activeMode.getIntegerSetting("squeezer.liquid.seed");
						RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[] {new ItemStack(hopSeeds)}, FluidRegistry.getFluidStack("seedoil", amount));
					}
				}

				if (BackpackManager.backpackItems[2] != null)
				{
					BackpackManager.backpackItems[2].add(new ItemStack(hopSeeds));
					BackpackManager.backpackItems[2].add(new ItemStack(hops));
				}

				FMLLog.info("[Growthcraft|Hops] Successfully integrated with Forestry.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Hops] Forestry not found. No integration made.", new Object[0]);
			}
		}

		modid = "Thaumcraft";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(hopSeeds.itemID, -1, new AspectList().add(Aspect.SEED, 1));
				ThaumcraftApi.registerObjectTag(hops.itemID, -1, new AspectList().add(Aspect.PLANT, 1));

				for (int i = 0; i < hopAleBooze.length; ++i)
				{
					if (i == 0 || i == 4)
					{
						ThaumcraftApi.registerObjectTag(hopAle.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(hopAleBucket_deprecated.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(hopAle.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(hopAleBucket_deprecated.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
					}
				}

				FMLLog.info("[Growthcraft|Hops] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Hops] Thaumcraft not found. No integration made.", new Object[0]);
			}
		}*/
	}

	/*private void addFermenterRecipe(ItemStack stack, int value, String fluid)
	{
		RecipeManagers.fermenterManager.addRecipe(stack, value, 1.0F, FluidRegistry.getFluidStack(fluid, 1), FluidRegistry.getFluidStack("water", 1));

		if (FluidRegistry.isFluidRegistered("juice"))
		{
			RecipeManagers.fermenterManager.addRecipe(stack, value, 1.5F, FluidRegistry.getFluidStack(fluid, 1), FluidRegistry.getFluidStack("juice", 1));
		}

		if (FluidRegistry.isFluidRegistered("honey"))
		{
			RecipeManagers.fermenterManager.addRecipe(stack, value, 1.5F, FluidRegistry.getFluidStack(fluid, 1), FluidRegistry.getFluidStack("honey", 1));
		}
	}*/
}
