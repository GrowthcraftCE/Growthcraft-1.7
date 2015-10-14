package growthcraft.nether.init;

import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.nether.common.item.ItemEctoplasm;
import growthcraft.nether.common.item.ItemNetherPepper;
import growthcraft.nether.common.item.ItemNetherRashSpores;

import cpw.mods.fml.common.registry.GameRegistry;

public class GrcNetherItems
{
	public ItemDefinition ectoplasm;
	public ItemDefinition netherPepper;
	public ItemDefinition netherRashSpores;

	public GrcNetherItems() {}

	public void init()
	{
		this.ectoplasm = new ItemDefinition(new ItemEctoplasm());
		this.netherPepper = new ItemDefinition(new ItemNetherPepper());
		this.netherRashSpores = new ItemDefinition(new ItemNetherRashSpores());

		register();
	}

	public void register()
	{
		GameRegistry.registerItem(ectoplasm.getItem(), "grcnether.ectoplasm");
		GameRegistry.registerItem(netherPepper.getItem(), "grcnether.netherPepper");
		GameRegistry.registerItem(netherRashSpores.getItem(), "grcnether.netherRashSpores");
	}
}
