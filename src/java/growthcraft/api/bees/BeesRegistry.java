package growthcraft.api.bees;

import growthcraft.api.core.util.ItemKey;
import growthcraft.api.core.util.BlockKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BeesRegistry
{
	/**
	 * Gwafu:
	 *
	 * Yes, it's the same functons/methods as Forge's tall grass hook.
	 */
	private static final BeesRegistry INSTANCE = new BeesRegistry();
	private static final int NO_META = -1;

	private final List<Item> beesList = new ArrayList<Item>();
	private final List<ItemKey> emptyHoneyCombList = new ArrayList<ItemKey>();
	private final List<ItemKey> filledHoneyCombList = new ArrayList<ItemKey>();
	private final Map<ItemKey, ItemStack> honeyCombMap = new HashMap<ItemKey, ItemStack>();
	private final List<BlockKey> flowersList = new ArrayList<BlockKey>();

	public static final BeesRegistry instance()
	{
		return INSTANCE;
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
	public void addBee(Block bee)
	{
		this.beesList.add(Item.getItemFromBlock(bee));
	}

	public void addBee(Item bee)
	{
		this.beesList.add(bee);
	}

	private ItemKey stackToKey(ItemStack itemstack)
	{
		return new ItemKey(itemstack);
	}

	public void addEmptyHoneyComb(ItemStack itemstack)
	{
		emptyHoneyCombList.add(stackToKey(itemstack));
	}

	public void addFilledHoneyComb(ItemStack itemstack)
	{
		filledHoneyCombList.add(stackToKey(itemstack));
	}

	public void addHoneyCombMapping(ItemStack empty, ItemStack full)
	{
		honeyCombMap.put(stackToKey(empty), full);
	}

	public void addHoneyComb(ItemStack empty, ItemStack full)
	{
		addEmptyHoneyComb(empty);
		addFilledHoneyComb(full);
		addHoneyCombMapping(empty, full);
	}

	public ItemStack getFilledHoneyComb(ItemStack itemstack)
	{
		return honeyCombMap.get(stackToKey(itemstack));
	}

	public boolean isItemFilledHoneyComb(ItemStack itemstack)
	{
		return filledHoneyCombList.contains(stackToKey(itemstack));
	}

	public boolean isItemEmptyHoneyComb(ItemStack itemstack)
	{
		return emptyHoneyCombList.contains(stackToKey(itemstack));
	}

	public boolean isItemHoneyComb(ItemStack itemstack)
	{
		final ItemKey key = stackToKey(itemstack);
		return emptyHoneyCombList.contains(key) || filledHoneyCombList.contains(key);
	}

	/**
	 * addFlower()
	 * Adds a custom flower the mod.
	 * NOTE: This is meta-sensitive.
	 *
	 * @param flower - Block to be registered.
	 * @param meta   - Metadata of the block to be registered.
	 */
	public void addFlower(Block flower, int meta)
	{
		this.flowersList.add(new BlockKey(flower, meta));
	}

	/**
	 * Flower wildcard
	 *
	 * @param flower - Block to be registered.
	 */
	public void addFlower(Block flower)
	{
		addFlower(flower, NO_META);
	}

	/**
	 * @param itemstack - an itemstack to check
	 * @return Does the provided itemstack contain any known bees?
	 */
	public boolean isItemBee(ItemStack itemstack)
	{
		if (itemstack == null) return false;
		return this.beesList.contains(itemstack.getItem());
	}

	public boolean isBlockFlower(Block block, int meta)
	{
		if (block == null) return false;
		return this.flowersList.contains(new BlockKey(block, meta)) ||
			this.flowersList.contains(new BlockKey(block, NO_META));
	}
}
