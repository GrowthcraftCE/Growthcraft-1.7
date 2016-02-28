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

import growthcraft.core.common.block.BlockFenceRope;
import growthcraft.core.common.block.BlockRope;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.integration.NEI;
import growthcraft.core.registry.FenceRopeRegistry;

import net.minecraft.init.Blocks;

public class GrcCoreBlocks extends GrcModuleBase
{
	public BlockDefinition ropeBlock;
	public BlockDefinition fenceRope;
	public BlockDefinition netherBrickFenceRope;

	@Override
	public void preInit()
	{
		ropeBlock = new BlockDefinition(new BlockRope());
		fenceRope = new BlockDefinition(new BlockFenceRope(Blocks.fence, "grc.fenceRope"));
		netherBrickFenceRope = new BlockDefinition(new BlockFenceRope(Blocks.nether_brick_fence, "grc.netherBrickFenceRope"));

		FenceRopeRegistry.instance().addEntry(Blocks.fence, fenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.nether_brick_fence, netherBrickFenceRope.getBlock());
	}

	@Override
	public void register()
	{
		//====================
		// REGISTRIES
		//====================
		fenceRope.register("grc.fenceRope");
		ropeBlock.register("grc.ropeBlock");
		netherBrickFenceRope.register("grc.netherBrickFenceRope");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(fenceRope.getBlock(), 5, 20);

		NEI.hideItem(fenceRope.asStack());
		NEI.hideItem(ropeBlock.asStack());
	}
}
