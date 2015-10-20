package growthcraft.pipes.common.item;

import growthcraft.api.core.GrcColour;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPipeBase extends ItemBlock
{
	public ItemBlockPipeBase(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return super.getItemStackDisplayName(stack) + "(" + GrcColour.toColour(stack.getItemDamage()).toString() + ")";
	}
}
