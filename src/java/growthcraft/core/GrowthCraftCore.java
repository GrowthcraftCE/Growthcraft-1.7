package growthcraft.core;

import growthcraft.core.common.AchievementPageGrowthcraft;
import growthcraft.core.common.block.BlockFenceRope;
import growthcraft.core.common.block.BlockRope;
import growthcraft.core.common.CommonProxy;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.item.ItemRope;
import growthcraft.core.common.ModuleContainer;
import growthcraft.core.creativetab.CreativeTabsGrowthcraft;
import growthcraft.core.event.HarvestDropsEventCore;
import growthcraft.core.event.PlayerInteractEventAmazingStick;
import growthcraft.core.event.PlayerInteractEventPaddy;
import growthcraft.core.event.TextureStitchEventCore;
import growthcraft.core.handler.BucketHandler;
import growthcraft.core.integration.NEI;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
	modid = GrowthCraftCore.MOD_ID,
	name = GrowthCraftCore.MOD_NAME,
	version = GrowthCraftCore.MOD_VERSION
)
public class GrowthCraftCore
{
	public static final String MOD_ID = "Growthcraft";
	public static final String MOD_NAME = "Growthcraft";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftCore instance;

	@SideOnly(Side.CLIENT)
	public static IIcon liquidSmoothTexture;
	@SideOnly(Side.CLIENT)
	public static IIcon liquidBlobsTexture;

	public static CreativeTabs tab;

	public static BlockDefinition fenceRope;
	public static BlockDefinition ropeBlock;
	public static ItemDefinition rope;

	private GrcCoreConfig config = new GrcCoreConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcCoreConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/core.conf");

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.core.integration.ThaumcraftModule());
		if (config.enableWailaIntegration) modules.add(new growthcraft.core.integration.Waila());
		if (config.enableAppleCoreIntegration) modules.add(new growthcraft.core.integration.AppleCore());

		tab =  new CreativeTabsGrowthcraft("tabGrowthCraft");

		//====================
		// INIT
		//====================
		fenceRope = new BlockDefinition(new BlockFenceRope());
		ropeBlock = new BlockDefinition(new BlockRope());
		rope = new ItemDefinition(new ItemRope());

		register();
		modules.preInit();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fenceRope.getBlock(), "grc.fenceRope");
		GameRegistry.registerBlock(ropeBlock.getBlock(), "grc.ropeBlock");

		GameRegistry.registerItem(rope.getItem(), "grc.rope");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(fenceRope.getBlock(), 5, 20);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(rope.asStack(8), new Object[] {"A", 'A', Items.lead});


		OreDictionary.registerOre("materialRope", rope.getItem());

		NEI.hideItem(fenceRope.asStack());
		NEI.hideItem(ropeBlock.asStack());

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventCore());
		BucketHandler.init();

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();
		AchievementPageGrowthcraft.init();

		ItemUtils.init();

		modules.init();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new HarvestDropsEventCore());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventPaddy());
		if (config.useAmazingStick)
		{
			MinecraftForge.EVENT_BUS.register(new PlayerInteractEventAmazingStick());
		}

		modules.postInit();
	}
}
