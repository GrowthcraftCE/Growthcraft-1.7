package growthcraft.cellar.common.inventory;

import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.common.inventory.slot.SlotInputYeast;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFermentJar extends CellarContainer
{
	public ContainerFermentJar(InventoryPlayer player, TileEntityFermentJar fermentJar)
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
