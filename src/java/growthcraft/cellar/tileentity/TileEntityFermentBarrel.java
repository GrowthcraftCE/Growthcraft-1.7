package growthcraft.cellar.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.container.ContainerFermentBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFermentBarrel extends TileEntity implements ISidedInventory, IFluidHandler
{
	// Constants
	private ItemStack[] invSlots   = new ItemStack[1];
	private int         maxCap     = 3000;
	private CellarTank   tank      = new CellarTank(this.maxCap, this);
	private int timemax  = GrowthCraftCellar.ferment_speed;

	// Other Vars.
	private String  name;
	protected int   time     = 0;
	protected boolean update = false;

	/************
	 * UPDATE
	 ************/	
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
			if (this.canFerment())
			{
				++this.time;

				if (this.time == this.timemax)
				{
					this.time = 0;
					this.fermentItem();
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

	private boolean canFerment()
	{
		if (this.invSlots[0] == null) return false;
		if (isFluidTankEmpty()) return false;
		if (!CellarRegistry.instance().isFluidBooze(getFluid()))return false;

		Item item = this.invSlots[0].getItem();
		int meta = CellarRegistry.instance().getBoozeIndex(getFluid());

		if (meta == 3)
		{
			return  item == Items.glowstone_dust;
		}
		else if (meta == 2)
		{
			return item == Items.redstone;
		}
		else if (meta == 1)
		{
			return item == Items.redstone || item == Items.glowstone_dust;
		}
		else if (meta == 0)
		{
			return item == Items.nether_wart;
		}

		return false;
	}

	public void fermentItem()
	{
		Item item = this.invSlots[0].getItem();
		int meta = CellarRegistry.instance().getBoozeIndex(getFluid());
		Fluid[] fluidArray = CellarRegistry.instance().getBoozeArray(getFluid());

		if (meta == 0 && item == Items.nether_wart)
		{
			this.tank.setFluid(new FluidStack(fluidArray[1].getID(), getFluidStack().amount, getFluidStack().tag));
		}
		else if ((meta == 1 || meta == 3) && item == Items.glowstone_dust)
		{
			this.tank.setFluid(new FluidStack(fluidArray[2].getID(), getFluidStack().amount, getFluidStack().tag));
		}			
		else if ((meta == 1 || meta == 2) && item == Items.redstone)
		{
			this.tank.setFluid(new FluidStack(fluidArray[3].getID(), getFluidStack().amount, getFluidStack().tag));
		}

		--this.invSlots[0].stackSize;

		if (this.invSlots[0].stackSize <= 0)
		{
			this.invSlots[0] = null;
		}

		//this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	public int getFermentProgressScaled(int par1)
	{
		if (this.canFerment())
		{
			return this.time * par1 / this.timemax;
		}

		return 0;
	}

	public int getTime(){return this.time;}
	public int getTimeMax(){return this.timemax;}

	public int getBoozeMeta(){ return CellarRegistry.instance().getBoozeIndex(this.getFluid());}

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
		Item item = itemstack.getItem();
		return index == 0 ? item == Items.nether_wart || item == Items.redstone || item == Items.glowstone_dust : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] {0};
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

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		//INVENTORY
		NBTTagList inv_tags = nbt.getTagList("items", 10);
		this.invSlots = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < inv_tags.tagCount(); ++i)
		{
			NBTTagCompound inv_compound = inv_tags.getCompoundTagAt(i);
			byte b0 = inv_compound.getByte("Slot");

			if (b0 >= 0 && b0 < this.invSlots.length)
			{
				this.invSlots[b0] = ItemStack.loadItemStackFromNBT(inv_compound);
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
		NBTTagList inv_tags = new NBTTagList();
		for (int i = 0; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] != null)
			{
				NBTTagCompound inv_tagcompound = new NBTTagCompound();
				inv_tagcompound.setByte("Slot", (byte)i);
				this.invSlots[i].writeToNBT(inv_tagcompound);
				inv_tags.appendTag(inv_tagcompound);
			}
		}
		nbt.setTag("items", inv_tags);

		//TANKS
		writeTankToNBT(nbt);

		//NAME
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}

		nbt.setShort("time", (short)this.time);
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
		return this.hasCustomInventoryName() ? this.name : "container.grc.fermentBarrel";
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
	public void getGUINetworkData(int id, int v) 
	{
		switch (id) 
		{
		case 0:
			time = v;
			break;
		case 1:
			if (tank.getFluid() == null) 
			{
				tank.setFluid(new FluidStack(v, 0));
			} else 
			{
				tank.getFluid().fluidID = v;
			}
			break;
		case 2:
			if (tank.getFluid() == null) 
			{
				tank.setFluid(new FluidStack(0, v));
			} else 
			{
				tank.getFluid().amount = v;
			}
			break;
		}
	}

	public void sendGUINetworkData(ContainerFermentBarrel container, ICrafting iCrafting) 
	{
		iCrafting.sendProgressBarUpdate(container, 0, time);
		iCrafting.sendProgressBarUpdate(container, 1, tank.getFluid() != null ? tank.getFluid().fluidID : 0);
		iCrafting.sendProgressBarUpdate(container, 2, tank.getFluid() != null ? tank.getFluid().amount : 0);
	}

	/*@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeTankToNBT(nbt);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 5, nbt);
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
		int f = this.tank.fill(resource, doFill);
		//this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		return f;
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
		return true;
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
