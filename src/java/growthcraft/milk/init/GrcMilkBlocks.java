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
package growthcraft.milk.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.milk.common.block.BlockButterChurn;
import growthcraft.milk.common.block.BlockCheeseBlock;
import growthcraft.milk.common.block.BlockCheesePress;
import growthcraft.milk.common.block.BlockCheeseVat;
import growthcraft.milk.common.block.BlockHangingCurds;
import growthcraft.milk.common.block.BlockPancheon;
import growthcraft.milk.common.block.BlockThistle;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.ItemBlockCheeseBlock;
import growthcraft.milk.common.item.ItemBlockHangingCurds;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraftforge.oredict.OreDictionary;

public class GrcMilkBlocks extends GrcModuleBase
{
	public BlockDefinition butterChurn;
	public BlockDefinition cheeseBlock;
	public BlockDefinition cheesePress;
	public BlockDefinition cheeseVat;
	public BlockDefinition hangingCurds;
	public BlockDefinition pancheon;
	public BlockDefinition thistle;

	@Override
	public void preInit()
	{
		this.butterChurn = new BlockDefinition(new BlockButterChurn());
		this.cheeseBlock = new BlockDefinition(new BlockCheeseBlock());
		this.cheesePress = new BlockDefinition(new BlockCheesePress());
		this.cheeseVat = new BlockDefinition(new BlockCheeseVat());
		this.hangingCurds = new BlockDefinition(new BlockHangingCurds());
		this.pancheon = new BlockDefinition(new BlockPancheon());
		if (GrowthCraftMilk.getConfig().thistleEnabled)
		{
			this.thistle = new BlockDefinition(new BlockThistle());
		}
	}

	private void registerOres()
	{
		for (EnumCheeseType type : EnumCheeseType.VALUES)
		{
			OreDictionary.registerOre("blockCheese", type.asBlockItemStack());
		}

		if (thistle != null)
		{
			OreDictionary.registerOre("flowerThistle", thistle.getItem());
			OreDictionary.registerOre("rennetSource", thistle.getItem());
		}
	}

	@Override
	public void register()
	{
		butterChurn.register("grcmilk.ButterChurn");
		cheeseBlock.register("grcmilk.CheeseBlock", ItemBlockCheeseBlock.class);
		cheesePress.register("grcmilk.CheesePress");
		cheeseVat.register("grcmilk.CheeseVat");
		hangingCurds.register("grcmilk.HangingCurds", ItemBlockHangingCurds.class);
		pancheon.register("grcmilk.Pancheon");
		if (thistle != null)
		{
			thistle.register("grcmilk.Thistle");
		}
		registerOres();
	}
}
