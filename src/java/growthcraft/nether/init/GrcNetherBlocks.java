package growthcraft.nether.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.integration.NEI;
import growthcraft.nether.common.block.BlockNetherPepper;

import cpw.mods.fml.common.registry.GameRegistry;

public class GrcNetherBlocks
{
	public BlockDefinition netherPepper;

	public GrcNetherBlocks() {}

	public void init()
	{
		this.netherPepper = new BlockDefinition(new BlockNetherPepper());

		register();
	}

	public void register()
	{
		GameRegistry.registerBlock(netherPepper.getBlock(), "grcnether.netherPepperBlock");

		NEI.hideItem(netherPepper.asStack());
	}
}
