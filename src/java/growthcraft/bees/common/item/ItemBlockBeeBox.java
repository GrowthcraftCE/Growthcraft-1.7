package growthcraft.bees.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/*
 * This ItemBlock is used for the vanilla wood variations of the BeeBox
 */
public class ItemBlockBeeBox extends ItemBlock
{
	public ItemBlockBeeBox(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	public int getMetadata(int meta)
	{
		return meta;
	}
}
