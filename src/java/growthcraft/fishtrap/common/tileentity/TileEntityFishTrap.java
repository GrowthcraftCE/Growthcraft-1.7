package growthcraft.fishtrap.common.tileentity;

import growthcraft.api.core.nbt.NBTType;
import growthcraft.api.fishtrap.BaitRegistry;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.inventory.InventorySlice;
import growthcraft.core.common.tileentity.event.TileEventHandler;
import growthcraft.core.common.tileentity.feature.IInteractionObject;
import growthcraft.core.common.tileentity.GrcTileInventoryBase;
import growthcraft.fishtrap.common.inventory.ContainerFishTrap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityFishTrap extends GrcTileInventoryBase implements IInteractionObject
{
	private static final int[] trapSlots = new int[] {0,1,2,3,4,5};
	private static final int[] baitSlots = new int[] {6};
	public InventorySlice trapInventory;
	public InventorySlice baitInventory;

	public TileEntityFishTrap()
	{
		super();
		this.trapInventory = new InventorySlice(this, trapSlots);
		this.baitInventory = new InventorySlice(this, baitSlots);
	}

	public float applyBaitModifier(float f)
	{
		return f;
	}

	public int getBaitInventoryOffset()
	{
		return baitSlots[0];
	}

	public int getTrapInventorySize()
	{
		return trapSlots.length;
	}

	public int getBaitInventorySize()
	{
		return baitSlots.length;
	}

	@Override
	public String getGuiID()
	{
		return "grcfishtrap:fish_trap";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerFishTrap(playerInventory, this);
	}

	@Override
	public GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 7);
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	public boolean canAddStack(ItemStack stack, int index)
	{
		return InventoryProcessor.instance().canInsertItem(trapInventory, stack, index);
	}

	public void addStack(ItemStack stack)
	{
		InventoryProcessor.instance().mergeWithSlots(trapInventory, stack);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fishTrap";
	}

	@Override
	protected void readInventoryFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("items"))
		{
			inventory.clear();
			final NBTTagList tags = nbt.getTagList("items", NBTType.COMPOUND.id);
			for (int i = 0; i < tags.tagCount(); ++i)
			{
				final NBTTagCompound item = tags.getCompoundTagAt(i);
				final byte b0 = item.getByte("Slot");
				if (b0 >= 0 && b0 < trapInventory.getSizeInventory())
				{
					final ItemStack stack = ItemStack.loadItemStackFromNBT(item);
					InventoryProcessor.instance().mergeWithSlot(trapInventory, stack, (int)b0);
				}
			}
		}
		else
		{
			super.readInventoryFromNBT(nbt);
		}
	}
}
