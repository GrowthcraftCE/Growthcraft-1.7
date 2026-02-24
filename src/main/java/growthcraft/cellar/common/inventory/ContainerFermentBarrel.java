package growthcraft.cellar.common.inventory;

import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.common.inventory.slot.SlotInputFermenting;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFermentBarrel extends CellarContainer
{
	public ContainerFermentBarrel(InventoryPlayer player, TileEntityFermentBarrel fermentBarrel)
	{
		super(fermentBarrel);
		// Slot Indexes:
		//0            raw
		//1 - 27 (28)  player.inv.backpack
		//28 - 36 (37) player.inv.hotbar

		addSlotToContainer(new SlotInputFermenting(fermentBarrel, 0, 43, 53));

		bindPlayerInventory(player, 8, 84);
	}
}
