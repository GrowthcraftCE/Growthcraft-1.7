package growthcraft.apples;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.apples.block.BlockApple;
import growthcraft.apples.block.BlockAppleLeaves;
import growthcraft.apples.block.BlockAppleSapling;
import growthcraft.apples.event.BonemealEventApples;
import growthcraft.apples.item.ItemAppleSeeds;
import growthcraft.apples.village.ComponentVillageAppleFarm;
import growthcraft.apples.village.VillageHandlerApples;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.handler.BucketHandler;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.GrowthCraftCore;

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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "Growthcraft|Apples",name = "Growthcraft Apples",version = "@VERSION@",dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar")
public class GrowthCraftApples
{
	@Instance("Growthcraft|Apples")
	public static GrowthCraftApples instance;

	@SidedProxy(clientSide="growthcraft.apples.ClientProxy", serverSide="growthcraft.apples.CommonProxy")
	public static CommonProxy proxy;

	public static Block appleSapling;
	public static Block appleLeaves;
	public static Block appleBlock;
	public static BlockFluidBooze[] appleCiderFluids;
	public static Item appleSeeds;
	public static Item appleCider;
	public static Item appleCiderBucket_deprecated;
	public static ItemBucketBooze[] appleCiderBuckets;

	public static Fluid[] appleCiderBooze;

	private growthcraft.apples.Config config;

	public static growthcraft.apples.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.apples.Config(event.getModConfigurationDirectory(), "growthcraft/apples.conf");

		//====================
		// INIT
		//====================
		appleSapling      = new BlockAppleSapling();
		appleLeaves       = new BlockAppleLeaves();
		appleBlock        = new BlockApple();

		appleSeeds        = new ItemAppleSeeds();

		appleCiderBooze = new Booze[4];
		appleCiderFluids = new BlockFluidBooze[appleCiderBooze.length];
		appleCiderBuckets = new ItemBucketBooze[appleCiderBooze.length];
		BoozeRegistryHelper.initializeBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets, "grc.appleCider", config.appleCiderColor);

		appleCider        = (new ItemBoozeBottle(4, -0.3F, appleCiderBooze))
			.setColor(this.config.appleCiderColor)
			.setTipsy(0.60F, 900)
			.setPotionEffects(new int[] {Potion.field_76444_x.id}, new int[] {1800});
		appleCiderBucket_deprecated = (new ItemBoozeBucketDEPRECATED(appleCiderBooze))
			.setColor(this.config.appleCiderColor);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(appleSapling, "grc.appleSapling");
		GameRegistry.registerBlock(appleLeaves, "grc.appleLeaves");
		GameRegistry.registerBlock(appleBlock, "grc.appleBlock");

		GameRegistry.registerItem(appleSeeds, "grc.appleSeeds");
		GameRegistry.registerItem(appleCider, "grc.appleCider");
		GameRegistry.registerItem(appleCiderBucket_deprecated, "grc.appleCider_bucket");

		BoozeRegistryHelper.registerBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets, appleCider, "grc.appleCider", appleCiderBucket_deprecated);

		CellarRegistry.instance().pressing().addPressing(Items.apple, appleCiderBooze[0], this.config.appleCiderPressingTime, 40, 0.3F);

		try
		{
			MapGenStructureIO.func_143031_a(ComponentVillageAppleFarm.class, "grc.applefarm");
		}
		catch (Throwable e) {}

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(appleLeaves, 30, 60);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("saplingTree", appleSapling);
		OreDictionary.registerOre("treeSapling", appleSapling);
		OreDictionary.registerOre("seedApple", appleSeeds);
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", appleSeeds);
		OreDictionary.registerOre("foodApplejuice", new ItemStack(appleCider, 1, 0));

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(new ItemStack(appleSeeds, 1), Items.apple);

		MinecraftForge.EVENT_BUS.register(this);

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new AppleFuelHandler());

	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();
		VillageHandlerApples handler = new VillageHandlerApples();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(appleBlock, 1, 2));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < appleCiderBooze.length; ++i)
			{
				appleCiderBooze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventApples());

		/*String modid;

		modid = "Forestry";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				if (RecipeManagers.squeezerManager != null && ForestryAPI.activeMode != null && FluidRegistry.isFluidRegistered("seedoil"))
				{
					int amount = ForestryAPI.activeMode.getIntegerSetting("squeezer.liquid.seed");
					RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[] {new ItemStack(appleSeeds)}, FluidRegistry.getFluidStack("seedoil", amount));
				}

				if (BackpackManager.backpackItems[2] != null)
				{
					BackpackManager.backpackItems[2].add(new ItemStack(appleSeeds));
				}

				FMLLog.info("[Growthcraft|Apples] Successfully integrated with Forestry.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Apples] Forestry not found. No integration made.", new Object[0]);
			}
		}

		modid = "Thaumcraft";
		/*if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(appleSeeds.itemID, -1, new AspectList().add(Aspect.SEED, 1));
				ThaumcraftApi.registerObjectTag(appleSapling.blockID, -1, new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1).add(Aspect.SEED, 1));
				ThaumcraftApi.registerObjectTag(appleLeaves.blockID, -1, new AspectList().add(Aspect.PLANT, 1));

				for (int i = 0; i < appleCiderBooze.length; ++i)
				{
					if (i == 0 || i == 4)
					{
						ThaumcraftApi.registerObjectTag(appleCider.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(appleCiderBucket_deprecated.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(appleCider.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(appleCiderBucket_deprecated.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
					}
				}

				FMLLog.info("[Growthcraft|Apples] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Apples] Thaumcraft not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
