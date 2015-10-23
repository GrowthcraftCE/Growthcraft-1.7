package growthcraft.api.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * As the name implies, this class is used in place of a List for Item keys
 */
public class ItemKey
{
	public final Item item;
	public final int meta;
	private int hash;

	public ItemKey(Item iitem, int imeta)
	{
		this.item = iitem;
		this.meta = imeta;
		generateHashCode();
	}

	public ItemKey(ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage());
	}

	public void generateHashCode()
	{
		this.hash = item.hashCode();
		this.hash = 31 * hash + meta;
	}

	@Override
	public int hashCode()
	{
		return hash;
	}
}
