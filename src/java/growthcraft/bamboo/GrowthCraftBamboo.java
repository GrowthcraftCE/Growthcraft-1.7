package growthcraft.bamboo;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import growthcraft.bamboo.block.BlockBamboo;
import growthcraft.bamboo.block.BlockBambooDoor;
import growthcraft.bamboo.block.BlockBambooFence;
import growthcraft.bamboo.block.BlockBambooFenceGate;
import growthcraft.bamboo.block.BlockBambooLeaves;
import growthcraft.bamboo.block.BlockBambooScaffold;
import growthcraft.bamboo.block.BlockBambooShoot;
import growthcraft.bamboo.block.BlockBambooSlab;
import growthcraft.bamboo.block.BlockBambooStairs;
import growthcraft.bamboo.block.BlockBambooStalk;
import growthcraft.bamboo.block.BlockBambooWall;
import growthcraft.bamboo.entity.EntityBambooRaft;
import growthcraft.bamboo.event.BonemealEventBamboo;
import growthcraft.bamboo.item.ItemBamboo;
import growthcraft.bamboo.item.ItemBambooCoal;
import growthcraft.bamboo.item.ItemBambooDoor;
import growthcraft.bamboo.item.ItemBambooRaft;
import growthcraft.bamboo.item.ItemBambooShoot;
import growthcraft.bamboo.item.ItemBambooSlab;
import growthcraft.bamboo.village.ComponentVillageBambooYard;
import growthcraft.bamboo.village.VillageHandlerBamboo;
import growthcraft.bamboo.world.BiomeGenBamboo;
import growthcraft.bamboo.world.WorldGenBamboo;
import growthcraft.bamboo.world.WorldGeneratorBamboo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.SidedProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "Growthcraft|Bamboo",name = "Growthcraft Bamboo",version = "@VERSION@",dependencies = "required-after:Growthcraft")
public class GrowthCraftBamboo
{
	@Instance("Growthcraft|Bamboo")
	public static GrowthCraftBamboo instance;

	public static Block bambooBlock;
	public static Block bambooShoot;
	public static Block bambooStalk;
	public static Block bambooLeaves;
	public static Block bambooFence;
	public static Block bambooWall;
	public static Block bambooStairs;
	public static BlockSlab bambooSingleSlab;
	public static BlockSlab bambooDoubleSlab;
	public static Block bambooDoor;
	public static Block bambooFenceGate;
	public static Block bambooScaffold;
	public static Item bamboo;
	public static Item bambooDoorItem;
	public static Item bambooRaft;
	public static Item bambooCoal;
	public static Item bambooShootFood;

	public static BiomeGenBase bambooBiome;
	public static int bambooBiome_id;

	public static String bambooBiomesList;
	public static boolean bambooUseBiomeDict;
	public static boolean bambooGenerateBiome;
	public static int bambooWorldGen_rarity;
	public static int bambooWorldGen_density;
	public static int bambooStalk_growth;
	public static int bambooShoot_growth;
	public static boolean config_genBambooYard;

