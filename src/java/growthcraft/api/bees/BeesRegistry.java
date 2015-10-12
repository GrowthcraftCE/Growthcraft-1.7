package growthcraft.api.bees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private static final BeesRegistry instance = new BeesRegistry();
	private final List<Item> beesList = new ArrayList<Item>();
	private final List<List> flowersList = new ArrayList<List>();

	public static final BeesRegistry instance()
	{
		return instance;
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
		this.flowersList.add(Arrays.asList(flower, meta));
	}

	/**
	 * @param itemstack - an itemstack to check
	 * @return Does the provided itemstack contain any known bees?
	 */
	public boolean isItemBee(ItemStack itemstack)
	{
		if (itemstack == null)
		{
			return false;
		}

		return this.beesList.contains(itemstack.getItem());
	}

	public boolean isBlockFlower(Block block, int meta)
	{
		return this.flowersList.contains(Arrays.asList(block, meta));
	}
}
