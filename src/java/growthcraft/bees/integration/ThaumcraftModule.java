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

import growthcraft.bees.common.block.BlockThaumcraftBeeBox.ThaumcraftBeeBoxType;
import growthcraft.bees.common.block.BlockThaumcraftBeeBox;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftBees.MOD_ID);
	}

	@Override
	protected void doPreInit()
	{
		GrowthCraftBees.thaumcraftBeeBox = new BlockDefinition(new BlockThaumcraftBeeBox());
	}

	@Override
	protected void doRegister()
	{
		GameRegistry.registerBlock(GrowthCraftBees.thaumcraftBeeBox.getBlock(), ItemBlockBeeBox.class, "grc.thaumcraftBeeBox");

		final Block blockWoodenDevice = GameRegistry.findBlock(modID, "blockWoodenDevice");
		if (blockWoodenDevice != null)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftBees.thaumcraftBeeBox.asStack(1, ThaumcraftBeeBoxType.GREATWOOD), " A ", "A A", "AAA", 'A', new ItemStack(blockWoodenDevice, 1, 6)));
			GameRegistry.addRecipe(new ShapedOreRecipe(GrowthCraftBees.thaumcraftBeeBox.asStack(1, ThaumcraftBeeBoxType.SILVERWOOD), " A ", "A A", "AAA", 'A', new ItemStack(blockWoodenDevice, 1, 7)));
		}
	}

	@Override
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyCombEmpty.asStack(), new AspectList().add(Aspect.ORDER, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyCombFilled.asStack(), new AspectList().add(Aspect.ORDER, 1).add(Aspect.SLIME, 1).add(Aspect.GREED, 1).add(Aspect.HUNGER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyJar.asStack(), new AspectList().add(Aspect.SLIME, 1).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(Aspect.VOID, 1).add(Aspect.GREED, 3).add(Aspect.HUNGER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.bee.asStack(), new AspectList().add(Aspect.BEAST, 1).add(Aspect.AIR, 1).add(Aspect.FLIGHT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeHive.asStack(), new AspectList().add(Aspect.SLIME, 1).add(Aspect.BEAST, 1).add(Aspect.ORDER, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeBox.asStack(), new int[]{0,1,2,3,4,5}, new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
		if (GrowthCraftBees.maliceBeeBox != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftBees.maliceBeeBox.asStack(), new AspectList().add(Aspect.TREE, 4).add(Aspect.ENTROPY, 1).add(Aspect.VOID, 1));
		}
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.bambooBeeBox.asStack(), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.thaumcraftBeeBox.asStack(), new AspectList().add(Aspect.TREE, 4).add(Aspect.VOID, 1).add(Aspect.MAGIC, 1));

		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMead.asStack(1, 0), new AspectList().add(Aspect.WATER, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMead.asStack(1, 1), new AspectList().add(Aspect.HEAL, 3).add(Aspect.WATER, 1).add(Aspect.POISON, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMead.asStack(1, 2), new AspectList().add(Aspect.HEAL, 6).add(Aspect.WATER, 1).add(Aspect.POISON, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMead.asStack(1, 3), new AspectList().add(Aspect.HEAL, 3).add(Aspect.WATER, 1).add(Aspect.POISON, 2));

		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBuckets[0].asStack(1), new AspectList().add(Aspect.WATER, 1).add(Aspect.METAL, 8).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBuckets[1].asStack(1), new AspectList().add(Aspect.WATER, 1).add(Aspect.POISON, 2).add(Aspect.METAL, 8).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBuckets[2].asStack(1), new AspectList().add(Aspect.WATER, 1).add(Aspect.POISON, 2).add(Aspect.METAL, 8).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBuckets[3].asStack(1), new AspectList().add(Aspect.WATER, 1).add(Aspect.POISON, 2).add(Aspect.METAL, 8).add(Aspect.VOID, 1));

		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadFluids[0].asStack(1), new AspectList().add(Aspect.WATER, 4));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadFluids[1].asStack(1), new AspectList().add(Aspect.WATER, 2).add(Aspect.POISON, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadFluids[2].asStack(1), new AspectList().add(Aspect.WATER, 1).add(Aspect.POISON, 3));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadFluids[3].asStack(1), new AspectList().add(Aspect.WATER, 2).add(Aspect.POISON, 2));
	}
}