	@SidedProxy(clientSide="growthcraft.bamboo.ClientProxy", serverSide="growthcraft.bamboo.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		//====================
		// CONFIGURATION
		//====================
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "growthcraft/bamboo.conf"));
		try
		{
			//config.addCustomCategoryComment("Biomes", "Biomes");
			config.load();

			bambooBiome_id = config.get("Biomes", "Bamboo Forest biome ID", 170).getInt();

			Property cfgA = config.get(Configuration.CATEGORY_GENERAL, "Biomes (IDs) That Generate Bamboos", "0;1;4;7;18;21;22;23;149;151");
			cfgA.comment = "Separate the IDs with ';' (without the quote marks)";
			this.bambooBiomesList = cfgA.getString();

			//Ocean, Plains, Forest, River, ForestHills
			//Jungle, JungleHills, JungleEdge
			//JungleM, JungleEdgeM

			Property cfgB = config.get(Configuration.CATEGORY_GENERAL, "Enable Biome Dictionary compatability?", true);
			cfgB.comment = "Default : true  || false = Disable";
			this.bambooUseBiomeDict = cfgB.getBoolean(true);

			Property cfgC = config.get(Configuration.CATEGORY_GENERAL, "Generate Bamboo Forest biome?", true);
			cfgC.comment = "Default : true  || false = Disable";
			this.bambooGenerateBiome = cfgC.getBoolean(true);

			int v = 32;
			Property cfgD = config.get(Configuration.CATEGORY_GENERAL, "Bamboo WorldGen rarity", v);
			cfgD.comment = "[Higher -> Rarer] Default : " + v;
			this.bambooWorldGen_rarity = cfgD.getInt(v);

			v = 64;
			Property cfgG = config.get(Configuration.CATEGORY_GENERAL, "Bamboo WorldGen density", v);
			cfgG.comment = "[Higher -> Denser] Default : " + v;
			this.bambooWorldGen_density = cfgG.getInt(v);

			v = 4;
			Property cfgE = config.get(Configuration.CATEGORY_GENERAL, "Bamboo Spread rate", v);
			cfgE.comment = "[Higher -> Slower] Default : " + v;
			this.bambooStalk_growth = cfgE.getInt(v);

			v = 7;
			Property cfgF = config.get(Configuration.CATEGORY_GENERAL, "Bamboo Shoot growth rate", v);
			cfgF.comment = "[Higher -> Slower] Default : " + v;
			this.bambooShoot_growth = cfgF.getInt(v);

			boolean b = false;
			Property genBambooYard = config.get(Configuration.CATEGORY_GENERAL, "Generate Village Bamboo Yard", b);
			genBambooYard.comment = "Controls bamboo yard spawning in villages Default : " + b;
			this.config_genBambooYard = genBambooYard.getBoolean(b);
		}
		finally
		{
			if (config.hasChanged()) { config.save(); }
		}

		//====================
		// INIT
		//====================
		bambooBlock      = (new BlockBamboo());
		bambooShoot      = (new BlockBambooShoot());
		bambooStalk      = (new BlockBambooStalk());
		bambooLeaves     = (new BlockBambooLeaves());
		bambooFence      = (new BlockBambooFence());
		bambooWall       = (new BlockBambooWall());
		bambooStairs     = (new BlockBambooStairs());
		bambooSingleSlab = (BlockSlab)(new BlockBambooSlab(false));
		bambooDoubleSlab = (BlockSlab)(new BlockBambooSlab(true));
		bambooDoor       = (new BlockBambooDoor());
		bambooFenceGate  = (new BlockBambooFenceGate());
		bambooScaffold   = (new BlockBambooScaffold());

		bamboo = (new ItemBamboo());
		bambooDoorItem = (new ItemBambooDoor());
		bambooRaft = (new ItemBambooRaft());
		bambooCoal = (new ItemBambooCoal());
		bambooShootFood = (new ItemBambooShoot());

		if (bambooGenerateBiome)
		{
			bambooBiome = (new BiomeGenBamboo(bambooBiome_id)).setColor(353825).setBiomeName("BambooForest").func_76733_a(5159473).setTemperatureRainfall(0.7F, 0.8F);
		}

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(bambooBlock, "grc.bambooBlock");
		GameRegistry.registerBlock(bambooShoot, "grc.bambooShoot");
		GameRegistry.registerBlock(bambooStalk, "grc.bambooStalk");
		GameRegistry.registerBlock(bambooLeaves, "grc.bambooLeaves");
		GameRegistry.registerBlock(bambooFence, "grc.bambooFence");
		GameRegistry.registerBlock(bambooWall, "grc.bambooWall");
		GameRegistry.registerBlock(bambooStairs, "grc.bambooStairs");
		GameRegistry.registerBlock(bambooSingleSlab, ItemBambooSlab.class, "grc.bambooSingleSlab");
		GameRegistry.registerBlock(bambooDoubleSlab, ItemBambooSlab.class, "grc.bambooDoubleSlab");
		GameRegistry.registerBlock(bambooDoor, "grc.bambooDoor");
		GameRegistry.registerBlock(bambooFenceGate, "grc.bambooFenceGate");
		GameRegistry.registerBlock(bambooScaffold, "grc.bambooScaffold");

		GameRegistry.registerItem(bamboo, "grc.bamboo");
		GameRegistry.registerItem(bambooDoorItem, "grc.bambooDoorItem");
		GameRegistry.registerItem(bambooRaft, "grc.bambooRaft");
		GameRegistry.registerItem(bambooCoal, "grc.bambooCoal");
		GameRegistry.registerItem(bambooShootFood, "grc.bambooShootFood");

		if (bambooGenerateBiome)
		{
			//GameRegistry.addBiome(bambooBiome);
			BiomeManager.addSpawnBiome(bambooBiome);
			BiomeDictionary.registerBiomeType(bambooBiome, Type.FOREST);
		}

		GameRegistry.registerWorldGenerator(new WorldGeneratorBamboo(), 0);

		EntityRegistry.registerModEntity(EntityBambooRaft.class, "bambooRaft", 1, this, 80, 3, true);

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(bambooBlock, 5, 20);
		Blocks.fire.setFireInfo(bambooStalk, 5, 4);
		Blocks.fire.setFireInfo(bambooLeaves, 30, 60);
		Blocks.fire.setFireInfo(bambooFence, 5, 20);
		Blocks.fire.setFireInfo(bambooWall, 5, 20);
		Blocks.fire.setFireInfo(bambooStairs, 5, 20);
		Blocks.fire.setFireInfo(bambooSingleSlab, 5, 20);
		Blocks.fire.setFireInfo(bambooDoubleSlab, 5, 20);
		Blocks.fire.setFireInfo(bambooScaffold, 5, 20);

		//====================
		// CRAFTING
		//====================
		List recipes = CraftingManager.getInstance().getRecipeList();
		addRecipe(recipes, new ItemStack(bambooWall, 6), new Object [] {"###", "###", '#', bambooBlock});
		addRecipe(recipes, new ItemStack(bambooStairs, 4), new Object[] {"#  ", "## ", "###", '#', bambooBlock});
		addRecipe(recipes, new ItemStack(bambooSingleSlab, 6), new Object[] {"###", '#', bambooBlock});
		addRecipe(recipes, new ItemStack(bambooDoorItem, 1), new Object[] {"##", "##", "##", '#', bambooBlock});
		addRecipe(recipes, new ItemStack(bambooRaft, 1), new Object[] {"A A", "AAA", 'A', bambooBlock});
		addRecipe(recipes, new ItemStack(bambooBlock, 1), new Object[] {"A", "A", 'A', bambooSingleSlab});
		addRecipe(recipes, new ItemStack(bambooBlock, 1), "AA", "AA", 'A', bamboo);
		addRecipe(recipes, new ItemStack(bambooFence, 3), "AAA", "AAA", 'A', bamboo);
		addRecipe(recipes, new ItemStack(bambooFenceGate,1), "ABA", "ABA", 'A', bamboo, 'B', bambooBlock);
		addRecipe(recipes, new ItemStack(bambooScaffold, 16), "BBB", " A ", "A A", 'A', bamboo, 'B', bambooBlock);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 2), new Object[] {"A", "B", 'A', bambooCoal, 'B', "stickWood"}));

		try
		{
			MapGenStructureIO.func_143031_a(ComponentVillageBambooYard.class, "grc.bambooyard");
		}
		catch (Throwable e) {}

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("stickWood", bamboo);
		OreDictionary.registerOre("woodStick", bamboo);
		OreDictionary.registerOre("plankWood", bambooBlock);
		OreDictionary.registerOre("woodPlank", bambooBlock);
		OreDictionary.registerOre("slabWood", bambooSingleSlab);
		OreDictionary.registerOre("woodSlab", bambooSingleSlab);
		OreDictionary.registerOre("stairWood", bambooStairs);
		OreDictionary.registerOre("woodStair", bambooStairs);
		OreDictionary.registerOre("leavesTree", bambooLeaves);
		OreDictionary.registerOre("treeLeaves", bambooLeaves);
		//OreDictionary.registerOre("cropBamboo", bamboo);
		//OreDictionary.registerOre("materialBamboo", bamboo);
		OreDictionary.registerOre("bamboo", bamboo);
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("cropBambooshoot", bambooShoot);
		OreDictionary.registerOre("listAllveggie", bambooShoot);
		OreDictionary.registerOre("cropBambooshoot", bambooShootFood);
		OreDictionary.registerOre("listAllveggie", bambooShootFood);

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new BambooFuelHandler());
		GameRegistry.addSmelting(bamboo, new ItemStack(bambooCoal), 0.075f);
	}

	public void addRecipe(List recipeList, ItemStack itemstack, Object... objArray)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (objArray[i] instanceof String[])
		{
			String[] astring = ((String[]) objArray[i++]);

			for (int l = 0; l < astring.length; ++l)
			{
				String s1 = astring[l];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		else
		{
			while (objArray[i] instanceof String)
			{
				String s2 = (String) objArray[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap hashmap;

		for (hashmap = new HashMap(); i < objArray.length; i += 2)
		{
			Character character = (Character) objArray[i];
			ItemStack itemstack1 = null;

			if (objArray[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item) objArray[i + 1]);
			}
			else if (objArray[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block) objArray[i + 1], 1, 32767);
			}
			else if (objArray[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack) objArray[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1)
		{
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0)))
			{
				aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}

		ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, itemstack);
		recipeList.add(0, shapedrecipes);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();
		VillageHandlerBamboo handler = new VillageHandlerBamboo();
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BonemealEventBamboo());

		/*String modid;

		modid = "Forestry";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				if (RecipeManagers.fermenterManager != null)
				{
					addFermenterRecipe(new ItemStack(bambooShoot), 250, "biomass");
					addFermenterRecipe(new ItemStack(bambooShootFood), 250, "biomass");
					addFermenterRecipe(new ItemStack(bamboo), 100, "biomass");
				}

				if (BackpackManager.backpackItems[2] != null)
				{
					BackpackManager.backpackItems[2].add(new ItemStack(bambooShoot));
					BackpackManager.backpackItems[2].add(new ItemStack(bambooShootFood));
					BackpackManager.backpackItems[2].add(new ItemStack(bamboo));
					BackpackManager.backpackItems[2].add(new ItemStack(bambooLeaves));
				}

				FMLLog.info("[Growthcraft|Bamboo] Successfully integrated with Forestry.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Bamboo] Forestry not found. No integration made.", new Object[0]);
			}
		}

		modid = "Thaumcraft";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(bambooBlock.blockID, -1, new AspectList().add(Aspect.TREE, 1));
				ThaumcraftApi.registerObjectTag(bambooShoot.blockID, -1, new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1).add(Aspect.SEED, 1));
				ThaumcraftApi.registerObjectTag(bambooLeaves.blockID, -1, new AspectList().add(Aspect.PLANT, 1));
				ThaumcraftApi.registerObjectTag(bambooFence.blockID, -1, new AspectList().add(Aspect.TREE, 1));
				ThaumcraftApi.registerObjectTag(bambooWall.blockID, -1, new AspectList().add(Aspect.TREE, 1));
				ThaumcraftApi.registerObjectTag(bambooStairs.blockID, -1, new AspectList().add(Aspect.TREE, 1));
				ThaumcraftApi.registerObjectTag(bambooSingleSlab.blockID, -1, new AspectList().add(Aspect.TREE, 1));

				ThaumcraftApi.registerObjectTag(bamboo.itemID, -1, new AspectList().add(Aspect.TREE, 1));
				ThaumcraftApi.registerObjectTag(bambooDoorItem.itemID, -1, new AspectList().add(Aspect.TREE, 1));
				ThaumcraftApi.registerObjectTag(bambooRaft.itemID, -1, new AspectList().add(Aspect.WATER, 4).add(Aspect.TRAVEL, 4));
				ThaumcraftApi.registerObjectTag(bambooShootFood.itemID, -1, new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1).add(Aspect.SEED, 1));

				FMLLog.info("[Growthcraft|Bamboo] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Bamboo] Thaumcraft not found. No integration made.", new Object[0]);
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
