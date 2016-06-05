/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.init;

import java.util.HashMap;
import java.util.Map;

import growthcraft.core.common.block.BlockFenceRope;
import growthcraft.core.common.block.BlockRope;
import growthcraft.core.common.block.BlockSaltBlock;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.common.item.ItemBlockFenceRope;
import growthcraft.core.common.item.ItemBlockNaturaFenceRope;
import growthcraft.core.integration.minecraft.EnumMinecraftWoodType;
import growthcraft.core.integration.NEI;
import growthcraft.core.registry.FenceRopeRegistry;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

public class GrcCoreBlocks extends GrcModuleBase
{
	public BlockDefinition ropeBlock;
	public BlockDefinition saltBlock;
	public BlockDefinition fenceRope;
	public BlockDefinition netherBrickFenceRope;
	public BlockDefinition naturaFenceRope;
	public Map<EnumMinecraftWoodType, BlockDefinition> etfuturumFenceRopes = new HashMap<EnumMinecraftWoodType, BlockDefinition>();
	public Map<String, BlockDefinition> woodstuffFenceRopes = new HashMap<String, BlockDefinition>();

	@Override
	public void preInit()
	{
		this.saltBlock = new BlockDefinition(new BlockSaltBlock());
		this.ropeBlock = new BlockDefinition(new BlockRope());
		this.fenceRope = new BlockDefinition(new BlockFenceRope(Blocks.fence, "grc.fenceRope"));
		this.netherBrickFenceRope = new BlockDefinition(new BlockFenceRope(Blocks.nether_brick_fence, "grc.netherBrickFenceRope"));

		FenceRopeRegistry.instance().addEntry(Blocks.fence, fenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.nether_brick_fence, netherBrickFenceRope.getBlock());
	}

	@Override
	public void register()
	{
		fenceRope.register("grc.fenceRope", ItemBlockFenceRope.class);
		ropeBlock.register("grc.ropeBlock");
		saltBlock.register("grccore.salt_block");
		netherBrickFenceRope.register("grc.netherBrickFenceRope", ItemBlockFenceRope.class);

		Blocks.fire.setFireInfo(fenceRope.getBlock(), 5, 20);
	}

	private void initEtfuturum()
	{
		final String modId = "etfuturum";
		if (Loader.isModLoaded(modId))
		{
			for (EnumMinecraftWoodType ty : EnumMinecraftWoodType.VALUES)
			{
				final Block block = GameRegistry.findBlock(modId, "fence_" + ty.name);
				if (block != null)
				{
					final String basename = "grc.etfuturum_fence_rope_" + ty.name;
					final BlockDefinition fp = new BlockDefinition(new BlockFenceRope(block, basename));
					fp.register(basename, ItemBlockFenceRope.class);
					Blocks.fire.setFireInfo(fp.getBlock(), 5, 20);
					FenceRopeRegistry.instance().addEntry(block, fp.getBlock());
					NEI.hideItem(fp.asStack());
				}
			}
		}
	}

	private void initWoodstuff()
	{
		final String modId = "woodstuff";
		if (Loader.isModLoaded(modId))
		{
			final String[] names = {
				// aether
				"skyrootPlank",
				// arsmagica2
				"planksWitchwood",
				// BiomesOPlenty
				"planks",
				// Botania
				"livingwood",
				"dreamwood",
				// dendrology
				"wood0",
				// enhancedbiomes
				"enhancedbiomes.tile.planksEB",
				// erebus
				"planks",
				"planks_scorched",
				// erebus
				"planks_varnished",
				// erebus
				"petrifiedWoodPlanks",
				// ExtrabiomesXL
				"planks",
				// Forestry
				"planks",
				// Highlands
				"hl_woodPlanks",
				// Natura
				"planks",
				// RidiculousWorld
				"RidiculousPlanks",
				// Thaumcraft
				"blockWoodenDevice",
				// totemic
				"redCedarPlank",
				// TwilightForest
				"tile.TFTowerStone",
				// witchery
				"witchwood",
			};
			// TODO
		}
	}

	private void initNatura()
	{
		final String modId = "Natura";

		if (Loader.isModLoaded(modId))
		{
			final Block block = GameRegistry.findBlock(modId, "Natura.fence");
			if (block != null)
			{
				this.naturaFenceRope = new BlockDefinition(new BlockFenceRope(block, "grc.naturaFenceRope"));
				naturaFenceRope.register("grc.naturaFenceRope", ItemBlockNaturaFenceRope.class);
				Blocks.fire.setFireInfo(naturaFenceRope.getBlock(), 5, 20);
				FenceRopeRegistry.instance().addEntry(block, naturaFenceRope.getBlock());
				NEI.hideItem(naturaFenceRope.asStack());
			}
		}
	}

	@Override
	public void init()
	{
		OreDictionary.registerOre("blockSalt", saltBlock.getItem());

		if (GrowthCraftCore.getConfig().enableEtfuturumIntegration) initEtfuturum();
		if (GrowthCraftCore.getConfig().enableWoodstuffIntegration) initWoodstuff();
		if (GrowthCraftCore.getConfig().enableNaturaIntegration) initNatura();
		NEI.hideItem(fenceRope.asStack());
		NEI.hideItem(netherBrickFenceRope.asStack());
		NEI.hideItem(ropeBlock.asStack());
	}
}
