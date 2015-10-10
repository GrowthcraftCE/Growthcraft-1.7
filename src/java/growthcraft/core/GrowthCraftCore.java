package growthcraft.core;

import growthcraft.core.block.BlockFenceRope;
import growthcraft.core.block.BlockRope;
import growthcraft.core.event.HarvestDropsEventCore;
import growthcraft.core.event.PlayerInteractEventAmazingStick;
import growthcraft.core.event.TextureStitchEventCore;
import growthcraft.core.handler.BucketHandler;
import growthcraft.core.integration.NEI;
import growthcraft.core.item.ItemRope;
import growthcraft.core.network.CommonProxy;

import org.apache.logging.log4j.Level;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = GrowthCraftCore.MOD_ID,name = GrowthCraftCore.MOD_NAME,version = GrowthCraftCore.MOD_VERSION)
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

	@SidedProxy(clientSide="growthcraft.core.network.ClientProxy", serverSide="growthcraft.core.network.CommonProxy")
	public static CommonProxy proxy;

	public static Block fenceRope;
	public static Block ropeBlock;
	public static Item rope;

	public static AchievementPage chievPage;

	private growthcraft.core.Config config;

	public static growthcraft.core.Config getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config = new growthcraft.core.Config();
		config.load(event.getModConfigurationDirectory(), "growthcraft/core.conf");

		tab =  new CreativeTabGrowthcraft("tabGrowthCraft");

		//====================
		// INIT
		//====================
		fenceRope = (new BlockFenceRope());
		ropeBlock = (new BlockRope());

		rope = (new ItemRope());

		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fenceRope, "grc.fenceRope");
		GameRegistry.registerBlock(ropeBlock, "grc.ropeBlock");

		GameRegistry.registerItem(rope, "grc.rope");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(fenceRope, 5, 20);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ItemStack(rope, 8), new Object[] {"A", 'A', Items.lead});

		NEI.hideItem(new ItemStack(fenceRope));
		NEI.hideItem(new ItemStack(ropeBlock));

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventCore());
		BucketHandler.init();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.initRenders();
		AchievementPageGrowthcraft.init(chievPage);

		new growthcraft.core.integration.Waila();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new HarvestDropsEventCore());
		if (config.useAmazingStick)
		{
			FMLLog.log(MOD_ID, Level.INFO, "Enabling the amazing stick event");
			MinecraftForge.EVENT_BUS.register(new PlayerInteractEventAmazingStick());
		}
		else
		{
			FMLLog.log(MOD_ID, Level.INFO, "No amazing stick event");
		}
		//		MinecraftForge.EVENT_BUS.register(new PlayerSleepInBed());

		/*String modid;

		modid = "Thaumcraft";
		if (Loader.isModLoaded(modid))
		{
			try
			{
				ThaumcraftApi.registerObjectTag(rope.itemID, -1, new AspectList().add(Aspect.BEAST, 1).add(Aspect.CLOTH, 1));

				FMLLog.info("[Growthcraft|Core] Successfully integrated with Thaumcraft.", new Object[0]);
			}
			catch (Exception e)
			{
				FMLLog.info("[Growthcraft|Core] Thaumcraft not found. No integration made.", new Object[0]);
			}
		}*/
	}
}
