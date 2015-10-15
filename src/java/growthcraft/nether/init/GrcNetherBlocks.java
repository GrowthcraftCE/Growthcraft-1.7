package growthcraft.nether.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.event.PlayerInteractEventPaddy;
import growthcraft.core.integration.NEI;
import growthcraft.nether.common.block.BlockNetherPaddy;
import growthcraft.nether.common.block.BlockNetherPepper;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;

public class GrcNetherBlocks
{
	public BlockDefinition netherPepper;
	public BlockDefinition netherPaddyField;

	public GrcNetherBlocks() {}

	public void preInit()
	{
		this.netherPepper = new BlockDefinition(new BlockNetherPepper());
		this.netherPaddyField = new BlockDefinition(new BlockNetherPaddy());

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

		NEI.hideItem(netherPepper.asStack());
		NEI.hideItem(netherPaddyField.asStack());
	}
}
