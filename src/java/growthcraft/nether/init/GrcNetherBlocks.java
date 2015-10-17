package growthcraft.nether.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.event.PlayerInteractEventPaddy;
import growthcraft.core.integration.NEI;
import growthcraft.nether.common.block.BlockNetherPaddy;
import growthcraft.nether.common.block.BlockNetherPepper;
import growthcraft.nether.common.block.BlockNetherSquash;
import growthcraft.nether.common.block.BlockNetherSquashStem;
import growthcraft.nether.common.block.BlockNetherMaliceSapling;
import growthcraft.nether.common.block.BlockNetherMaliceLeaves;
import growthcraft.nether.common.block.BlockNetherMalicePlanks;
import growthcraft.nether.common.block.BlockNetherMaliceLog;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

public class GrcNetherBlocks
{
	public BlockDefinition netherPepper;
	public BlockDefinition netherPaddyField;
	public BlockDefinition netherPaddyFieldFilled;
	public BlockDefinition netherSquash;
	public BlockDefinition netherSquashStem;
	public BlockDefinition netherMaliceSapling;
	public BlockDefinition netherMaliceLeaves;
	public BlockDefinition netherMalicePlanks;
	public BlockDefinition netherMaliceLog;

	public GrcNetherBlocks() {}

	public void preInit()
	{
		this.netherPepper = new BlockDefinition(new BlockNetherPepper());
		this.netherPaddyField = new BlockDefinition(new BlockNetherPaddy(false));
		this.netherPaddyFieldFilled = new BlockDefinition(new BlockNetherPaddy(true));
		this.netherSquash = new BlockDefinition(new BlockNetherSquash());
		this.netherSquashStem = new BlockDefinition(new BlockNetherSquashStem(netherSquash.getBlock()));
		this.netherMaliceSapling = new BlockDefinition(new BlockNetherMaliceSapling());
		this.netherMaliceLeaves = new BlockDefinition(new BlockNetherMaliceLeaves());
		this.netherMalicePlanks = new BlockDefinition(new BlockNetherMalicePlanks());
		this.netherMaliceLog = new BlockDefinition(new BlockNetherMaliceLog());
	}

	public void init()
	{
		register();
		PlayerInteractEventPaddy.paddyBlocks.put(Blocks.soul_sand, netherPaddyField.getBlock());
	}

	public void register()
	{
		GameRegistry.registerBlock(netherPepper.getBlock(), "grcnether.netherPepperBlock");
		GameRegistry.registerBlock(netherPaddyField.getBlock(), "grcnether.netherPaddyField");
		GameRegistry.registerBlock(netherPaddyFieldFilled.getBlock(), "grcnether.netherPaddyFieldFilled");
		GameRegistry.registerBlock(netherSquash.getBlock(), "grcnether.netherSquash");
		GameRegistry.registerBlock(netherSquashStem.getBlock(), "grcnether.netherSquashStem");
		GameRegistry.registerBlock(netherMaliceSapling.getBlock(), "grcnether.netherMaliceSapling");
		GameRegistry.registerBlock(netherMaliceLeaves.getBlock(), "grcnether.netherMaliceLeaves");
		GameRegistry.registerBlock(netherMalicePlanks.getBlock(), "grcnether.netherMalicePlanks");
		GameRegistry.registerBlock(netherMaliceLog.getBlock(), "grcnether.netherMaliceLog");

		OreDictionary.registerOre("plankMaliceWood", netherMalicePlanks.getBlock());

		NEI.hideItem(netherPepper.asStack());
		NEI.hideItem(netherPaddyField.asStack());
		NEI.hideItem(netherPaddyFieldFilled.asStack());
		NEI.hideItem(netherSquashStem.asStack());
	}
}
