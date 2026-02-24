package growthcraft.api.bees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.util.BlockKey;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BeesRegistry implements IBeesRegistry
{
	private static final BeesRegistry INSTANCE = new BeesRegistry();

	private final List<ItemKey> beesList = new ArrayList<ItemKey>();
	private final Map<ItemKey, ItemStack> emptyToFullHoneyComb = new HashMap<ItemKey, ItemStack>();
	private final Map<ItemKey, ItemStack> fullToEmptyHoneyComb = new HashMap<ItemKey, ItemStack>();
	private final Map<BlockKey, IFlowerBlockEntry> flowerEntries = new HashMap<BlockKey, IFlowerBlockEntry>();
	private ILogger logger = NullLogger.INSTANCE;

	public static final BeesRegistry instance()
	{
		return INSTANCE;
	}

	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	private ItemKey stackToKey(@Nonnull ItemStack itemstack)
	{
		return new ItemKey(itemstack);
	}

	///////////////////////////////////////////////////////////////////////
	// BEES ///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	/**
	 * addBee()
	 * Adds a custom bee the mod.
	 * NOTE: This is not meta-sensitive.
	 *
	 * @param bee - The Item/Block to be registered.
	 */
	public void addBee(@Nonnull Item bee, int meta)
	{
		logger.debug("Adding Bee {%s}:%d", bee, meta);
		beesList.add(new ItemKey(bee, meta));
	}

	public void addBee(@Nonnull Block bee, int meta)
	{
		addBee(Item.getItemFromBlock(bee), meta);
	}

	public void addBee(@Nonnull ItemStack stack)
	{
		final ItemKey key = stackToKey(stack);
		logger.debug("Adding Bee {%s}", key);
		beesList.add(key);
	}

	public void addBee(@Nonnull Item bee)
	{
		addBee(bee, ItemKey.WILDCARD_VALUE);
	}

	public void addBee(@Nonnull Block bee)
	{
		addBee(Item.getItemFromBlock(bee), ItemKey.WILDCARD_VALUE);
	}

	protected void addHoneyCombMapping(@Nonnull ItemStack empty, @Nonnull ItemStack full)
	{
		logger.debug("Adding Honey Comb mapping {%s} - {%s}", empty, full);
		emptyToFullHoneyComb.put(stackToKey(empty), full);
		fullToEmptyHoneyComb.put(stackToKey(full), empty);
	}

	public void addHoneyComb(@Nonnull ItemStack empty, @Nonnull ItemStack full)
	{
		addHoneyCombMapping(empty, full);
	}

	public ItemStack getFilledHoneyComb(@Nonnull ItemStack itemstack)
	{
		return emptyToFullHoneyComb.get(stackToKey(itemstack));
	}

	public ItemStack getEmptyHoneyComb(@Nonnull ItemStack itemstack)
	{
		return fullToEmptyHoneyComb.get(stackToKey(itemstack));
	}

	protected boolean isItemFilledHoneyComb(@Nonnull ItemKey key)
	{
		return fullToEmptyHoneyComb.containsKey(key);
	}

	public boolean isItemFilledHoneyComb(@Nonnull ItemStack itemstack)
	{
		return isItemFilledHoneyComb(stackToKey(itemstack));
	}

	protected boolean isItemEmptyHoneyComb(@Nonnull ItemKey key)
	{
		return emptyToFullHoneyComb.containsKey(key);
	}

	public boolean isItemEmptyHoneyComb(@Nonnull ItemStack itemstack)
	{
		return isItemEmptyHoneyComb(stackToKey(itemstack));
	}

	public boolean isItemHoneyComb(@Nonnull ItemStack itemstack)
	{
		final ItemKey key = stackToKey(itemstack);
		return isItemFilledHoneyComb(key) || isItemEmptyHoneyComb(key);
	}

	public void addFlower(@Nonnull BlockKey key, @Nonnull IFlowerBlockEntry entry)
	{
		logger.debug("Adding Flower {%s}:{%s}", key, entry);
		flowerEntries.put(key, entry);
	}

	public void addFlower(@Nonnull IFlowerBlockEntry entry)
	{
		addFlower(new BlockKey(entry.getBlock(), entry.getMetadata()), entry);
	}

	/**
	 * Adds a custom flower the mod.
	 * NOTE: This is meta-sensitive.
	 *
	 * @param flower - Block to be registered.
	 * @param meta   - Metadata of the block to be registered.
	 */
	public void addFlower(@Nonnull Block flower, int meta)
	{
		addFlower(new BlockKey(flower, meta), new GenericFlowerBlockEntry(flower, meta));
	}

	/**
	 * Flower wildcard
	 *
	 * @param flower - Block to be registered.
	 */
	public void addFlower(@Nonnull Block flower)
	{
		addFlower(flower, ItemKey.WILDCARD_VALUE);
	}

	/**
	 * @param itemstack - an itemstack to check
	 * @return Does the provided itemstack contain any known bees?
	 */
	public boolean isItemBee(@Nullable ItemStack itemstack)
	{
		if (itemstack == null) return false;
		final Item item = itemstack.getItem();
		final int meta = itemstack.getItemDamage();
		for (ItemKey key : beesList)
		{
			if (item == key.item)
			{
				if (key.meta == ItemKey.WILDCARD_VALUE || key.meta == meta)
				{
					return true;
				}
			}
		}
		return false;
	}

	public IFlowerBlockEntry getFlowerBlockEntry(@Nonnull BlockKey key)
	{
		return flowerEntries.get(key);
	}

	public IFlowerBlockEntry getFlowerBlockEntry(@Nullable Block block, int meta)
	{
		if (block == null) return null;
		final IFlowerBlockEntry entry = getFlowerBlockEntry(new BlockKey(block, meta));
		if (entry != null) return entry;
		return getFlowerBlockEntry(new BlockKey(block, ItemKey.WILDCARD_VALUE));
	}

	public boolean isBlockFlower(@Nullable Block block, int meta)
	{
		return flowerEntries.containsKey(new BlockKey(block, meta)) ||
			flowerEntries.containsKey(new BlockKey(block, ItemKey.WILDCARD_VALUE));
	}
}
