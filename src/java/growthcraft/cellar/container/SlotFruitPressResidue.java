package growthcraft.cellar.container;

import growthcraft.cellar.GrowthCraftCellar;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFruitPressResidue extends Slot
{
	final ContainerFruitPress con;

	public SlotFruitPressResidue(ContainerFruitPress cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
		this.con = cont;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack != null ? stack.getItem() == GrowthCraftCellar.residue.getItem() && stack.getItemDamage() == GrowthCraftCellar.residue.getItemDamage()  : false;
	}
}
