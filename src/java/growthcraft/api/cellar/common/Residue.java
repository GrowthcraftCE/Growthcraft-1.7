package growthcraft.api.cellar.common;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class Residue
{
	public final ItemStack residueItem;
	/**
	 * How much does this residue need to build up before it creates an item?
	 * The lower this value, the more it requires, the higher the less.
	 */
	public final float pomaceRate;

	public Residue(ItemStack item, float pomace)
	{
		this.residueItem = item;
		this.pomaceRate = pomace;
	}

	public static Residue newDefault(float pomace)
	{
		return new Residue(new ItemStack(Items.dye, 1, 15), pomace);
	}
}
