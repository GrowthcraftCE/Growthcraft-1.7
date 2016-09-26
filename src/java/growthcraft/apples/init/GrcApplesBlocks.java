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
package growthcraft.apples.init;

import growthcraft.apples.common.block.BlockApple;
import growthcraft.apples.common.block.BlockAppleLeaves;
import growthcraft.apples.common.block.BlockAppleSapling;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.GrcModuleBlocks;

import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

public class GrcApplesBlocks extends GrcModuleBlocks
{
	public BlockDefinition appleSapling;
	public BlockDefinition appleLeaves;
	public BlockTypeDefinition<BlockApple> appleBlock;

	@Override
	public void preInit()
	{
		this.appleSapling = newDefinition(new BlockAppleSapling());
		this.appleLeaves = newDefinition(new BlockAppleLeaves());
		this.appleBlock = newTypedDefinition(new BlockApple());
	}

	@Override
	public void register()
	{
		appleSapling.register("grc.appleSapling");
		appleLeaves.register("grc.appleLeaves");
		appleBlock.register("grc.appleBlock");

		OreDictionary.registerOre("saplingTree", appleSapling.getItem());
		OreDictionary.registerOre("treeSapling", appleSapling.getItem());
		OreDictionary.registerOre("treeLeaves", appleLeaves.asStack(1, OreDictionary.WILDCARD_VALUE));

		Blocks.fire.setFireInfo(appleLeaves.getBlock(), 30, 60);
	}
}
