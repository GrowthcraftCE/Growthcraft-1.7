package growthcraft.cellar.common.inventory;

import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.common.inventory.slot.SlotFruitPressResidue;
import growthcraft.cellar.common.inventory.slot.SlotInputPressing;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFruitPress extends CellarContainer
{
	public ContainerFruitPress(InventoryPlayer player, TileEntityFruitPress fruitPress)
	{
		super(fruitPress);
		// Slot Indexes:
		//0            raw
		//1            residue
		//2 - 28 (29)  player.inv.backpack
		//29 - 37 (38) player.inv.hotbar

		addSlotToContainer(new SlotInputPressing(fruitPress, 0, 45, 35));
		addSlotToContainer(new SlotFruitPressResidue(fruitPress, 1, 116, 17));

		bindPlayerInventory(player, 8, 84);
	}
}
