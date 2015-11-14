package growthcraft.cellar.common.inventory;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;
import growthcraft.core.common.inventory.GrcContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CellarContainer extends GrcContainer
{
	public CellarContainer(TileEntityCellarDevice te)
	{
		super(te);
	}
}
