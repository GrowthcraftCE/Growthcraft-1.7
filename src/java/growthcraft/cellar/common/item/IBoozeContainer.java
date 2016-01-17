package growthcraft.cellar.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public interface IBoozeContainer
{
	Fluid getBooze(ItemStack stack);
}
