package growthcraft.core.integration;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import squeek.applecore.api.AppleCoreAPI;

/*
 * API abstraction layer this will handle AppleCore methods if AppleCore is present
 * or does some default behaviour
 */
public class AppleCore
{
	// abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
	@Optional.Method(modid = "AppleCore")
	private static Event.Result validateGrowthTick_AC(Block block, World world, int x, int y, int z, Random random)
	{
		return AppleCoreAPI.dispatcher.validatePlantGrowth(block, world, x, y, z, random);
	}

	public static Event.Result validateGrowthTick(Block block, World world, int x, int y, int z, Random random)
	{
		if (Loader.isModLoaded("AppleCore"))
			return validateGrowthTick_AC(block, world, x, y, z, random);

		return Event.Result.DEFAULT;
	}

	// abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
	@Optional.Method(modid = "AppleCore")
	private static void announceGrowthTick_AC(Block block, World world, int x, int y, int z, int previousMetadata)
	{
		AppleCoreAPI.dispatcher.announcePlantGrowth(block, world, x, y, z, previousMetadata);
	}

	public static void announceGrowthTick(Block block, World world, int x, int y, int z, int previousMetadata)
	{
		if (Loader.isModLoaded("AppleCore"))
			announceGrowthTick_AC(block, world, x, y, z, previousMetadata);
	}
}
