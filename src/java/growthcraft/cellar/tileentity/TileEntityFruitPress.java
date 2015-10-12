package growthcraft.cellar.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.PressingRegistry;
import growthcraft.cellar.container.ContainerFruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.utils.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFruitPress extends TileEntity implements ISidedInventory, IFluidHandler
{
	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};

	protected float pomace;
	protected int time;
	protected boolean update;

	private ItemStack[] invSlots = new ItemStack[2];
	private final int maxCap = GrowthCraftCellar.getConfig().fruitPressMaxCap;
	private CellarTank tank = new CellarTank(this.maxCap, this);
	private String name;

	/************
	 * UPDATE
	 ************/
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (update)
		{
			update = false;
			this.markDirty();
		}

		if (!this.worldObj.isRemote)
		{
			if (this.canPress())
			{
				++this.time;

				if (this.time == CellarRegistry.instance().pressing().getPressingTime(this.invSlots[0]))
				{
					this.time = 0;
					this.pressItem();
				}
			}
			else
			{
				this.time = 0;
			}

			update = true;
		}

		//debugMsg();
	}

	private void debugMsg()
	{
		if (this.worldObj.isRemote)
		{
			System.out.println("CLIENT: " + getFluidAmount());
		}
		if (!this.worldObj.isRemote)
		{
			System.out.println("SERVER: " + getFluidAmount());
		}
	}

	private boolean canPress()
	{
		final PressingRegistry pressing = CellarRegistry.instance().pressing();
		final int m = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);

		if (m == 0 || m == 1) return false;
		if (this.invSlots[0] == null) return false;
		if (getFluidAmount() == this.maxCap) return false;
		if (!pressing.isPressingRecipe(this.invSlots[0])) return false;

		if (isFluidTankEmpty()) return true;

		final FluidStack stack = pressing.getPressingFluidStack(this.invSlots[0]);
		return stack.isFluidEqual(getFluidStack());
	}

	public void pressItem()
	{
		final PressingRegistry pressing = CellarRegistry.instance().pressing();
		final float f = pressing.getPressingResidue(this.invSlots[0]);
		this.pomace = this.pomace + f;
		if (this.pomace >= 1.0F)
		{
			this.pomace = this.pomace - 1.0F;

			if (this.pomaceBool())
			{
				if (this.invSlots[1] == null)
				{
					this.invSlots[1] = GrowthCraftCellar.residue.copy();
				}
				else if (this.invSlots[1].isItemEqual(GrowthCraftCellar.residue))
				{
					this.invSlots[1].stackSize += GrowthCraftCellar.residue.stackSize;
				}
			}
		}

		final FluidStack fluidstack = pressing.getPressingFluidStack(this.invSlots[0]);
		fluidstack.amount  = pressing.getPressingAmount(this.invSlots[0]);
		this.tank.fill(fluidstack, true);

		--this.invSlots[0].stackSize;

		if (this.invSlots[0].stackSize <= 0)
		{
			this.invSlots[0] = null;
		}

		//this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	private boolean pomaceBool()
	{
		if (this.invSlots[1] == null) return true;
		if (!this.invSlots[1].isItemEqual(GrowthCraftCellar.residue)) return false;
		final int result = invSlots[1].stackSize + GrowthCraftCellar.residue.stackSize;
		return result <= getInventoryStackLimit() &&
			result <= GrowthCraftCellar.residue.getMaxStackSize();
	}

	public int getPressProgressScaled(int par1)
	{
		if (this.canPress())
		{
			return this.time * par1 / CellarRegistry.instance().pressing().getPressingTime(this.invSlots[0]);
		}

		return 0;
	}

	/************
	 * INVENTORY
	 ************/
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.invSlots[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int par2)
	{
		if (this.invSlots[index] != null)
		{
			ItemStack itemstack;

			if (this.invSlots[index].stackSize <= par2)
			{
				itemstack = this.invSlots[index];
				this.invSlots[index] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.invSlots[index].splitStack(par2);

				if (this.invSlots[index].stackSize == 0)
				{
					this.invSlots[index] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		if (this.invSlots[index] != null)
		{
			final ItemStack itemstack = this.invSlots[index];
			this.invSlots[index] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		this.invSlots[index] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		return this.invSlots.length;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
		{
			return false;
		}
		return player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		if (index == 1)
		{
			return itemstack.getItem() == GrowthCraftCellar.residue.getItem() &&
				itemstack.getItemDamage() == GrowthCraftCellar.residue.getItemDamage();
		}
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		// 0 = raw
		// 1 = residue
		return side == 0 ? rawSlotIDs : residueSlotIDs;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return side != 0 || index == 1;
	}

	protected void readTankFromNBT(NBTTagCompound nbt)
	{
		this.tank = new CellarTank(this.maxCap, this);
		if (nbt.hasKey("Tank"))
		{
			this.tank.readFromNBT(nbt.getCompoundTag("Tank"));
		}
	}

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		// INVENTORY
		this.invSlots = new ItemStack[this.getSizeInventory()];
		NBTHelper.readInventorySlotsFromNBT(invSlots, nbt.getTagList("items", NBTHelper.NBTType.COMPOUND));

		// TANKS
		readTankFromNBT(nbt);

		// NAME
		if (nbt.hasKey("name"))
		{
			this.name = nbt.getString("name");
		}

		this.time = nbt.getShort("time");
		this.pomace = nbt.getFloat("pomace");
	}

	protected void writeTankToNBT(NBTTagCompound nbt)
	{
		final NBTTagCompound tag = new NBTTagCompound();
		this.tank.writeToNBT(tag);
		nbt.setTag("Tank", tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		// INVENTORY
		nbt.setTag("items", NBTHelper.writeInventorySlotsToNBT(invSlots));

		// TANK
		writeTankToNBT(nbt);

		// NAME
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}

		nbt.setShort("time", (short)this.time);
		nbt.setFloat("pomace", this.pomace);
	}

	/************
	 * NAMES
	 ************/
	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.name : "container.grc.fruitPress";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return this.name != null && this.name.length() > 0;
	}

	public void setGuiDisplayName(String string)
	{
		this.name = string;
	}

	/************
	 * PACKETS
	 ************/

	/**
	 * @param id - data id
	 * @param v - value
	 */
	public void getGUINetworkData(int id, int v)
	{
		switch (id)
		{
			case 0:
				time = v;
				break;
			case 1:
				if (FluidRegistry.getFluid(v) == null)
				{
					return;
				}
				if (tank.getFluid() == null)
				{
					tank.setFluid(new FluidStack(v, 0));
				}
				else
				{
					tank.setFluid(new FluidStack(v, tank.getFluid().amount));
				}
				break;
			case 2:
				if (tank.getFluid() == null)
				{
					tank.setFluid(new FluidStack(FluidRegistry.WATER, v));
				}
				else
				{
					tank.getFluid().amount = v;
				}
				break;
			default:
				break;
		}
	}

	public void sendGUINetworkData(ContainerFruitPress container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, 0, time);
		iCrafting.sendProgressBarUpdate(container, 1, tank.getFluid() != null ? tank.getFluid().getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, 2, tank.getFluid() != null ? tank.getFluid().amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(this.tank.getFluid()))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return this.tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	public int getFluidAmountScaled(int scale)
	{
		return this.getFluidAmount() * scale / this.maxCap;
	}

	public boolean isFluidTankFilled()
	{
		return getFluidAmount() > 0;
	}

	public boolean isFluidTankEmpty()
	{
		return getFluidAmount() == 0;
	}

	public int getFluidAmount()
	{
		return this.tank.getFluidAmount();
	}

	public CellarTank getFluidTank()
	{
		return this.tank;
	}

	public FluidStack getFluidStack()
	{
		return this.tank.getFluid();
	}

	public Fluid getFluid()
	{
		return getFluidStack().getFluid();
	}

	public void clearTank()
	{
		this.tank.setFluid(null);
	}
}
