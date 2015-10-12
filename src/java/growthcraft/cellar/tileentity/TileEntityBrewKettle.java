package growthcraft.cellar.tileentity;

import growthcraft.api.cellar.BrewRegistry;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.container.ContainerBrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.utils.NBTHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityBrewKettle extends TileEntity implements ISidedInventory, IFluidHandler
{
	// Other Vars.
	protected float residue;
	protected int time;
	protected boolean update;

	private ItemStack[] invSlots = new ItemStack[2];
	private int maxCap = GrowthCraftCellar.getConfig().brewKettleMaxCap;
	private int[] maxCaps = new int[] {maxCap, maxCap};
	private CellarTank[] tank = new CellarTank[] {new CellarTank(this.maxCaps[0], this), new CellarTank(this.maxCaps[1], this)};
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
			if (this.canBrew())
			{
				++this.time;

				if (this.time == CellarRegistry.instance().brew().getBrewingTime(getFluidStack(0), this.invSlots[0]))
				{
					this.time = 0;
					this.brewItem();
				}
			}
			else
			{
				this.time = 0;
			}

			update = true;
		}
	}

	private void sendUpdate()
	{
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	private void debugMsg()
	{
		if (this.worldObj.isRemote)
		{
			System.out.println("CLIENT: " + getFluidAmount(0) + " " + getFluidAmount(1));
		}
		if (!this.worldObj.isRemote)
		{
			System.out.println("SERVER: " + getFluidAmount(0) + " " + getFluidAmount(1));
		}
	}

	public boolean canBrew()
	{
		if (!hasFire()) return false;
		if (this.invSlots[0] == null) return false;
		if (this.isFluidTankFull(1)) return false;
		if (!CellarRegistry.instance().brew().isBrewingRecipe(getFluidStack(0), this.invSlots[0])) return false;
		if (this.isFluidTankEmpty(1)) return true;

		final FluidStack stack = CellarRegistry.instance().brew().getBrewingFluidStack(getFluidStack(0), this.invSlots[0]);
		return stack.isFluidEqual(getFluidStack(1));
	}

	public boolean hasFire()
	{
		return CellarRegistry.instance().isBlockHeatSource(this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord));
	}

	public void brewItem()
	{
		final BrewRegistry brew = CellarRegistry.instance().brew();
		// set spent grain
		final float f = brew.getBrewingResidue(getFluidStack(0), this.invSlots[0]);
		this.residue = this.residue + f;
		if (this.residue >= 1.0F)
		{
			this.residue = this.residue - 1.0F;

			if (this.grainBool())
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

		final FluidStack fluidstack = brew.getBrewingFluidStack(getFluidStack(0), this.invSlots[0]);
		final int amount = brew.getBrewingAmount(getFluidStack(0), this.invSlots[0]);
		fluidstack.amount = amount;
		this.tank[1].fill(fluidstack, true);
		this.tank[0].drain(amount, true);

		// subtract from the itemstack
		--this.invSlots[0].stackSize;

		if (this.invSlots[0].stackSize <= 0)
		{
			this.invSlots[0] = null;
		}

		sendUpdate();
	}

	private boolean grainBool()
	{
		if (this.invSlots[1] == null) return true;
		if (!this.invSlots[1].isItemEqual(GrowthCraftCellar.residue)) return false;
		final int result = invSlots[1].stackSize + GrowthCraftCellar.residue.stackSize;
		return result <= getInventoryStackLimit() && result <= GrowthCraftCellar.residue.getMaxStackSize();
	}

	@SideOnly(Side.CLIENT)
	public int getBrewProgressScaled(int par1)
	{
		if (this.canBrew())
		{
			return this.time * par1 / CellarRegistry.instance().brew().getBrewingTime(getFluidStack(0), this.invSlots[0]);
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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

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
		// INVENTORY
		this.invSlots = new ItemStack[this.getSizeInventory()];
		NBTHelper.readInventorySlotsFromNBT(nbt.getTagList("items", NBTHelper.NBTType.COMPOUND), invSlots);

		//TANK
		readTankFromNBT(nbt);

		//NAME
		if (nbt.hasKey("name"))
		{
			this.name = nbt.getString("name");
		}

		this.time = nbt.getShort("time");
		this.residue = nbt.getFloat("grain");
	}

	protected void readTankFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < this.tank.length; i++)
		{
			this.tank[i] = new CellarTank(this.maxCaps[i], this);
			if (nbt.hasKey("Tank" + i))
			{
				this.tank[i].readFromNBT(nbt.getCompoundTag("Tank" + i));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		// INVENTORY
		nbt.setTag("items", NBTHelper.writeInventorySlotsToNBT(invSlots));

		// TANKS
		writeTankToNBT(nbt);

		// NAME
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}

		nbt.setShort("time", (short)this.time);
		nbt.setFloat("grain", this.residue);
	}

	protected void writeTankToNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < this.tank.length; i++)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			this.tank[i].writeToNBT(tag);
			nbt.setTag("Tank" + i, tag);
		}
	}

	/************
	 * NAMES
	 ************/
	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.name : "container.grc.brewKettle";
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
				if (tank[0].getFluid() == null)
				{
					tank[0].setFluid(new FluidStack(v, 0));
				}
				else
				{
					tank[0].setFluid(new FluidStack(v, tank[0].getFluid().amount));
				}
				break;
			case 2:
				if (tank[0].getFluid() == null)
				{
					tank[0].setFluid(new FluidStack(FluidRegistry.WATER, v));
				}
				else
				{
					tank[0].getFluid().amount = v;
				}
				break;
			case 3:
				if (FluidRegistry.getFluid(v) == null)
				{
					return;
				}
				if (tank[1].getFluid() == null)
				{
					tank[1].setFluid(new FluidStack(v, 0));
				}
				else
				{
					tank[1].setFluid(new FluidStack(v, tank[1].getFluid().amount));
				}
				break;
			case 4:
				if (tank[1].getFluid() == null)
				{
					tank[1].setFluid(new FluidStack(FluidRegistry.WATER, v));
				}
				else
				{
					tank[1].getFluid().amount = v;
				}
				break;
			default:
				break;
		}
	}

	public void sendGUINetworkData(ContainerBrewKettle container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, 0, time);
		iCrafting.sendProgressBarUpdate(container, 1, tank[0].getFluid() != null ? tank[0].getFluid().getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, 2, tank[0].getFluid() != null ? tank[0].getFluid().amount : 0);
		iCrafting.sendProgressBarUpdate(container, 3, tank[1].getFluid() != null ? tank[1].getFluid().getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, 4, tank[1].getFluid() != null ? tank[1].getFluid().amount : 0);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		final NBTTagCompound nbt = new NBTTagCompound();
		writeTankToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		readTankFromNBT(packet.func_148857_g());
		this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		final int f = this.tank[0].fill(resource, doFill);
		if (f > 0)
		{
			sendUpdate();
		}
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(this.tank[1].getFluid()))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		final FluidStack d = this.tank[1].drain(maxDrain, doDrain);
		if (d != null)
		{
			sendUpdate();
		}
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
		return new FluidTankInfo[] { this.tank[0].getInfo(), this.tank[1].getInfo() };
	}

	public int getFluidAmountScaled(int par1, int slot)
	{
		return this.getFluidAmount(slot) * par1 / this.maxCaps[slot];
	}

	public boolean isFluidTankFilled(int slot)
	{
		return this.getFluidAmount(slot) > 0;
	}

	public boolean isFluidTankFull(int slot)
	{
		return this.getFluidAmount(slot) == this.tank[slot].getCapacity();
	}

	public boolean isFluidTankEmpty(int slot)
	{
		return this.getFluidAmount(slot) == 0;
	}

	public int getFluidAmount(int slot)
	{
		return this.tank[slot].getFluidAmount();
	}

	public CellarTank getFluidTank(int slot)
	{
		return this.tank[slot];
	}

	public FluidStack getFluidStack(int slot)
	{
		return this.tank[slot].getFluid();
	}

	public Fluid getFluid(int slot)
	{
		return getFluidStack(slot).getFluid();
	}

	public void clearTank(int slot)
	{
		this.tank[slot].setFluid(null);

		sendUpdate();
	}

	public void switchTanks()
	{
		FluidStack f0 = null;
		FluidStack f1 = null;
		if (this.getFluidStack(0) != null)
		{
			f0 = this.getFluidStack(0).copy();
		}
		if (this.getFluidStack(1) != null)
		{
			f1 = this.getFluidStack(1).copy();
		}
		this.clearTank(0);
		this.clearTank(1);
		this.getFluidTank(0).fill(f1, true);
		this.getFluidTank(1).fill(f0, true);

		sendUpdate();
	}
}
