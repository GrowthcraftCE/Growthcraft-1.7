package growthcraft.cellar.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.container.ContainerFruitPress;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFruitPress extends TileEntity implements ISidedInventory, IFluidHandler
{
	// Constants
	private ItemStack[] invSlots = new ItemStack[2];
	private int         maxCap   = GrowthCraftCellar.getConfig().fruitPressMaxCap;
	private CellarTank  tank     = new CellarTank(this.maxCap, this);

	// Other Vars.
	private String  name;
	protected float pomace   = 0.0F;
	protected int   time     = 0;
	protected boolean update = false;

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

				if (this.time == CellarRegistry.instance().getPressingTime(this.invSlots[0]))
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
		int m = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);

		if (m == 0 || m == 1) return false;
		if (this.invSlots[0] == null) return false;
		if (getFluidAmount() == this.maxCap) return false;
		if (!CellarRegistry.instance().isPressingRecipe(this.invSlots[0])) return false;

		if (isFluidTankEmpty()) return true;

		FluidStack stack = CellarRegistry.instance().getPressingFluidStack(this.invSlots[0]);
		return stack.isFluidEqual(getFluidStack());
	}

	public void pressItem()
	{
		float f = CellarRegistry.instance().getPressingResidue(this.invSlots[0]);
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

		FluidStack fluidstack = CellarRegistry.instance().getPressingFluidStack(this.invSlots[0]);
		fluidstack.amount  = CellarRegistry.instance().getPressingAmount(this.invSlots[0]);
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
		int result = invSlots[1].stackSize + GrowthCraftCellar.residue.stackSize;
		return (result <= getInventoryStackLimit() && result <= GrowthCraftCellar.residue.getMaxStackSize());
	}

	public int getPressProgressScaled(int par1)
	{
		if (this.canPress())
		{
			return this.time * par1 / CellarRegistry.instance().getPressingTime(this.invSlots[0]);
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
			ItemStack itemstack = this.invSlots[index];
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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return index == 1 ? itemstack.getItem() == GrowthCraftCellar.residue.getItem() && itemstack.getItemDamage() == GrowthCraftCellar.residue.getItemDamage() : true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		// 0 = raw
		// 1 = residue
		return side == 0 ? new int[] {0, 1} : new int[] {0};
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

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		//INVENTORY
		NBTTagList tags = nbt.getTagList("items", 10);
		this.invSlots = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tags.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = tags.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.invSlots.length)
			{
				this.invSlots[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		//TANKS
		readTankFromNBT(nbt);

		//NAME
		if (nbt.hasKey("name"))
		{
			this.name = nbt.getString("name");
		}

		this.time = nbt.getShort("time");
		this.pomace = nbt.getFloat("pomace");
	}

	protected void readTankFromNBT(NBTTagCompound nbt)
	{
		this.tank = new CellarTank(this.maxCap, this);
		if (nbt.hasKey("Tank"))
		{
			this.tank.readFromNBT(nbt.getCompoundTag("Tank"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		//INVENTORY
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.invSlots[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("items", nbttaglist);

		//TANK
		writeTankToNBT(nbt);

		//NAME
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}

		nbt.setShort("time", (short)this.time);
		nbt.setFloat("pomace", this.pomace);
	}

	protected void writeTankToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.tank.writeToNBT(tag);
		nbt.setTag("Tank", tag);
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
			if (FluidRegistry.getFluid(v) == null) {
				return;
			}
			if (tank.getFluid() == null)
			{
				tank.setFluid(new FluidStack(v, 0));
			} else
			{
				tank.setFluid(new FluidStack(v, tank.getFluid().amount));
			}
			break;
		case 2:
			if (tank.getFluid() == null)
			{
				tank.setFluid(new FluidStack(FluidRegistry.WATER, v));
			} else
			{
				tank.getFluid().amount = v;
			}
			break;
		}
	}

	public void sendGUINetworkData(ContainerFruitPress container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, 0, time);
		iCrafting.sendProgressBarUpdate(container, 1, tank.getFluid() != null ? tank.getFluid().getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, 2, tank.getFluid() != null ? tank.getFluid().amount : 0);
	}

	/*@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeTankToNBT(nbt);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
	{
		readTankFromNBT(packet.data);
		//this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
	}*/

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
		FluidStack d = this.tank.drain(maxDrain, doDrain);
		//this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		return d;
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
		//this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}
}
