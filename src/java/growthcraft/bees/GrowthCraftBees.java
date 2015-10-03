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
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucket;
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
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
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

	public static int villagerApiarist_id;

	public static Block beeBox;
	public static Block beeHive;
	public static Item honeyComb;
	public static Item honeyJar;
	public static Item bee;
	public static Item honeyMead;
	public static Item honeyMead_bucket;

	public static Fluid[] honeyMead_booze;

	public static String beeBiomesList;
	public static boolean beeUseBiomeDict;
	public static int beeWorldGen_density;
	public static float beeBox_growth;
	public static float beeBox_growth2;
	public static boolean config_genApiarist;

	public static final int color = 10707212;

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		//====================
		// CONFIGURATION
		//====================
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "growthcraft/bees.conf"));
		try
		{
			config.load();

			villagerApiarist_id = config.get("Villager", "Apiarist ID", 14).getInt();

			Property cfgA = config.get(Configuration.CATEGORY_GENERAL, "Biomes (IDs) That Generate Beehives", "1;4;18;27;28;129;132;155;156");
			cfgA.comment = "Separate the IDs with ';' (without the quote marks)";
			this.beeBiomesList = cfgA.getString();

			Property cfgB = config.get(Configuration.CATEGORY_GENERAL, "Enable Biome Dictionary compatability?", true);
			cfgB.comment = "Default : true  || false = Disable";
			this.beeUseBiomeDict = cfgB.getBoolean(true);

			int v = 2;
			Property cfgC = config.get(Configuration.CATEGORY_GENERAL, "Bee Hive WorldGen density", v);
			cfgC.comment = "[Higher -> Denser] Default : " + v;
			this.beeWorldGen_density = cfgC.getInt(v);

			double f = 18.75D;
			Property cfgD = config.get(Configuration.CATEGORY_GENERAL, "Bee Box Honeycomb spawn rate", f);
			cfgD.comment = "[Higher -> Slower] Default : " + f;
			this.beeBox_growth = (float) cfgD.getDouble(f);

			f = 6.25D;
			Property cfgE = config.get(Configuration.CATEGORY_GENERAL, "Bee Box Honey and Bee spawn rate", f);
			cfgE.comment = "[Higher -> Slower] Default : " + f;
			this.beeBox_growth2 = (float) cfgE.getDouble(f);

			boolean b = false;
			Property genApiarist = config.get(Configuration.CATEGORY_GENERAL, "Spawn Village Apiarist Structure", b);
			genApiarist.comment = "Should the apiarist structure be generated in villages? : " + b;
			this.config_genApiarist = (boolean)genApiarist.getBoolean(b);
		}
		finally
		{
			if (config.hasChanged()) { config.save(); }
		}

		//====================
		// INIT
		//====================
		beeBox  = (new BlockBeeBox());
		beeHive = (new BlockBeeHive());

		honeyComb = (new ItemHoneyComb());
		honeyJar  = (new ItemHoneyJar());
		bee       = (new ItemBee());

		honeyMead_booze = new Booze[4];
		for (int i = 0; i < honeyMead_booze.length; ++i)
		{
			honeyMead_booze[i]  = (new Booze("grc.honeyMead" + i));
			FluidRegistry.registerFluid(honeyMead_booze[i]);
		}
		CellarRegistry.instance().createBooze(honeyMead_booze, this.color, "fluid.grc.honeyMead");

		honeyMead        = (new ItemBoozeBottle(6, -0.45F, honeyMead_booze)).setColor(this.color).setTipsy(0.60F, 900).setPotionEffects(new int[] {Potion.regeneration.id}, new int[] {900});
		honeyMead_bucket = (new ItemBoozeBucket(honeyMead_booze)).setColor(this.color);

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
			FluidStack stack = new FluidStack(honeyMead_booze[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(honeyMead_bucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);

			FluidStack stack2 = new FluidStack(honeyMead_booze[i].getID(), GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack2, new ItemStack(honeyMead, 1, i), GrowthCraftCellar.EMPTY_BOTTLE);
		}

		GameRegistry.registerTileEntity(TileEntityBeeBox.class, "grc.tileentity.beeBox");
		//CellarRegistry.instance().addBoozeAlternative(FluidRegistry.LAVA, honeyMead_booze[0]);

		BeesRegistry.instance().addBee(bee);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 0);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 1);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 2);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 3);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 4);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 5);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 6);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 7);
		BeesRegistry.instance().addFlower(Blocks.red_flower, 8);
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
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.villagerBrewer_id, new VillageHandlerBees());
		VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftBees.villagerApiarist_id, handler);

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
