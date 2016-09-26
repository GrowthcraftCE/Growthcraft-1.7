package growthcraft.fishtrap.common.inventory;

import growthcraft.core.common.inventory.GrcContainer;
import growthcraft.core.common.inventory.slot.GrcSlot;
import growthcraft.core.common.inventory.slot.SlotInput;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFishTrap extends GrcContainer
{
	public ContainerFishTrap(InventoryPlayer player, TileEntityFishTrap fishtrap)
	{
		super(fishtrap);
		this.addSlotToContainer(new SlotInput(fishtrap, fishtrap.getBaitInventoryOffset(), 17, 20));
		for (int i = 0; i < fishtrap.getTrapInventorySize(); ++i)
		{
			final int x = 44 + i * 18;
			this.addSlotToContainer(new GrcSlot(fishtrap, i, x, 20));
		}
		bindPlayerInventory(player, 8, 51);
	}
}
