package growthcraft.bees;

import java.io.File;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.bees.block.BlockBeeBox;
import growthcraft.bees.block.BlockBeeHive;
import growthcraft.bees.tileentity.TileEntityBeeBox;
import growthcraft.bees.gui.GuiHandlerBees;
import growthcraft.bees.item.ItemBee;
import growthcraft.bees.item.ItemHoneyComb;
import growthcraft.bees.item.ItemHoneyJar;
import growthcraft.bees.village.ComponentVillageApiarist;
import growthcraft.bees.village.VillageHandlerBees;
import growthcraft.bees.village.VillageHandlerBeesApiarist;
import growthcraft.bees.world.WorldGeneratorBees;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.handler.BucketHandler;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = "Growthcraft|Bees",name = "GrowthCraft Bees",version = "@VERSION@",dependencies = "required-after:Growthcraft;required-after:Growthcraft|Cellar")
public class GrowthCraftBees
{
	@Instance("Growthcraft|Bees")
	public static GrowthCraftBees instance;

	@SidedProxy(clientSide="growthcraft.bees.ClientProxy", serverSide="growthcraft.bees.CommonProxy")
	public static CommonProxy proxy;

	public static Block beeBox;
	public static Block beeHive;
	public static BlockFluidBooze[] honeyMeadFluids;
	public static Item honeyComb;
	public static Item honeyJar;
	public static Item bee;
	public static Item honeyMead;
	public static Item honeyMead_bucket;
	public static ItemBucketBooze[] honeyMeadBuckets;

	public static Fluid[] honeyMead_booze;

	private growthcraft.bees.Config config;

	public static growthcraft.bees.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.bees.Config(event.getModConfigurationDirectory(), "growthcraft/bees.conf");

		//====================
		// INIT
		//====================
		beeBox  = (new BlockBeeBox());
		beeHive = (new BlockBeeHive());

		honeyComb = (new ItemHoneyComb());
		honeyJar  = (new ItemHoneyJar());
		bee       = (new ItemBee());

		honeyMead_booze = new Booze[4];
		honeyMeadFluids = new BlockFluidBooze[honeyMead_booze.length];
		honeyMeadBuckets = new ItemBucketBooze[honeyMead_booze.length];
		for (int i = 0; i < honeyMead_booze.length; ++i)
		{
			honeyMead_booze[i]  = new Booze("grc.honeyMead" + i);
			FluidRegistry.registerFluid(honeyMead_booze[i]);
			honeyMeadFluids[i] = new BlockFluidBooze(honeyMead_booze[i], this.color);
			honeyMeadBuckets[i] = new ItemBucketBooze(honeyMeadFluids[i], honeyMead_booze, i).setColor(this.color);
		}
		CellarRegistry.instance().booze().createBooze(honeyMead_booze, config.honeyMeadColor, "fluid.grc.honeyMead");

		honeyMead        = (new ItemBoozeBottle(6, -0.45F, honeyMead_booze))
			.setColor(config.honeyMeadColor)
			.setTipsy(0.60F, 900)
			.setPotionEffects(new int[] {Potion.regeneration.id}, new int[] {900});
		honeyMead_bucket = (new ItemBoozeBucketDEPRECATED(honeyMead_booze))
			.setColor(config.honeyMeadColor);

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(beeBox, "grc.beeBox");
		GameRegistry.registerBlock(beeHive, "grc.beeHive");

		GameRegistry.registerItem(honeyComb, "grc.honeyComb");
		GameRegistry.registerItem(honeyJar, "grc.honeyJar");
		GameRegistry.registerItem(bee, "grc.bee");
		GameRegistry.registerItem(honeyMead, "grc.honeyMead");
		GameRegistry.registerItem(honeyMead_bucket, "grc.honeyMead_bucket");

