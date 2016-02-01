/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
		GrowthCraftBees.beeBoxThaumcraft = new BlockTypeDefinition<BlockBeeBox>(new BlockBeeBoxThaumcraft());
		GrowthCraftBees.beeBoxThaumcraft.getBlock().setFlammability(20).setFireSpreadSpeed(5).setHarvestLevel("axe", 0);
	}

	@Override
	protected void doRegister()
	{
		if (GrowthCraftBees.beeBoxThaumcraft != null)
		{
			GameRegistry.registerBlock(GrowthCraftBees.beeBoxThaumcraft.getBlock(), ItemBlockBeeBox.class, "grc.BeeBox.Thaumcraft");
		}
	}

	@Override
	protected void doLateRegister()
	{
		if (GrowthCraftBees.beeBoxThaumcraft != null)
		{
			final Block blockWoodenDevice = GameRegistry.findBlock(modID, "blockWoodenDevice");
			if (blockWoodenDevice != null)
			{
				GameRegistry.addShapedRecipe(EnumBeeBoxThaumcraft.GREATWOOD.asStack(), " A ", "A A", "AAA", 'A', new ItemStack(blockWoodenDevice, 1, 6));
				GameRegistry.addShapedRecipe(EnumBeeBoxThaumcraft.SILVERWOOD.asStack(), " A ", "A A", "AAA", 'A', new ItemStack(blockWoodenDevice, 1, 7));
			}
		}
	}

	@Override
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.honeyCombEmpty.asStack(), new AspectList().add(Aspect.ORDER, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.honeyCombFilled.asStack(), new AspectList().add(Aspect.ORDER, 1).add(Aspect.SLIME, 1).add(Aspect.GREED, 1).add(Aspect.HUNGER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.honeyJar.asStack(), new AspectList().add(Aspect.SLIME, 1).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(Aspect.VOID, 1).add(Aspect.GREED, 3).add(Aspect.HUNGER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.items.bee.asStack(), new AspectList().add(Aspect.BEAST, 1).add(Aspect.AIR, 1).add(Aspect.FLIGHT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeHive.asStack(), new AspectList().add(Aspect.SLIME, 1).add(Aspect.BEAST, 1).add(Aspect.ORDER, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeBox.asStack(), new int[]{0,1,2,3,4,5}, new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));

		if (GrowthCraftBees.beeBoxNether != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeBoxNether.asStack(), new AspectList().add(Aspect.TREE, 4).add(Aspect.ENTROPY, 1).add(Aspect.VOID, 1));
		}
		if (GrowthCraftBees.beeBoxBamboo != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeBoxBamboo.asStack(), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
		}
		if (GrowthCraftBees.beeBoxThaumcraft != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeBoxThaumcraft.asStack(), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1).add(Aspect.MAGIC, 1));
		}

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
			ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftBees.booze.honeyMead.asStack(1, i), list.copy());
			ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftBees.booze.honeyMeadBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL));
			ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftBees.booze.honeyMeadFluids[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL));
		}
	}
}

