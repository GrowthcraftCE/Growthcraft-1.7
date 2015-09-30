package growthcraft.hops;

import java.io.File;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.CoreRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucket;
import growthcraft.core.GrowthCraftCore;
import growthcraft.hops.block.BlockHops;
import growthcraft.hops.event.BonemealEventHops;
import growthcraft.hops.item.ItemHopSeeds;
import growthcraft.hops.item.ItemHops;
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

	public static Item hops;
	public static Item hopSeeds;
	public static Item hopAle;
	public static Item hopAle_bucket;

	public static Fluid[] hopAle_booze;

	public static float hopVine_growth;
	public static float hopVine_growth2;
	public static int hops_vineDropChance;
	public static int hopAle_speed;
	public static int hopAle_speed2;
	public static boolean config_genHopVineyard;

	public static final int color = 13290055;

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		//====================
		// CONFIGURATION
		//====================
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "growthcraft/hops.conf"));
		try
		{
			config.load();

			double f = 25.0D;
			Property cfgA = config.get(Configuration.CATEGORY_GENERAL, "Hop (Vine) growth rate", f);
			cfgA.comment = "[Higher -> Slower] Default : " + f;
			this.hopVine_growth = (float) cfgA.getDouble(f);

			f = 40.0D;
			Property cfgB = config.get(Configuration.CATEGORY_GENERAL, "Hop (Flower) spawn rate", f);
			cfgB.comment = "[Higher -> Slower] Default : " + f;
			this.hopVine_growth2 = (float) cfgB.getDouble(f);

			int v = 10;
			Property cfgC = config.get(Configuration.CATEGORY_GENERAL, "Hops vine drop rarity", v);
			cfgC.comment = "[Lower -> Rarer] Default : " + v;
			this.hops_vineDropChance = cfgC.getInt(v);

			v = 20;
			Property cfgD = config.get(Configuration.CATEGORY_GENERAL, "Hop Ale (no hops) brew time", v);
			cfgD.comment = "[Higher -> Slower] Default : " + v;
			this.hopAle_speed = cfgD.getInt(v);

			v = 20;
			Property cfgE = config.get(Configuration.CATEGORY_GENERAL, "Hop Ale (hopped) brew time", v);
			cfgE.comment = "[Higher -> Slower] Default : " + v;
			this.hopAle_speed2 = cfgE.getInt(v);

			boolean b = true;
			Property genHopVineyard = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Hop Vineyards", v);
			genHopVineyard.comment = "Controls hop vineyards spawning in villages Default : " + b;
			this.config_genHopVineyard = genHopVineyard.getBoolean(b);
		}
		finally
		{
			if (config.hasChanged()) { config.save(); }
		}

		//====================
		// INIT
		//====================
		hopVine  = (new BlockHops());

		hops     = (new ItemHops());
		hopSeeds = (new ItemHopSeeds());

		hopAle_booze = new Booze[5];
		for (int i = 0; i < hopAle_booze.length; ++i)
		{
			hopAle_booze[i]  = (new Booze("grc.hopAle" + i));
			FluidRegistry.registerFluid(hopAle_booze[i]);
		}
		CellarRegistry.instance().createBooze(hopAle_booze, this.color, "fluid.grc.hopAle");

		hopAle        = (new ItemBoozeBottle(5, -0.6F, hopAle_booze)).setColor(this.color).setTipsy(0.70F, 900).setPotionEffects(new int[] {Potion.digSpeed.id}, new int[] {3600});
		hopAle_bucket = (new ItemBoozeBucket(hopAle_booze)).setColor(this.color);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(hopVine, "grc.hopVine");

		GameRegistry.registerItem(hops, "grc.hops");
		GameRegistry.registerItem(hopSeeds, "grc.hopSeeds");
		GameRegistry.registerItem(hopAle, "grc.hopAle");
		GameRegistry.registerItem(hopAle_bucket, "grc.hopAle_bucket");

		for (int i = 0; i < hopAle_booze.length; ++i)
		{
			FluidStack stack = new FluidStack(hopAle_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(hopAle_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);

			FluidStack stack2 = new FluidStack(hopAle_booze[i].getID(), GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack2, new ItemStack(hopAle, 1, i), GrowthCraftCellar.EMPTY_BOTTLE);
		}

		CellarRegistry.instance().addBrewing(FluidRegistry.WATER, Items.wheat, hopAle_booze[4], this.hopAle_speed, 40, 0.3F);
		CellarRegistry.instance().addBrewing(hopAle_booze[4], hops, hopAle_booze[0], this.hopAle_speed2, 40, 0.0F);

		CoreRegistry.instance().addVineDrop(new ItemStack(hops, 2), this.hops_vineDropChance);

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

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();

		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.villagerBrewer_id, new VillageHandlerHops());
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerHops());

		FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", new ItemStack(hopVine, 1, 3));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (int i = 0; i < hopAle_booze.length; ++i)
			{
				hopAle_booze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
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

				for (int i = 0; i < hopAle_booze.length; ++i)
				{
					if (i == 0 || i == 4)
					{
						ThaumcraftApi.registerObjectTag(hopAle.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(hopAle_bucket.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(hopAle.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(hopAle_bucket.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
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
