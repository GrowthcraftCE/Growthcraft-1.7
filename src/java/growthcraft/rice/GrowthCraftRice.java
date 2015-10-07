package growthcraft.rice;

import java.io.File;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.GrowthCraftCore;
import growthcraft.rice.block.BlockPaddy;
import growthcraft.rice.block.BlockRice;
import growthcraft.rice.event.BonemealEventRice;
import growthcraft.rice.event.PlayerInteractEventRice;
import growthcraft.rice.item.ItemRice;
import growthcraft.rice.item.ItemRiceBall;
import growthcraft.rice.village.ComponentVillageRiceField;
import growthcraft.rice.village.VillageHandlerRice;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
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
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = "Growthcraft|Rice",name = "Growthcraft Rice",version = "@VERSION@",dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar")
public class GrowthCraftRice
{
	@Instance("Growthcraft|Rice")
	public static GrowthCraftRice instance;

	@SidedProxy(clientSide="growthcraft.rice.ClientProxy", serverSide="growthcraft.rice.CommonProxy")
	public static CommonProxy proxy;

	public static Block riceBlock;
	public static Block paddyField;
	public static BlockFluidBooze[] riceSakeFluids;
	public static Item rice;
	public static Item riceSake;
	public static Item riceSakeBucket_deprecated;
	public static Item riceBall;
	public static ItemBucketBooze[] riceSakeBuckets;

	public static Fluid[] riceSakeBooze;

	private growthcraft.rice.Config config;

	public static growthcraft.rice.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.rice.Config(event.getModConfigurationDirectory(), "growthcraft/rice.conf");

		//====================
		// INIT
		//====================
		riceBlock = new BlockRice();
		paddyField = new BlockPaddy();

		rice     = new ItemRice();
		riceBall = new ItemRiceBall();

		riceSakeBooze = new Booze[4];
		riceSakeFluids = new BlockFluidBooze[riceSakeBooze.length];
		riceSakeBuckets = new ItemBucketBooze[riceSakeBooze.length];
		BoozeRegistryHelper.initializeBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, "grc.riceSake", config.riceSakeColor);

		riceSake        = (new ItemBoozeBottle(5, -0.6F, riceSakeBooze))
			.setColor(config.riceSakeColor)
			.setTipsy(0.65F, 900)
			.setPotionEffects(new int[] {Potion.moveSpeed.id, Potion.jump.id}, new int[] {3600, 3600});
		riceSakeBucket_deprecated = (new ItemBoozeBucketDEPRECATED(riceSakeBooze))
			.setColor(config.riceSakeColor);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(riceBlock, "grc.riceBlock");
		GameRegistry.registerBlock(paddyField, "grc.paddyField");

		GameRegistry.registerItem(rice, "grc.rice");
		GameRegistry.registerItem(riceSake, "grc.riceSake");
		GameRegistry.registerItem(riceSakeBucket_deprecated, "grc.riceSake_bucket");
		GameRegistry.registerItem(riceBall, "grc.riceBall");
		BoozeRegistryHelper.registerBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, riceSake, "grc.riceSake", riceSakeBucket_deprecated);

		CellarRegistry.instance().brew().addBrewing(FluidRegistry.WATER, rice, riceSakeBooze[0], config.riceSakeBrewingTime, 25, 0.2F);

		MinecraftForge.addGrassSeed(new ItemStack(rice), config.riceSeedDropRarity);

		try
		{
			MapGenStructureIO.func_143031_a(ComponentVillageRiceField.class, "grc.ricefield");
		}
		catch (Throwable e) {}

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

		VillageHandlerRice handler = new VillageHandlerRice();
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);

		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(riceBlock, 1, 7));

		new growthcraft.rice.integration.Waila();
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

				for (int i = 0; i < riceSakeBooze.length; ++i)
				{
					if (i == 0)
					{
						ThaumcraftApi.registerObjectTag(riceSake.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(riceSakeBucket_deprecated.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(riceSake.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(riceSakeBucket_deprecated.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
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
