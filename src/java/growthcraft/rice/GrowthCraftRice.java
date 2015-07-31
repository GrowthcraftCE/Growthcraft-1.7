package growthcraft.rice;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucket;
import growthcraft.core.GrowthCraftCore;

import java.io.File;

import net.minecraft.block.Block;
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
import net.minecraftforge.oredict.ShapedOreRecipe;
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

@Mod(modid = "Growthcraft|Rice",name = "Growthcraft Rice",version = "2.1.0a",dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar")

public class GrowthCraftRice
{
	@Instance("Growthcraft|Rice")
	public static GrowthCraftRice instance;

	@SidedProxy(clientSide="growthcraft.rice.ClientProxy", serverSide="growthcraft.rice.CommonProxy")
	public static CommonProxy proxy;

	public static Block riceBlock;
	public static Block paddyField;
	public static Item rice;
	public static Item riceSake;
	public static Item riceSake_bucket;
	public static Item riceBall;

	public static Fluid[] riceSake_booze;

	public static float riceBlock_growth;
	public static int rice_grassDropChance;
	public static int riceSake_speed;

	public static final int color = 15331319;

	// Constants
	public static final int paddyFieldMax = 7;

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		//====================
		// CONFIGURATION
		//====================
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "growthcraft/rice.conf"));
		try
		{
			config.load();

			double f = 25.0D;
			Property cfgA = config.get(Configuration.CATEGORY_GENERAL, "Rice growth rate", f);
			cfgA.comment = "[Higher -> Slower] Default : " + f;
			this.riceBlock_growth = (float) cfgA.getDouble(f);

			int v = 3;
			Property cfgB = config.get(Configuration.CATEGORY_GENERAL, "Rice grass drop rarity", v);
			cfgB.comment = "[Higher -> Rarer] Default : " + v;
			this.rice_grassDropChance = cfgB.getInt(v);

			v = 20;
			Property cfgC = config.get(Configuration.CATEGORY_GENERAL, "Rice Sake brew time", v);
			cfgC.comment = "[Higher -> Slower] Default : " + v;
			this.riceSake_speed = cfgC.getInt(v);
		}
		finally
		{
			if (config.hasChanged()) { config.save(); }

		}

		//====================
		// INIT
		//====================
		riceBlock = (new BlockRice());
		paddyField = (new BlockPaddy());

		rice     = (new ItemRice());
		riceBall = (new ItemRiceBall());

		riceSake_booze = new Booze[4];
		for (int i = 0; i < riceSake_booze.length; ++i)
		{
			riceSake_booze[i]  = (new Booze("grc.riceSake" + i));
			FluidRegistry.registerFluid(riceSake_booze[i]);
		}
		CellarRegistry.instance().createBooze(riceSake_booze, this.color, "fluid.grc.riceSake");

		riceSake        = (new ItemBoozeBottle(5, -0.6F, riceSake_booze)).setColor(this.color).setTipsy(0.65F, 900).setPotionEffects(new int[] {Potion.moveSpeed.id, Potion.jump.id}, new int[] {3600, 3600});
		riceSake_bucket = (new ItemBoozeBucket(riceSake_booze)).setColor(this.color);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(riceBlock, "grc.riceBlock");
		GameRegistry.registerBlock(paddyField, "grc.paddyField");

		GameRegistry.registerItem(rice, "grc.rice");
		GameRegistry.registerItem(riceSake, "grc.riceSake");
		GameRegistry.registerItem(riceSake_bucket, "grc.riceSake_bucket");
		GameRegistry.registerItem(riceBall, "grc.riceBall");

		for (int i = 0; i < riceSake_booze.length; ++i)
		{
			FluidStack stack = new FluidStack(riceSake_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(riceSake_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);

			FluidStack stack2 = new FluidStack(riceSake_booze[i].getID(), GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack2, new ItemStack(riceSake, 1, i), GrowthCraftCellar.EMPTY_BOTTLE);
		}

		CellarRegistry.instance().addBrewing(FluidRegistry.WATER, rice, riceSake_booze[0], this.riceSake_speed, 25, 0.2F);

		MinecraftForge.addGrassSeed(new ItemStack(rice), this.rice_grassDropChance);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropRice", rice);
		OreDictionary.registerOre("seedRice", rice);
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", rice);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(riceBall, 1), "###", "###", '#', "cropRice"));

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.villagerBrewer_id, new VillageHandlerRice());

		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(riceBlock, 1, 7));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < riceSake_booze.length; ++i)
			{
				riceSake_booze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventRice());
		//MinecraftForge.EVENT_BUS.register(new UseHoeEventCore());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventRice());

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
						RecipeManagers.squeezerManager.addRecipe(10, new ItemStack[] {new ItemStack(rice)}, FluidRegistry.getFluidStack("seedoil", amount));
					}
				}

				if (BackpackManager.backpackItems[2] != null)
				{
					BackpackManager.backpackItems[2].add(new ItemStack(rice));
				}

				FMLLog.info("[Growthcraft|Rice] Successfully integrated with Forestry.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Rice] Forestry not found. No integration made.", new Object[0]);
			}
		}

		modid = "Thaumcraft";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(rice.itemID, -1, new AspectList().add(Aspect.CROP, 2).add(Aspect.SEED, 1).add(Aspect.HUNGER, 1));

				for (int i = 0; i < riceSake_booze.length; ++i)
				{
					if (i == 0)
					{
						ThaumcraftApi.registerObjectTag(riceSake.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(riceSake_bucket.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(riceSake.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(riceSake_bucket.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
					}
				}

				FMLLog.info("[Growthcraft|Rice] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Rice] Thaumcraft not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
