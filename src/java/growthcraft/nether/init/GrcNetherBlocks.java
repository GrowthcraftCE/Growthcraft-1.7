package growthcraft.nether.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.event.PlayerInteractEventPaddy;
import growthcraft.core.integration.NEI;
import growthcraft.nether.common.block.BlockNetherBaalsRot;
import growthcraft.nether.common.block.BlockNetherCinderrot;
import growthcraft.nether.common.block.BlockNetherFireLily;
import growthcraft.nether.common.block.BlockNetherKnifeBush;
import growthcraft.nether.common.block.BlockNetherMaliceFruit;
import growthcraft.nether.common.block.BlockNetherMaliceLeaves;
import growthcraft.nether.common.block.BlockNetherMaliceLog;
import growthcraft.nether.common.block.BlockNetherMalicePlanks;
import growthcraft.nether.common.block.BlockNetherMaliceSapling;
import growthcraft.nether.common.block.BlockNetherMaraLotus;
import growthcraft.nether.common.block.BlockNetherMuertecap;
import growthcraft.nether.common.block.BlockNetherPaddy;
import growthcraft.nether.common.block.BlockNetherPepper;
import growthcraft.nether.common.block.BlockNetherSquash;
import growthcraft.nether.common.block.BlockNetherSquashStem;
import growthcraft.nether.common.item.ItemNetherLilyPad;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

public class GrcNetherBlocks
{
	public BlockDefinition netherBaalsRot;
	public BlockDefinition netherCinderrot;
	public BlockDefinition netherFireLily;
	public BlockDefinition netherKnifeBush;
	public BlockDefinition netherMaliceFruit;
	public BlockDefinition netherMaliceLeaves;
	public BlockDefinition netherMaliceLog;
	public BlockDefinition netherMalicePlanks;
	public BlockDefinition netherMaliceSapling;
	public BlockDefinition netherMaraLotus;
	public BlockDefinition netherMuertecap;
	public BlockDefinition netherPaddyField;
	public BlockDefinition netherPaddyFieldFilled;
	public BlockDefinition netherPepper;
	public BlockDefinition netherSquash;
	public BlockDefinition netherSquashStem;

	public GrcNetherBlocks() {}

	public void preInit()
	{
		this.netherBaalsRot = new BlockDefinition(new BlockNetherBaalsRot());
		this.netherCinderrot = new BlockDefinition(new BlockNetherCinderrot());
		this.netherFireLily = new BlockDefinition(new BlockNetherFireLily());
		this.netherKnifeBush = new BlockDefinition(new BlockNetherKnifeBush());
		this.netherMaliceFruit = new BlockDefinition(new BlockNetherMaliceFruit());
		this.netherMaliceLeaves = new BlockDefinition(new BlockNetherMaliceLeaves());
		this.netherMaliceLog = new BlockDefinition(new BlockNetherMaliceLog());
		this.netherMalicePlanks = new BlockDefinition(new BlockNetherMalicePlanks());
		this.netherMaliceSapling = new BlockDefinition(new BlockNetherMaliceSapling());
		this.netherMaraLotus = new BlockDefinition(new BlockNetherMaraLotus());
		this.netherMuertecap = new BlockDefinition(new BlockNetherMuertecap());
		this.netherPaddyField = new BlockDefinition(new BlockNetherPaddy(false));
		this.netherPaddyFieldFilled = new BlockDefinition(new BlockNetherPaddy(true));
		this.netherPepper = new BlockDefinition(new BlockNetherPepper());
		this.netherSquash = new BlockDefinition(new BlockNetherSquash());
		this.netherSquashStem = new BlockDefinition(new BlockNetherSquashStem(netherSquash.getBlock()));
	}

	public void init()
	{
		register();

		PlayerInteractEventPaddy.paddyBlocks.put(Blocks.soul_sand, netherPaddyField.getBlock());

		hideItems();
	}

	private void register()
	{
		GameRegistry.registerBlock(netherBaalsRot.getBlock(), "grcnether.netherBaalsRot");
		GameRegistry.registerBlock(netherCinderrot.getBlock(), "grcnether.netherCinderrot");
		GameRegistry.registerBlock(netherFireLily.getBlock(), ItemNetherLilyPad.class, "grcnether.netherFireLily");
		GameRegistry.registerBlock(netherKnifeBush.getBlock(), "grcnether.netherKnifeBush");
		GameRegistry.registerBlock(netherMaliceFruit.getBlock(), "grcnether.netherMaliceFruit");
		GameRegistry.registerBlock(netherMaliceLeaves.getBlock(), "grcnether.netherMaliceLeaves");
		GameRegistry.registerBlock(netherMaliceLog.getBlock(), "grcnether.netherMaliceLog");
		GameRegistry.registerBlock(netherMalicePlanks.getBlock(), "grcnether.netherMalicePlanks");
		GameRegistry.registerBlock(netherMaliceSapling.getBlock(), "grcnether.netherMaliceSapling");
		GameRegistry.registerBlock(netherMaraLotus.getBlock(), ItemNetherLilyPad.class, "grcnether.netherMaraLotus");
		GameRegistry.registerBlock(netherMuertecap.getBlock(), "grcnether.netherMuertecap");
		GameRegistry.registerBlock(netherPaddyField.getBlock(), "grcnether.netherPaddyField");
		GameRegistry.registerBlock(netherPaddyFieldFilled.getBlock(), "grcnether.netherPaddyFieldFilled");
		GameRegistry.registerBlock(netherPepper.getBlock(), "grcnether.netherPepperBlock");
		GameRegistry.registerBlock(netherSquash.getBlock(), "grcnether.netherSquash");
		GameRegistry.registerBlock(netherSquashStem.getBlock(), "grcnether.netherSquashStem");

		OreDictionary.registerOre("plankMaliceWood", netherMalicePlanks.getBlock());
	}

	private void hideItems()
	{
		NEI.hideItem(netherMuertecap.asStack());
		NEI.hideItem(netherMaliceFruit.asStack());
		NEI.hideItem(netherPaddyField.asStack());
		NEI.hideItem(netherPaddyFieldFilled.asStack());
		NEI.hideItem(netherPepper.asStack());
		NEI.hideItem(netherSquashStem.asStack());
	}
}
