package growthcraft.cellar.common.inventory;

import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.common.inventory.slot.SlotInputYeast;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCultureJar extends CellarContainer
{
	public ContainerCultureJar(InventoryPlayer player, TileEntityCultureJar fermentJar)
	{
		super(fermentJar);
		// Slot Indexes:
		//0            output
		//1 - 27 (28)  player.inv.backpack
		//28 - 36 (37) player.inv.hotbar

		addSlotToContainer(new SlotInputYeast(fermentJar, 0, 80, 35));

		bindPlayerInventory(player, 8, 84);
	}
}
