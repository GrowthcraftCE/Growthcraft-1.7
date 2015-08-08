package growthcraft.apples;

import java.io.File;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.apples.block.BlockApple;
import growthcraft.apples.block.BlockAppleLeaves;
import growthcraft.apples.block.BlockAppleSapling;
import growthcraft.apples.event.BonemealEventApples;
import growthcraft.apples.item.ItemAppleSeeds;
import growthcraft.apples.village.VillageHandlerApples;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucket;
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
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrowthCraftApples
{
	@Instance("Growthcraft|Apples")
	public static GrowthCraftApples instance;

	@SidedProxy(clientSide="growthcraft.apples.ClientProxy", serverSide="growthcraft.apples.CommonProxy")
	public static CommonProxy proxy;

	public static Block appleSapling;
	public static Block appleLeaves;
	public static Block appleBlock;
	public static Item appleSeeds;
	public static Item appleCider;
	public static Item appleCider_bucket;

	public static Fluid[] appleCider_booze;

	public static boolean appleBlock_dropFlag;
	public static int appleBlock_growth;
	public static int appleBlock_dropChance;
	public static int appleLeaves_growth;
	public static int appleSapling_growth;
	public static int appleCider_speed;

	public static final int color = 8737829;

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		//====================
		// CONFIGURATION
		//====================
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "growthcraft/apples.conf"));
		try
		{
			config.load();

			int v = 8;
			Property cfgB = config.get(Configuration.CATEGORY_GENERAL, "Apple growth rate", v);
			cfgB.comment = "[Higher -> Slower] Default : " + v;
			this.appleBlock_growth = cfgB.getInt(v);

			Property cfgA = config.get(Configuration.CATEGORY_GENERAL, "Allow natural apple falling?", true);
			cfgA.comment = "Default : true";
			this.appleBlock_dropFlag = cfgA.getBoolean(true);

			v = 8;
			Property cfgC = config.get(Configuration.CATEGORY_GENERAL, "Apple natural falling rate", v);
			cfgC.comment = "[Higher -> Slower] Default : " + v;
			this.appleBlock_dropChance = cfgC.getInt(v);

			v = 25;
			Property cfgD = config.get(Configuration.CATEGORY_GENERAL, "Apple Leaves apple spawn rate", v);
			cfgD.comment = "[Higher -> Slower] Default : " + v;
			this.appleLeaves_growth = cfgD.getInt(v);

			v = 7;
			Property cfgE = config.get(Configuration.CATEGORY_GENERAL, "Apple Sapling growth rate", v);
			cfgE.comment = "[Higher -> Slower] Default : " + v;
			this.appleSapling_growth = cfgE.getInt(v);

			v = 20;
			Property cfgF = config.get(Configuration.CATEGORY_GENERAL, "Apple Cider press time", v);
			cfgF.comment = "[Higher -> Slower] Default : " + v;
			this.appleCider_speed = cfgF.getInt(v);
		}
		finally
		{
			if (config.hasChanged()) { config.save(); }
		}

		//====================
		// INIT
		//====================
		appleSapling      = (new BlockAppleSapling());
		appleLeaves       = (new BlockAppleLeaves());
		appleBlock        = (new BlockApple());

		appleSeeds        = (new ItemAppleSeeds());

		appleCider_booze = new Booze[4];
		for (int i = 0; i < appleCider_booze.length; ++i)
		{
			appleCider_booze[i]  = (new Booze("grc.appleCider" + i));
			FluidRegistry.registerFluid(appleCider_booze[i]);
		}
		CellarRegistry.instance().createBooze(appleCider_booze, this.color, "fluid.grc.appleCider");

		appleCider        = (new ItemBoozeBottle(4, -0.3F, appleCider_booze)).setColor(this.color).setTipsy(0.60F, 900).setPotionEffects(new int[] {Potion.field_76444_x.id}, new int[] {1800});
		appleCider_bucket = (new ItemBoozeBucket(appleCider_booze)).setColor(this.color);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(appleSapling, "grc.appleSapling");
		GameRegistry.registerBlock(appleLeaves, "grc.appleLeaves");
		GameRegistry.registerBlock(appleBlock, "grc.appleBlock");

		GameRegistry.registerItem(appleSeeds, "grc.appleSeeds");
		GameRegistry.registerItem(appleCider, "grc.appleCider");
		GameRegistry.registerItem(appleCider_bucket, "grc.appleCider_bucket");

		for (int i = 0; i < appleCider_booze.length; ++i)
		{
			FluidStack stack = new FluidStack(appleCider_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(appleCider_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);

			FluidStack stack2 = new FluidStack(appleCider_booze[i].getID(), GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack2, new ItemStack(appleCider, 1, i), GrowthCraftCellar.EMPTY_BOTTLE);
		}

		CellarRegistry.instance().addPressing(Items.apple, appleCider_booze[0], this.appleCider_speed, 40, 0.3F);

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
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.villagerBrewer_id, new VillageHandlerApples());

		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(appleBlock, 1, 2));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < appleCider_booze.length; ++i)
			{
				appleCider_booze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
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

				for (int i = 0; i < appleCider_booze.length; ++i)
				{
					if (i == 0 || i == 4)
					{
						ThaumcraftApi.registerObjectTag(appleCider.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(appleCider_bucket.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(appleCider.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(appleCider_bucket.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
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
