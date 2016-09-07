package growthcraft.bees.common.tileentity;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.api.core.item.EnumDye;
import growthcraft.api.core.util.AuxFX;
import growthcraft.bees.common.inventory.ContainerBeeBox;
import growthcraft.bees.common.tileentity.device.DeviceBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.feature.IInteractionObject;
import growthcraft.core.common.tileentity.feature.IItemHandler;
import growthcraft.core.common.tileentity.GrcTileInventoryBase;
import growthcraft.core.util.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBeeBox extends GrcTileInventoryBase implements IItemHandler, IInteractionObject
{
	public static enum HoneyCombExpect
	{
		ANY,
		EMPTY,
		FILLED;
	}

	private static final int beeBoxVersion = 3;
	private static final int[] beeSlotIds = new int[] {0};
	private static final int[] honeyCombSlotIds = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};
	private DeviceBeeBox beeBox = new DeviceBeeBox(this);

	@Override
	public String getGuiID()
	{
		return "grcbees:bee_box";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerBeeBox(playerInventory, this);
	}

	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		super.onInventoryChanged(inv, index);
		if (index > 0)
		{
			markDirtyAndUpdate();
		}
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.beeBox";
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 28);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!worldObj.isRemote) beeBox.update();
	}

	public void updateBlockTick()
	{
		if (worldObj.isRemote)
		{
			beeBox.updateClientTick();
		}
		else
		{
			beeBox.updateTick();
		}
	}

	public boolean hasBonus()
	{
		return beeBox.hasBonus();
	}

	public float getGrowthRate()
	{
		return beeBox.getGrowthRate();
	}

	public void setTime(int v)
	{
		beeBox.setBonusTime(v);
	}

	public boolean slotHasHoneyComb(int index, HoneyCombExpect expects)
	{
		final ItemStack slotItem = getStackInSlot(index);
		if (slotItem == null) return false;
		switch (expects)
		{
			case EMPTY:
				return BeesRegistry.instance().isItemEmptyHoneyComb(slotItem);
			case FILLED:
				return BeesRegistry.instance().isItemFilledHoneyComb(slotItem);
			default:
				return BeesRegistry.instance().isItemHoneyComb(slotItem);
		}
	}

	public boolean slotHasEmptyComb(int index)
	{
		return slotHasHoneyComb(index, HoneyCombExpect.EMPTY);
	}

	public int countCombsOfType(HoneyCombExpect type)
	{
		int count = 0;
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null)
			{
				if (slotHasHoneyComb(i, type))
				{
					count++;
				}
			}
		}
		return count;
	}

	//counts filled honeycombs only
	public int countHoney()
	{
		return countCombsOfType(HoneyCombExpect.FILLED);
	}

	public int countEmptyCombs()
	{
		return countCombsOfType(HoneyCombExpect.EMPTY);
	}

	//counts both empty and filled honeycombs
	public int countCombs()
	{
		return countCombsOfType(HoneyCombExpect.ANY);
	}

	public int getHoneyCombMax()
	{
		return honeyCombSlotIds.length;
	}

	public int countBees()
	{
		final ItemStack stack = getStackInSlot(ContainerBeeBox.SlotId.BEE);
		if (stack == null) return 0;
		return stack.stackSize;
	}

	public boolean hasBees()
	{
		return countBees() != 0;
	}

	public boolean hasMaxBees()
	{
		// in the case they use a mod that overrides the maximum item stack
		return countBees() >= 64;
	}

	public boolean isHoneyEnough(int size)
	{
		return countHoney() >= size;
	}

	public ItemStack getBeeStack()
	{
		return getStackInSlot(ContainerBeeBox.SlotId.BEE);
	}

	private void setBeeStack(ItemStack itemstack)
	{
		setInventorySlotContents(ContainerBeeBox.SlotId.BEE, itemstack);
		markDirtyAndUpdate();
	}

	public void spawnBee()
	{
		final ItemStack beestack = getBeeStack();
		if (beestack == null)
		{
			// Put a bee in the slot if we have none currently
			setBeeStack(GrowthCraftBees.items.bee.asStack());
		}
		else
		{
			// Ensure that the item in the slot IS a bee, and prevent duplication
			if (BeesRegistry.instance().isItemBee(beestack))
			{
				setBeeStack(ItemUtils.increaseStack(beestack));
			}
		}
	}

	public void spawnHoneyCombs(int n)
	{
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (n <= 0) break;
			final ItemStack stack = getStackInSlot(i);
			if (stack == null)
			{
				setInventorySlotContents(i, GrowthCraftBees.items.honeyCombEmpty.asStack());
				markDirtyAndUpdate();
				n--;
			}
		}
	}

	public void spawnHoneyComb()
	{
		spawnHoneyCombs(1);
	}

	public boolean decreaseHoney(int count)
	{
		boolean shouldMark = false;
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (count <= 0) break;
			if (slotHasHoneyComb(i, HoneyCombExpect.FILLED))
			{
				final ItemStack stack = getStackInSlot(i);
				final ItemStack result = BeesRegistry.instance().getEmptyHoneyComb(stack);
				setInventorySlotContents(i, result != null ? result.copy() : null);
				count--;
				shouldMark = true;
			}
		}
		if (shouldMark)
		{
			markDirtyAndUpdate();
			return true;
		}
		return false;
	}

	public boolean fillHoneyCombs(int count)
	{
		boolean shouldMark = false;
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (count <= 0) break;
			final ItemStack stack = getStackInSlot(i);
			if (stack != null && slotHasEmptyComb(i))
			{
				final ItemStack resultStack = BeesRegistry.instance().getFilledHoneyComb(stack);
				if (resultStack != null)
				{
					setInventorySlotContents(i, resultStack.copy());
				}
				count--;
				shouldMark = true;
			}
		}
		if (shouldMark)
		{
			markDirtyAndUpdate();
			return true;
		}
		return false;
	}

	public void fillHoneyComb()
	{
		fillHoneyCombs(1);
	}

	@EventHandler(type=EventHandler.EventType.NBT_READ)
	public void readFromNBT_BeeBox(NBTTagCompound nbt)
	{
		beeBox.readFromNBT(nbt, "bee_box");
		if (nbt.hasKey("time"))
		{
			beeBox.setBonusTime(nbt.getShort("time"));
		}
	}

	@EventHandler(type=EventHandler.EventType.NBT_WRITE)
	public void writeToNBT_BeeBox(NBTTagCompound nbt)
	{
		nbt.setInteger("BeeBox.version", beeBoxVersion);
		beeBox.writeToNBT(nbt, "bee_box");
	}

	/************
	 * HOPPER
	 ************/
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (index == ContainerBeeBox.SlotId.BEE)
		{
			return BeesRegistry.instance().isItemBee(stack);
		}
		else
		{
			return BeesRegistry.instance().isItemHoneyComb(stack);
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 1 ? beeSlotIds : honeyCombSlotIds;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return true;
	}

	@Override
	public boolean tryPlaceItem(IItemHandler.Action action, EntityPlayer player, ItemStack stack)
	{
		if (IItemHandler.Action.RIGHT != action) return false;
		if (stack != null)
		{
			final Item item = stack.getItem();
			if (item == Items.flower_pot)
			{
				if (isHoneyEnough(6))
				{
					ItemUtils.addStackToPlayer(GrowthCraftBees.items.honeyJar.asStack(), player, worldObj, xCoord, yCoord, zCoord, false);
					ItemUtils.consumeStackOnPlayer(stack, player);
					decreaseHoney(6);
					return true;
				}
			}
			else if (item == Items.dye)
			{
				int time = 0;
				if (stack.getItemDamage() == EnumDye.PINK.meta)
				{
					time = 9600;
				}
				else if (stack.getItemDamage() == EnumDye.MAGENTA.meta)
				{
					time = 4800;
				}
				if (time > 0)
				{
					setTime(time);
					worldObj.playAuxSFX(AuxFX.BONEMEAL, xCoord, yCoord, zCoord, 0);
					ItemUtils.consumeStackOnPlayer(stack, player);
					markDirtyAndUpdate();
				}
				return true;
			}
			else if (item == Items.glass_bottle)
			{
				if (GrowthCraftBees.fluids.honey != null && isHoneyEnough(2))
				{
					final ItemStack result = GrowthCraftBees.fluids.honey.asBottleItemStack();
					if (result != null)
					{
						ItemUtils.addStackToPlayer(result, player, worldObj, xCoord, yCoord, zCoord, false);
						ItemUtils.decrPlayerCurrentInventorySlot(player, 1);
						decreaseHoney(2);
						return true;
					}
				}
			}
			else if (item == Items.bucket)
			{
				if (GrowthCraftBees.fluids.honey != null && isHoneyEnough(6))
				{
					final ItemStack result = GrowthCraftBees.fluids.honey.asBucketItemStack();
					if (result != null)
					{
						ItemUtils.addStackToPlayer(result, player, worldObj, xCoord, yCoord, zCoord, false);
						ItemUtils.decrPlayerCurrentInventorySlot(player, 1);
						decreaseHoney(6);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean tryTakeItem(IItemHandler.Action action, EntityPlayer player, ItemStack onHand)
	{
		return false;
	}
}
