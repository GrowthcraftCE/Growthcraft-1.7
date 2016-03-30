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
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
}
