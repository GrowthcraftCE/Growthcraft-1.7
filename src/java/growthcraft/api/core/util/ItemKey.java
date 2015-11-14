package growthcraft.api.core.util;

import java.lang.IllegalArgumentException;
import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

/**
 * As the name implies, this class is used in place of a List for Item keys
 */
public class ItemKey extends HashKey
{
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;

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
		super();
		final Item iitem = Item.getItemFromBlock(block);
		if (iitem == null)
		{
			throw new IllegalArgumentException("Invalid Block given for ItemKey (block=" + block + " meta=" + imeta + ")");
		}
		this.item = iitem;
		this.meta = imeta;
		generateHashCode();
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
