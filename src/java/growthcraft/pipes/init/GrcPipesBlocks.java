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
package growthcraft.pipes.init;

import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.pipes.common.block.BlockPipeBase;
import growthcraft.pipes.common.item.ItemBlockPipeBase;
import growthcraft.pipes.common.tileentity.TileEntityPipeBase;
import growthcraft.pipes.util.PipeType;

import cpw.mods.fml.common.registry.GameRegistry;

public class GrcPipesBlocks extends GrcModuleBase
{
	public BlockTypeDefinition<BlockPipeBase> pipeBase;
	public BlockTypeDefinition<BlockPipeBase> pipeVacuum;

	@Override
	public void preInit()
	{
		pipeBase = new BlockTypeDefinition<BlockPipeBase>(new BlockPipeBase(PipeType.BASE));
		pipeBase.getBlock().setBlockName("grc.pipeBase");
		pipeVacuum = new BlockTypeDefinition<BlockPipeBase>(new BlockPipeBase(PipeType.VACUUM));
		pipeVacuum.getBlock().setBlockName("grc.pipeVacuum");

		GameRegistry.registerBlock(pipeBase.getBlock(), ItemBlockPipeBase.class, "grc.pipeBase");
		GameRegistry.registerBlock(pipeVacuum.getBlock(), ItemBlockPipeBase.class, "grc.pipeVacuum");

		GameRegistry.registerTileEntity(TileEntityPipeBase.class, "grc.tileentity.pipeBase");
	}
}
