package growthcraft.nether.init;

import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.nether.common.item.ItemEctoplasm;
import growthcraft.nether.common.item.ItemNetherMaliceFruit;
import growthcraft.nether.common.item.ItemNetherMuertecap;
import growthcraft.nether.common.item.ItemNetherPepper;
import growthcraft.nether.common.item.ItemNetherRashSpores;
import growthcraft.nether.common.item.ItemNetherSquashSeeds;

import cpw.mods.fml.common.registry.GameRegistry;

public class GrcNetherItems
{
	public ItemDefinition ectoplasm;
	public ItemDefinition netherMaliceFruit;
	public ItemDefinition netherMuertecap;
	public ItemDefinition netherPepper;
	public ItemDefinition netherRashSpores;
	public ItemDefinition netherSquashSeeds;

	public GrcNetherItems() {}

	public void preInit()
	{
		this.ectoplasm = new ItemDefinition(new ItemEctoplasm());
		this.netherMuertecap = new ItemDefinition(new ItemNetherMuertecap());
		this.netherPepper = new ItemDefinition(new ItemNetherPepper());
		this.netherRashSpores = new ItemDefinition(new ItemNetherRashSpores());
		this.netherSquashSeeds = new ItemDefinition(new ItemNetherSquashSeeds());
		this.netherMaliceFruit = new ItemDefinition(new ItemNetherMaliceFruit());
	}

	public void init()
	{
		register();
	}

	public void register()
	{
		GameRegistry.registerItem(ectoplasm.getItem(), "grcnether.ectoplasm");
		GameRegistry.registerItem(netherMuertecap.getItem(), "grcnether.netherMuertecapFood");
		GameRegistry.registerItem(netherPepper.getItem(), "grcnether.netherPepper");
		GameRegistry.registerItem(netherRashSpores.getItem(), "grcnether.netherRashSpores");
		GameRegistry.registerItem(netherSquashSeeds.getItem(), "grcnether.netherSquashSeeds");
		GameRegistry.registerItem(netherMaliceFruit.getItem(), "grcnether.netherMaliceFruitItem");
	}
}
