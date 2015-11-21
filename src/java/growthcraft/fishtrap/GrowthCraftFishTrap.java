package growthcraft.fishtrap;

import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.FishTrapRegistry;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.ModuleContainer;
import growthcraft.fishtrap.client.gui.GuiHandlerFishTrap;
import growthcraft.fishtrap.common.block.BlockFishTrap;
import growthcraft.fishtrap.common.CommonProxy;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftFishTrap.MOD_ID,
	name = GrowthCraftFishTrap.MOD_NAME,
	version = GrowthCraftFishTrap.MOD_VERSION,
	dependencies = "required-after:Growthcraft"
)
public class GrowthCraftFishTrap
{
	public static final String MOD_ID = "Growthcraft|Fishtrap";
	public static final String MOD_NAME = "Growthcraft Fishtrap";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftFishTrap instance;

	public static BlockDefinition fishTrap;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcFishtrapConfig config = new GrcFishtrapConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcFishtrapConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/fishtrap.conf");

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.fishtrap.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		//====================
		// INIT
		//====================
		fishTrap = new BlockDefinition(new BlockFishTrap());

		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fishTrap.getBlock(), "grc.fishTrap");

		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "grc.tileentity.fishTrap");

		// Will use same chances as Fishing Rods
		//JUNK
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.leather_boots), 10).setDamage(0.9F));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.leather), 10));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.bone), 10));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.potionitem), 10));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.string), 5));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.fishing_rod), 2).setDamage(0.9F));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.bowl), 10));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.stick), 5));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.dye, 10), 1));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Blocks.tripwire_hook), 10));
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(new ItemStack(Items.rotten_flesh), 10));
		//TREASURE
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Blocks.waterlily), 1));
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Items.name_tag), 1));
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Items.saddle), 1));
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Items.bow), 1).setDamage(0.25F).setEnchantable());
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Items.fishing_rod), 1).setDamage(0.25F).setEnchantable());
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Items.book), 1).setEnchantable());
		//FISH
		FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 60));
		FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25));
		FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2));
		FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13));

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fishTrap.asStack(1), "ACA", "CBC", "ACA", 'A', "plankWood", 'B', Items.lead, 'C', Items.string));

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerFishTrap());

		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
