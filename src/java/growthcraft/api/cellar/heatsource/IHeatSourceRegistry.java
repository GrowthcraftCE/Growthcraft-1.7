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
package growthcraft.api.cellar.heatsource;

import growthcraft.api.core.log.ILoggable;

import net.minecraft.block.Block;

public interface IHeatSourceRegistry extends ILoggable
{
	/**
	 * Adds a valid heat source (like fire, lava, etc.)
	 * Currently only used by Brew Kettle.
	 *
	 * @param block - The block
	 * @param meta - possible block meta
	 * @param heat - how effective is this heat source, fire is 1.0f by default, higher the hotter, lower the cooler
	 **/
	void addHeatSource(Block block, int meta, IHeatSourceBlock heat);
	void addHeatSource(Block block, int meta, float heat);
	void addHeatSource(Block block, int meta);
	void addHeatSource(Block block, IHeatSourceBlock heat);
	void addHeatSource(Block block);

	IHeatSourceBlock getHeatSource(Block block, int meta);
	IHeatSourceBlock getHeatSource(Block block);

	boolean isBlockHeatSource(Block block, int meta);
	boolean isBlockHeatSource(Block block);
}
