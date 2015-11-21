package growthcraft.bees.common.block;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.api.core.definition.IItemStackFactory;

import net.minecraft.item.ItemStack;

public enum EnumBeeBoxThaumcraft implements IItemStackFactory
{
	GREATWOOD,
	SILVERWOOD;

	public final int meta;

	private EnumBeeBoxThaumcraft()
	{
		this.meta = ordinal();
	}

	public ItemStack asStack(int size)
	{
		if (GrowthCraftBees.beeBoxThaumcraft != null)
		{
			return GrowthCraftBees.beeBoxThaumcraft.asStack(size, meta);
		}
		return null;
	}

	public ItemStack asStack()
	{
		return asStack(1);
	}
}
