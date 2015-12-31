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

import java.util.HashMap;
import javax.annotation.Nonnull;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.ItemKey;

import net.minecraft.block.Block;

public class HeatSourceRegistry implements IHeatSourceRegistry
{
	static class HeatMap extends HashMap<Integer, IHeatSourceBlock>
	{
		public static final long serialVersionUID = 1L;
	}

	static class HeatSourceTree extends HashMap<Block, HeatMap>
	{
		public static final long serialVersionUID = 1L;
	}

	public static final float DEFAULT_HEAT = 1.0f;
	public static final float NO_HEAT = 0.0f;
	private ILogger logger = NullLogger.INSTANCE;

	private HeatSourceTree heatSources = new HeatSourceTree();

	@Override
	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addHeatSource(@Nonnull Block block, int meta, IHeatSourceBlock heat)
	{
		if (!heatSources.containsKey(block))
		{
			heatSources.put(block, new HeatMap());
		}
		final HeatMap map = heatSources.get(block);
		map.put(meta, heat);
	}

	@Override
	public void addHeatSource(@Nonnull Block block, int meta, float heat)
	{
		addHeatSource(block, meta, new GenericHeatSourceBlock(block, heat));
	}

	@Override
	public void addHeatSource(@Nonnull Block block, int meta)
	{
		addHeatSource(block, meta, DEFAULT_HEAT);
	}

	@Override
	public void addHeatSource(@Nonnull Block block, IHeatSourceBlock heat)
	{
		addHeatSource(block, ItemKey.WILDCARD_VALUE, heat);
	}

	@Override
	public void addHeatSource(@Nonnull Block block)
	{
		addHeatSource(block, ItemKey.WILDCARD_VALUE);
	}

	@Override
	public IHeatSourceBlock getHeatSource(Block block, int meta)
	{
		final HeatMap map = heatSources.get(block);
		if (map == null) return null;

		IHeatSourceBlock f = map.get(meta);
		if (f == null) f = map.get(ItemKey.WILDCARD_VALUE);
		if (f == null) return null;
		return f;
	}

	@Override
	public IHeatSourceBlock getHeatSource(Block block)
	{
		return getHeatSource(block, ItemKey.WILDCARD_VALUE);
	}

	@Override
	public boolean isBlockHeatSource(Block block, int meta)
	{
		final HeatMap map = heatSources.get(block);
		if (map == null) return false;
		return map.get(meta) != null || map.get(ItemKey.WILDCARD_VALUE) != null;
	}

	@Override
	public boolean isBlockHeatSource(Block block)
	{
		return isBlockHeatSource(block, ItemKey.WILDCARD_VALUE);
	}
}
