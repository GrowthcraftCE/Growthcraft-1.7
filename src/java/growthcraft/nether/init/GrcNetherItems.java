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
package growthcraft.nether.init;

import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.nether.common.item.ItemEctoplasm;
import growthcraft.nether.common.item.ItemNetherGhastPowder;
import growthcraft.nether.common.item.ItemNetherMaliceFruit;
import growthcraft.nether.common.item.ItemNetherMuertecap;
import growthcraft.nether.common.item.ItemNetherPepper;
import growthcraft.nether.common.item.ItemNetherRashSpores;
import growthcraft.nether.common.item.ItemNetherSquashSeeds;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;

public class GrcNetherItems extends GrcModuleBase
{
	public ItemDefinition ectoplasm;
	public ItemDefinition netherMaliceFruit;
	public ItemDefinition netherMuertecap;
	public ItemDefinition netherPepper;
	public ItemDefinition netherRashSpores;
	public ItemDefinition netherSquashSeeds;
	public ItemDefinition netherGhastPowder;

	@Override
	public void preInit()
	{
		this.ectoplasm = new ItemDefinition(new ItemEctoplasm());
		this.netherMuertecap = new ItemDefinition(new ItemNetherMuertecap());
		this.netherPepper = new ItemDefinition(new ItemNetherPepper());
		this.netherRashSpores = new ItemDefinition(new ItemNetherRashSpores());
		this.netherSquashSeeds = new ItemDefinition(new ItemNetherSquashSeeds());
		this.netherMaliceFruit = new ItemDefinition(new ItemNetherMaliceFruit());
		this.netherGhastPowder = new ItemDefinition(new ItemNetherGhastPowder());
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(ectoplasm.getItem(), "grcnether.ectoplasm");
		GameRegistry.registerItem(netherMuertecap.getItem(), "grcnether.netherMuertecapFood");
		GameRegistry.registerItem(netherPepper.getItem(), "grcnether.netherPepper");
		GameRegistry.registerItem(netherRashSpores.getItem(), "grcnether.netherRashSpores");
		GameRegistry.registerItem(netherSquashSeeds.getItem(), "grcnether.netherSquashSeeds");
		GameRegistry.registerItem(netherMaliceFruit.getItem(), "grcnether.netherMaliceFruitItem");
		GameRegistry.registerItem(netherGhastPowder.getItem(), "grcnether.netherGhastPowder");

		GameRegistry.addShapelessRecipe(netherGhastPowder.asStack(3), Items.ghast_tear, Items.blaze_powder);
	}
}
