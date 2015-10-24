package growthcraft.api.core.util;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * As the name implies, this class is used in place of a List for Item keys
 */
public class ItemKey extends HashKey
{
	public final Item item;
	public final int meta;

	public ItemKey(@Nonnull Item iitem, int imeta)
	{
		super();
		this.item = iitem;
		this.meta = imeta;
		generateHashCode();
	}

	public ItemKey(@Nonnull Block block, int imeta)
	{
		this(Item.getItemFromBlock(block), imeta);
	}

	public ItemKey(@Nonnull ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage());
	}

	public void generateHashCode()
	{
		this.hash = item.hashCode();
		this.hash = 31 * hash + meta;
	}
}
