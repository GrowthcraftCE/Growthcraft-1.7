package growthcraft.core.integration;

import java.util.Random;

import growthcraft.core.GrowthCraftCore;
import squeek.applecore.api.AppleCoreAPI;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class AppleCore extends ModIntegrationBase
{
	public static final String MOD_ID = "AppleCore";
	private static boolean appleCoreLoaded;

	public AppleCore()
	{
		super(GrowthCraftCore.MOD_ID, MOD_ID);
	}

	@Override
	public void doInit()
	{
		appleCoreLoaded = Loader.isModLoaded(modID);
	}

	// abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
	@Optional.Method(modid = MOD_ID)
	private static Event.Result validateGrowthTick_AC(Block block, World world, int x, int y, int z, Random random)
	{
		return AppleCoreAPI.dispatcher.validatePlantGrowth(block, world, x, y, z, random);
	}

	public static Event.Result validateGrowthTick(Block block, World world, int x, int y, int z, Random random)
	{
		if (appleCoreLoaded)
			return validateGrowthTick_AC(block, world, x, y, z, random);

		return Event.Result.DEFAULT;
	}

	// abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
	@Optional.Method(modid = MOD_ID)
	private static void announceGrowthTick_AC(Block block, World world, int x, int y, int z, int previousMetadata)
	{
		// 1.2.x - until there is an official 1.2.x release of AppleCore
		//AppleCoreAPI.dispatcher.announcePlantGrowth(block, world, x, y, z, previousMetadata);

		// 1.1.x
		AppleCoreAPI.dispatcher.announcePlantGrowth(block, world, x, y, z);
	}

	public static void announceGrowthTick(Block block, World world, int x, int y, int z, int previousMetadata)
	{
		if (appleCoreLoaded)
			announceGrowthTick_AC(block, world, x, y, z, previousMetadata);
	}
}
