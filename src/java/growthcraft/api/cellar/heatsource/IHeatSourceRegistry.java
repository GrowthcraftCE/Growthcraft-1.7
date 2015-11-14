package growthcraft.api.cellar.heatsource;

import net.minecraft.block.Block;

public interface IHeatSourceRegistry
{
	/**
	 * Adds a valid heat source (like fire, lava, etc.)
	 * Currently only used by Brew Kettle.
	 *
	 * @param block - The block
	 * @param meta - possible block meta
	 * @param heat - how effective is this heat source, fire is 1.0f by default, higher the hotter, lower the cooler
	 **/
	public void addHeatSource(Block block, int meta, IHeatSourceBlock heat);
	public void addHeatSource(Block block, int meta, float heat);
	public void addHeatSource(Block block, int meta);
	public void addHeatSource(Block block, IHeatSourceBlock heat);
	public void addHeatSource(Block block);

	public IHeatSourceBlock getHeatSource(Block block, int meta);
	public IHeatSourceBlock getHeatSource(Block block);

	public boolean isBlockHeatSource(Block block, int meta);
	public boolean isBlockHeatSource(Block block);
}
