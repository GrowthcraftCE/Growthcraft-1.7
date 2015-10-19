package growthcraft.cellar.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.container.ContainerFermentBarrel;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.utils.NBTHelper;
import growthcraft.core.utils.ItemUtils;
import growthcraft.api.cellar.FluidUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFermentBarrel extends TileEntity implements ISidedInventory, IFluidHandler
{
	public static class FermentBarrelDataID
	{
		public static final int TIME = 0;
		public static final int TANK_FLUID_ID = 1;
		public static final int TANK_FLUID_AMOUNT = 2;

		private FermentBarrelDataID() {}
	}

	// Constants
	private static final int[] accessableSlotIds = new int[] {0};

	// Other Vars.
	protected int time;
	protected boolean update;
	private ItemStack[] invSlots = new ItemStack[1];
	private int maxCap = GrowthCraftCellar.getConfig().fermentBarrelMaxCap;
	private CellarTank tank = new CellarTank(this.maxCap, this);
	private int timemax = GrowthCraftCellar.getConfig().fermentSpeed;
	private String name;

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
		if (!CellarRegistry.instance().booze().isFluidBooze(getFluid()))return false;

		final Item item = this.invSlots[0].getItem();
		final int meta = CellarRegistry.instance().booze().getBoozeIndex(getFluid());

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
		final Item item = this.invSlots[0].getItem();
		final int meta = CellarRegistry.instance().booze().getBoozeIndex(getFluid());
		final Fluid[] fluidArray = CellarRegistry.instance().booze().getBoozeArray(getFluid());

		if (meta == 0 && item == Items.nether_wart)
		{
			this.tank.setFluid(new FluidStack(fluidArray[1], getFluidStack().amount, getFluidStack().tag));
		}
		else if ((meta == 1 || meta == 3) && item == Items.glowstone_dust)
		{
			this.tank.setFluid(new FluidStack(fluidArray[2], getFluidStack().amount, getFluidStack().tag));
		}
		else if ((meta == 1 || meta == 2) && item == Items.redstone)
		{
			this.tank.setFluid(new FluidStack(fluidArray[3], getFluidStack().amount, getFluidStack().tag));
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

	public int getBoozeMeta(){ return CellarRegistry.instance().booze().getBoozeIndex(this.getFluid());}

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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		final Item item = itemstack.getItem();
		return index == 0 ? item == Items.nether_wart || item == Items.redstone || item == Items.glowstone_dust : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessableSlotIds;
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

	/**
	 * @param nbt - nbt data to load
	 */
	protected void readInventorySlotsFromNBT(NBTTagCompound nbt)
	{
		this.invSlots = ItemUtils.clearInventorySlots(invSlots, getSizeInventory());
		NBTHelper.readInventorySlotsFromNBT(invSlots, nbt.getTagList("items", NBTHelper.NBTType.COMPOUND));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		// INVENTORY
		readInventorySlotsFromNBT(nbt);

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
		// INVENTORY
		nbt.setTag("items", NBTHelper.writeInventorySlotsToNBT(invSlots));

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
		final NBTTagCompound tag = new NBTTagCompound();
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

	/**
	 * @param id - data id
	 * @param v - value
	 */
	public void getGUINetworkData(int id, int v)
	{
		switch (id)
		{
			case FermentBarrelDataID.TIME:
				time = v;
				break;
			case FermentBarrelDataID.TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, tank.getFluid());
				if (result != null) tank.setFluid(result);
				break;
			case FermentBarrelDataID.TANK_FLUID_AMOUNT:
				tank.setFluid(FluidUtils.updateFluidStackAmount(tank.getFluid(), v));
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	public void sendGUINetworkData(ContainerFermentBarrel container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME, time);
		final FluidStack fluid = tank.getFluid();
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_ID, fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_AMOUNT, fluid != null ? fluid.amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return this.tank.fill(resource, doFill);
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
		final FluidStack d = this.tank.drain(maxDrain, doDrain);
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
	}
}
