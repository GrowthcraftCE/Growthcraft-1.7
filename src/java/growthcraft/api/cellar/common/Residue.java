package growthcraft.api.cellar.common;

import net.minecraft.item.ItemStack;

public class Residue
{
	public final ItemStack residueItem;
	/**
	 * How frequent is this residual item created when processing?
	 * The lower this value, the less frequent, 1 is the maximum (all the time)
	 */
	public final float spawnRate;

	public Residue(ItemStack item, float rate)
	{
		this.residueItem = item;
		this.spawnRate = rate;
	}
}
