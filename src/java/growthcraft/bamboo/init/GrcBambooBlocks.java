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
package growthcraft.bamboo.init;

import growthcraft.bamboo.common.block.BlockBamboo;
import growthcraft.bamboo.common.block.BlockBambooDoor;
import growthcraft.bamboo.common.block.BlockBambooFence;
import growthcraft.bamboo.common.block.BlockBambooFenceGate;
import growthcraft.bamboo.common.block.BlockBambooLeaves;
import growthcraft.bamboo.common.block.BlockBambooScaffold;
import growthcraft.bamboo.common.block.BlockBambooShoot;
import growthcraft.bamboo.common.block.BlockBambooSlab;
import growthcraft.bamboo.common.block.BlockBambooStairs;
import growthcraft.bamboo.common.block.BlockBambooStalk;
import growthcraft.bamboo.common.block.BlockBambooWall;
import growthcraft.bamboo.common.item.ItemBambooSlab;
import growthcraft.core.common.block.BlockFenceRope;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.integration.NEI;
import growthcraft.core.registry.FenceRopeRegistry;

import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;

public class GrcBambooBlocks extends GrcModuleBase
{
	public BlockDefinition bambooBlock;
	public BlockTypeDefinition<BlockBambooShoot> bambooShoot;
	public BlockTypeDefinition<BlockBambooStalk> bambooStalk;
	public BlockDefinition bambooLeaves;
	public BlockDefinition bambooFence;
	public BlockDefinition bambooFenceRope;
	public BlockDefinition bambooWall;
	public BlockDefinition bambooStairs;
	public BlockTypeDefinition<BlockSlab> bambooSingleSlab;
	public BlockTypeDefinition<BlockSlab> bambooDoubleSlab;
	public BlockDefinition bambooDoor;
	public BlockDefinition bambooFenceGate;
	public BlockDefinition bambooScaffold;

	@Override
	public void preInit()
	{
		bambooBlock      = new BlockDefinition(new BlockBamboo());
		bambooShoot      = new BlockTypeDefinition<BlockBambooShoot>(new BlockBambooShoot());
		bambooStalk      = new BlockTypeDefinition<BlockBambooStalk>(new BlockBambooStalk());
		bambooLeaves     = new BlockDefinition(new BlockBambooLeaves());
		bambooFence      = new BlockDefinition(new BlockBambooFence());
		bambooWall       = new BlockDefinition(new BlockBambooWall());
		bambooStairs     = new BlockDefinition(new BlockBambooStairs());
		bambooSingleSlab = new BlockTypeDefinition<BlockSlab>(new BlockBambooSlab(false));
		bambooDoubleSlab = new BlockTypeDefinition<BlockSlab>(new BlockBambooSlab(true));
		bambooDoor       = new BlockDefinition(new BlockBambooDoor());
		bambooFenceGate  = new BlockDefinition(new BlockBambooFenceGate());
		bambooScaffold   = new BlockDefinition(new BlockBambooScaffold());
		bambooFenceRope = new BlockDefinition(new BlockFenceRope(bambooFence.getBlock(), "grc.bambooFenceRope"));
		FenceRopeRegistry.instance().addEntry(bambooFence.getBlock(), bambooFenceRope.getBlock());
	}

	@Override
	public void register()
	{
		bambooBlock.register("grc.bambooBlock");
		bambooShoot.register("grc.bambooShoot");
		bambooStalk.register("grc.bambooStalk");
		bambooLeaves.register("grc.bambooLeaves");
		bambooFence.register("grc.bambooFence");
		bambooFenceRope.register("grc.bambooFenceRope");
		bambooWall.register("grc.bambooWall");
		bambooStairs.register("grc.bambooStairs");
		bambooSingleSlab.register("grc.bambooSingleSlab", ItemBambooSlab.class);
		bambooDoubleSlab.register("grc.bambooDoubleSlab", ItemBambooSlab.class);
		bambooDoor.register("grc.bambooDoor");
		bambooFenceGate.register("grc.bambooFenceGate");
		bambooScaffold.register("grc.bambooScaffold");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(bambooBlock.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(bambooStalk.getBlock(), 5, 4);
		Blocks.fire.setFireInfo(bambooLeaves.getBlock(), 30, 60);
		Blocks.fire.setFireInfo(bambooFence.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(bambooWall.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(bambooStairs.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(bambooSingleSlab.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(bambooDoubleSlab.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(bambooScaffold.getBlock(), 5, 20);

		NEI.hideItem(bambooDoor.asStack());
	}
}