		for (int i = 0; i < honeyMead_booze.length; ++i)
		{
			GameRegistry.registerItem(honeyMeadBuckets[i], "grc.honeyMeadBucket." + i);
			GameRegistry.registerBlock(honeyMeadFluids[i], "grc.honeyMeadFluid." + i);
			// forward compat recipe
			GameRegistry.addShapelessRecipe(new ItemStack(honeyMeadBuckets[i], 1), new ItemStack(honeyMead_bucket, 1, i));

			BucketHandler.instance().register(honeyMeadFluids[i], honeyMeadBuckets[i]);

			FluidStack stack = new FluidStack(honeyMead_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(honeyMead_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(honeyMeadBuckets[i]), FluidContainerRegistry.EMPTY_BUCKET);

			FluidStack stack2 = new FluidStack(honeyMead_booze[i].getID(), GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack2, new ItemStack(honeyMead, 1, i), GrowthCraftCellar.EMPTY_BOTTLE);
		}

		GameRegistry.registerTileEntity(TileEntityBeeBox.class, "grc.tileentity.beeBox");
		//CellarRegistry.instance().addBoozeAlternative(FluidRegistry.LAVA, honeyMead_booze[0]);

		BeesRegistry.instance().addBee(bee);
		for (int i = 0; i < 9; ++i) {
			BeesRegistry.instance().addFlower(Blocks.red_flower, i);
		}
		BeesRegistry.instance().addFlower(Blocks.yellow_flower, 0);

		GameRegistry.registerWorldGenerator(new WorldGeneratorBees(), 0);

		try
		{
			MapGenStructureIO.func_143031_a(ComponentVillageApiarist.class, "grc.apiarist");
		}
		catch (Throwable e) {}

		//====================
		// ORE DICTIONARY
		//====================
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("beeQueen", bee);
		OreDictionary.registerOre("materialWaxcomb", new ItemStack(honeyComb, 1, 0));
		OreDictionary.registerOre("materialHoneycomb", new ItemStack(honeyComb, 1, 1));
		OreDictionary.registerOre("honeyDrop", honeyJar);
		OreDictionary.registerOre("dropHoney", honeyJar);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(beeBox, 1), " A ", "A A", "AAA", 'A', "plankWood"));
		ItemStack stack = new ItemStack(honeyComb, 1, 1);
		GameRegistry.addShapelessRecipe(new ItemStack(honeyJar, 1), stack, stack, stack, stack, stack, stack, Items.flower_pot);
		GameRegistry.addShapelessRecipe(new ItemStack(honeyMead_bucket, 1, 0), Items.water_bucket, honeyJar, Items.bucket);

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
			for (int i = 0; i < honeyMead_booze.length; ++i)
			{
				honeyMead_booze[i].setIcons(GrowthCraftCore.liquidSmoothTexture);
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
		}

		modid = "Thaumcraft";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(honeyComb.itemID, -1, new AspectList().add(Aspect.SLIME, 2));
				ThaumcraftApi.registerObjectTag(honeyJar.itemID, -1, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(bee.itemID, -1, new AspectList().add(Aspect.BEAST, 1).add(Aspect.AIR, 1));
				ThaumcraftApi.registerObjectTag(beeBox.blockID, -1, new AspectList().add(Aspect.AIR, 2));
				ThaumcraftApi.registerObjectTag(beeHive.blockID, -1, new AspectList().add(Aspect.AIR, 2));

				for (int i = 0; i < honeyMead_booze.length; ++i)
				{
					if (i == 0 || i == 4)
					{
						ThaumcraftApi.registerObjectTag(honeyMead.itemID, i, new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(honeyMead_bucket.itemID, i, new AspectList().add(Aspect.WATER, 2));
					}
					else
					{
						int m = i == 2 ? 4 : 2;
						ThaumcraftApi.registerObjectTag(honeyMead.itemID, i, new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
						ThaumcraftApi.registerObjectTag(honeyMead_bucket.itemID, i, new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
					}
				}

				FMLLog.info("[Growthcraft|Bees] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Bees] Thaumcraft not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
