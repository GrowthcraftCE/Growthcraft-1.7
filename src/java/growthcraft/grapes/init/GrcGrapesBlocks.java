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
package growthcraft.grapes.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.GrcModuleBlocks;
import growthcraft.grapes.common.block.BlockGrapeBlock;
import growthcraft.grapes.common.block.BlockGrapeLeaves;
import growthcraft.grapes.common.block.BlockGrapeVine0;
import growthcraft.grapes.common.block.BlockGrapeVine1;

public class GrcGrapesBlocks extends GrcModuleBlocks
{
	public BlockTypeDefinition<BlockGrapeVine0> grapeVine0;
	public BlockTypeDefinition<BlockGrapeVine1> grapeVine1;
	public BlockTypeDefinition<BlockGrapeLeaves> grapeLeaves;
	public BlockDefinition grapeBlock;

	@Override
	public void preInit()
	{
		this.grapeVine0  = newTypedDefinition(new BlockGrapeVine0());
		this.grapeVine1  = newTypedDefinition(new BlockGrapeVine1());
		this.grapeLeaves = newTypedDefinition(new BlockGrapeLeaves());
		this.grapeBlock  = newDefinition(new BlockGrapeBlock());
	}

	@Override
	public void register()
	{
		grapeVine0.register("grc.grapeVine0");
		grapeVine1.register("grc.grapeVine1");
		grapeLeaves.register("grc.grapeLeaves");
		grapeBlock.register("grc.grapeBlock");
	}
}
