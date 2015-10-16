package growthcraft.nether.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.event.PlayerInteractEventPaddy;
import growthcraft.core.integration.NEI;
import growthcraft.nether.common.block.BlockNetherPaddy;
import growthcraft.nether.common.block.BlockNetherPepper;
import growthcraft.nether.common.block.BlockNetherSquash;
import growthcraft.nether.common.block.BlockNetherSquashStem;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;

public class GrcNetherBlocks
{
	public BlockDefinition netherPepper;
	public BlockDefinition netherPaddyField;
	public BlockDefinition netherPaddyFieldFilled;
	public BlockDefinition netherSquash;
	public BlockDefinition netherSquashStem;

	public GrcNetherBlocks() {}

	public void preInit()
	{
		this.netherPepper = new BlockDefinition(new BlockNetherPepper());
		this.netherPaddyField = new BlockDefinition(new BlockNetherPaddy(false));
		this.netherPaddyFieldFilled = new BlockDefinition(new BlockNetherPaddy(true));
		this.netherSquash = new BlockDefinition(new BlockNetherSquash());
		this.netherSquashStem = new BlockDefinition(new BlockNetherSquashStem(netherSquash.getBlock()));

		register();
	}

	public void init()
	{
		PlayerInteractEventPaddy.paddyBlocks.put(Blocks.soul_sand, netherPaddyField.getBlock());
	}

	public void register()
	{
		GameRegistry.registerBlock(netherPepper.getBlock(), "grcnether.netherPepperBlock");
		GameRegistry.registerBlock(netherPaddyField.getBlock(), "grcnether.netherPaddyField");
		GameRegistry.registerBlock(netherPaddyFieldFilled.getBlock(), "grcnether.netherPaddyFieldFilled");
		GameRegistry.registerBlock(netherSquash.getBlock(), "grcnether.netherSquash");
		GameRegistry.registerBlock(netherSquashStem.getBlock(), "grcnether.netherSquashStem");

		NEI.hideItem(netherPepper.asStack());
		NEI.hideItem(netherPaddyField.asStack());
		NEI.hideItem(netherPaddyFieldFilled.asStack());
		NEI.hideItem(netherSquashStem.asStack());
	}
}
