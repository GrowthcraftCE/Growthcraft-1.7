package growthcraft.api.cellar;

import net.minecraft.block.Block;

public interface IHeatSourceRegistry
{
	/**
	 * Adds a valid heat source (like fire, lava, etc.)
	 * Currently only used by Brew Kettle.
	 *
	 * @param block - The block
	 * @param meta - possible block meta, if this is -1 treats it as a wild card
	 * @param heat - how effective is this heat source, fire is 1.0f by default, higher the hotter, lower the cooler
	 **/
	public void addHeatSource(Block block, int meta, float heat);
	public void addHeatSource(Block block, int meta);
	public void addHeatSource(Block block);

	public float getHeatMultiplier(Block block, int meta);
	public float getHeatMultiplier(Block block);

	public boolean isBlockHeatSource(Block block, int meta);
	public boolean isBlockHeatSource(Block block);
}
