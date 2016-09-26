/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.bees.integration;

import growthcraft.api.core.item.ItemKey;
import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.bees.common.block.BlockBeeBoxThaumcraft;
import growthcraft.bees.common.block.EnumBeeBoxThaumcraft;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.cellar.integration.ThaumcraftBoozeHelper;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.integration.thaumcraft.AspectsHelper;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftBees.MOD_ID);
	}

	@Override
	protected void doPreInit()
	{
		GrowthCraftBees.blocks.beeBoxThaumcraft = GrowthCraftBees.blocks.newTypedDefinition(new BlockBeeBoxThaumcraft());
		GrowthCraftBees.blocks.beeBoxThaumcraft.getBlock().setFlammability(20).setFireSpreadSpeed(5).setHarvestLevel("axe", 0);
	}

	@Override
	protected void doRegister()
	{
		if (GrowthCraftBees.blocks.beeBoxThaumcraft != null)
		{
			GameRegistry.registerBlock(GrowthCraftBees.blocks.beeBoxThaumcraft.getBlock(), ItemBlockBeeBox.class, "grc.BeeBox.Thaumcraft");
		}
	}

	@Override
	protected void doLateRegister()
	{
		if (GrowthCraftBees.blocks.beeBoxThaumcraft != null)
		{
			final Block blockWoodenDevice = GameRegistry.findBlock(modID, "blockWoodenDevice");
			if (blockWoodenDevice != null)
			{
				GameRegistry.addShapedRecipe(EnumBeeBoxThaumcraft.GREATWOOD.asStack(), " A ", "A A", "AAA", 'A', new ItemStack(blockWoodenDevice, 1, 6));
				GameRegistry.addShapedRecipe(EnumBeeBoxThaumcraft.SILVERWOOD.asStack(), " A ", "A A", "AAA", 'A', new ItemStack(blockWoodenDevice, 1, 7));
			}
			else
			{
				logger.warn("Could not find blockWoodenDevice from Thaumcraft, skipping recipes for Thaumcraft Bee Boxes");
			}
		}
	}

	@Override
	@Optional.Method(modid="Thaumcraft")
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.honeyCombEmpty.asStack(), new AspectList().add(Aspect.ORDER, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.honeyCombFilled.asStack(), new AspectList().add(Aspect.ORDER, 1).add(Aspect.SLIME, 1).add(Aspect.GREED, 1).add(Aspect.HUNGER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.honeyJar.asStack(), new AspectList().add(Aspect.SLIME, 1).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(Aspect.VOID, 1).add(Aspect.GREED, 3).add(Aspect.HUNGER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.bee.asStack(), new AspectList().add(Aspect.BEAST, 1).add(Aspect.AIR, 1).add(Aspect.FLIGHT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeHive.asStack(), new AspectList().add(Aspect.SLIME, 1).add(Aspect.BEAST, 1).add(Aspect.ORDER, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBox.asStack(), new int[]{0,1,2,3,4,5}, new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));

		{
			final AspectList[] common = new AspectList[]
			{
				new AspectList(),
				new AspectList().add(Aspect.HEAL, 1),
				new AspectList().add(Aspect.HEAL, 2),
				new AspectList().add(Aspect.HEAL, 1),
				new AspectList().add(Aspect.HEAL, 2),
				new AspectList().add(Aspect.HEAL, 1).add(Aspect.POISON, 1),
				new AspectList().add(Aspect.POISON, 1)
			};

			for (int i = 0; i < common.length; ++i)
			{
				final AspectList list = common[i];
				ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftBees.fluids.honeyMeadBottle.asStack(1, i), list.copy());
				ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftBees.fluids.honeyMeadBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL));
				ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftBees.fluids.honeyMeadFluids[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL));
			}
		}

		if (GrowthCraftBees.fluids.honey != null)
		{
			{
				final ItemStack bottleStack = GrowthCraftBees.fluids.honey.asBottleItemStack();
				if (bottleStack != null)
				{
					ThaumcraftApi.registerObjectTag(bottleStack, new AspectList().add(Aspect.ORDER, 1).add(Aspect.SLIME, 2).add(Aspect.GREED, 1).add(Aspect.HUNGER, 1));
				}
			}

			{
				final ItemStack bucketStack = GrowthCraftBees.fluids.honey.asBottleItemStack();
				if (bucketStack != null)
				{
					ThaumcraftApi.registerObjectTag(bucketStack, new AspectList().add(Aspect.ORDER, 1).add(Aspect.SLIME, 6).add(Aspect.GREED, 2).add(Aspect.HUNGER, 2));
				}
			}

			{
				final ItemStack fluidBlock = GrowthCraftBees.fluids.honey.asFluidBlockItemStack();
				if (fluidBlock != null)
				{
					ThaumcraftApi.registerObjectTag(fluidBlock, new AspectList().add(Aspect.ORDER, 1).add(Aspect.SLIME, 2).add(Aspect.GREED, 1).add(Aspect.HUNGER, 1));
				}
			}
		}

		if (GrowthCraftBees.blocks.beeBoxNether != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBoxNether.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.ENTROPY, 1).add(Aspect.VOID, 1));
		}
		if (GrowthCraftBees.blocks.beeBoxBamboo != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBoxBamboo.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
		}
		if (GrowthCraftBees.blocks.beeBoxThaumcraft != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBoxThaumcraft.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1).add(Aspect.MAGIC, 1));
		}
		if (GrowthCraftBees.blocks.beeBoxNatura != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBoxNatura.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
		}
		if (GrowthCraftBees.blocks.beeBoxBiomesOPlenty != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBoxBiomesOPlenty.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
		}
		if (GrowthCraftBees.blocks.beeBoxBotania != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.blocks.beeBoxBotania.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1).add(Aspect.MAGIC, 1));
		}
		if (GrowthCraftBees.blocks.beeBoxesForestry != null)
		{
			for (BlockTypeDefinition<? extends BlockBeeBox> bdef : GrowthCraftBees.blocks.beeBoxesForestry)
			{
				if (bdef != null)
				{
					ThaumcraftApi.registerObjectTag(bdef.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
				}
			}
		}
		if (GrowthCraftBees.blocks.beeBoxesForestryFireproof != null)
		{
			for (BlockTypeDefinition<? extends BlockBeeBox> bdef : GrowthCraftBees.blocks.beeBoxesForestryFireproof)
			{
				if (bdef != null)
				{
					ThaumcraftApi.registerObjectTag(bdef.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
				}
			}
		}
	}
}

