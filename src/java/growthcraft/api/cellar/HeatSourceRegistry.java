package growthcraft.api.cellar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import net.minecraft.block.Block;

public class HeatSourceRegistry implements IHeatSourceRegistry
{
	static class HeatMap extends HashMap<Integer, Float>
	{
		public static final long serialVersionUID = 1L;
	}

	static class HeatSourceTree extends HashMap<Block, HeatMap>
	{
		public static final long serialVersionUID = 1L;
	}

	public static final float DEFAULT_HEAT = 1.0f;
	public static final float NO_HEAT = 0.0f;
	private static final int NO_META = -1;

	private HeatSourceTree heatSources = new HeatSourceTree();

	public void addHeatSource(Block block, int meta, float heat)
	{
		if (!heatSources.containsKey(block))
		{
			heatSources.put(block, new HeatMap());
		}
		final HeatMap map = heatSources.get(block);
		map.put(meta, heat);
	}

	public void addHeatSource(Block block, int meta)
	{
		addHeatSource(block, meta, DEFAULT_HEAT);
	}

	public void addHeatSource(Block block)
	{
		addHeatSource(block, NO_META);
	}

	public float getHeatMultiplier(Block block, int meta)
	{
		final HeatMap map = heatSources.get(block);
		if (map == null) return NO_HEAT;

		Float f = map.get(meta);
		if (f == null) f = map.get(NO_META);
		if (f == null) return NO_HEAT;
		return f;
	}

	public float getHeatMultiplier(Block block)
	{
		return getHeatMultiplier(block, NO_META);
	}

	public boolean isBlockHeatSource(Block block, int meta)
	{
		final HeatMap map = heatSources.get(block);
		if (map == null) return false;
		return map.get(meta) != null || map.get(NO_META) != null;
	}

	public boolean isBlockHeatSource(Block block)
	{
		return isBlockHeatSource(block, NO_META);
	}
}
