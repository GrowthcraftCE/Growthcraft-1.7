/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.milk.common.tileentity;

import java.io.IOException;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.milk.churn.IChurnRecipe;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.core.common.inventory.AccesibleSlots;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.device.DeviceInventorySlot;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.feature.IItemHandler;
import growthcraft.core.common.tileentity.GrcTileDeviceBase;
import growthcraft.core.util.ItemUtils;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityButterChurn extends GrcTileDeviceBase implements IItemHandler
{
	public static enum WorkState
	{
		NONE,
		CHURN,
		PRODUCE;
	}

	private static AccesibleSlots accessibleSlots = new AccesibleSlots(new int[][] {
		{ 0 },
		{   },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 }
	});

	@SideOnly(Side.CLIENT)
	public float animProgress;
	@SideOnly(Side.CLIENT)
	public int animDir;

	private int shaftState;
	private int churns;
	private DeviceFluidSlot inputFluidSlot = new DeviceFluidSlot(this, 0);
	private DeviceFluidSlot outputFluidSlot = new DeviceFluidSlot(this, 1);
	private DeviceInventorySlot outputInventorySlot = new DeviceInventorySlot(this, 0);

	@Override
	protected FluidTank[] createTanks()
	{
		return new FluidTank[] {
			// cream
			new FluidTank(1000),
			// buttermilk
			new FluidTank(1000)
		};
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 1);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grcmilk.ButterChurn";
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessibleSlots.slotsAt(side);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return accessibleSlots.sideContains(side, index);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (worldObj.isRemote)
		{
			final float step = 1.0f / 5.0f;
			if (shaftState == 0)
			{
				this.animDir = -1;
			}
			else
			{
				this.animDir = 1;
			}

			if (animDir > 0 && animProgress < 1.0f || animDir < 0 && animProgress > 0)
			{
				this.animProgress = MathHelper.clamp_float(this.animProgress + step * animDir, 0.0f, 1.0f);
			}
		}
	}

	private IChurnRecipe getWorkingRecipe()
	{
		final FluidStack stack = inputFluidSlot.get();
		if (stack != null)
		{
			final IChurnRecipe recipe = MilkRegistry.instance().churn().getRecipe(stack);
			return recipe;
		}
		return null;
	}

	public WorkState doWork()
	{
		WorkState state = WorkState.NONE;
		final IChurnRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			state = WorkState.CHURN;
			this.churns++;
			if (churns >= recipe.getChurns())
			{
				this.churns = 0;
				inputFluidSlot.consume(recipe.getInputFluidStack(), true);
				outputFluidSlot.fill(recipe.getOutputFluidStack(), true);
				outputInventorySlot.increaseStack(recipe.getOutputItemStack());
				state = WorkState.PRODUCE;
			}

			if (shaftState == 0)
			{
				this.shaftState = 1;
			}
			else
			{
				this.shaftState = 0;
			}
			markForUpdate();
		}
		else
		{
			if (shaftState != 0)
			{
				this.shaftState = 0;
				markForUpdate();
			}
			this.churns = 0;
		}
		return state;
	}

	private DeviceFluidSlot getActiveFluidSlot()
	{
		if (outputFluidSlot.hasContent()) return outputFluidSlot;
		return inputFluidSlot;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return MilkRegistry.instance().churn().isFluidIngredient(fluid);
	}

	/**
	 * @param dir - direction to drain from
	 * @param amount - amount of fluid to drain
	 * @param doDrain - should any draining actually take place?
	 * @return fluid drained
	 */
	@Override
	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain)
	{
		if (dir == ForgeDirection.UP) return null;
		return getActiveFluidSlot().consume(amount, doDrain);
	}

	/**
	 * @param dir - direction to drain from
	 * @param stack - fluid stack (as filter) to drain
	 * @param doDrain - should any draining actually take place?
	 * @return fluid drained
	 */
	@Override
	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		if (dir == ForgeDirection.UP) return null;
		final DeviceFluidSlot fluidSlot = getActiveFluidSlot();
		if (FluidTest.areStacksEqual(fluidSlot.get(), stack))
		{
			return fluidSlot.consume(stack, doDrain);
		}
		return null;
	}

	/**
	 * When filling the Churn, only CREAM fluids are accepted
	 *
	 * @param dir - direction being filled from
	 * @param stack - fluid to fill with
	 * @param doFill - should we actually fill this?
	 * @return how much fluid was actually used
	 */
	@Override
	protected int doFill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		if (dir == ForgeDirection.UP) return 0;
		int result = 0;

		if (MilkRegistry.instance().churn().isFluidIngredient(stack))
		{
			result = inputFluidSlot.fill(stack, doFill);
		}

		return result;
	}

	/**
	 * Items cannot be placed into a Butter Churn.
	 *
	 * @param player - player placing the item
	 * @param stack - item stack being placed
	 * @return false
	 */
	@Override
	public boolean tryPlaceItem(IItemHandler.Action action, EntityPlayer player, ItemStack stack)
	{
		return false;
	}

	/**
	 * Attempts to remove the item in the butter churn
	 *
	 * @param player - player trying to remove the butter
	 * @param onHand - hand must be empty
	 * @return true, the item was removed, false otherwise
	 */
	@Override
	public boolean tryTakeItem(IItemHandler.Action action, EntityPlayer player, ItemStack onHand)
	{
		if (IItemHandler.Action.RIGHT != action) return false;
		final ItemStack stack = outputInventorySlot.yank();
		if (stack != null)
		{
			ItemUtils.addStackToPlayer(stack, player, false);
			return true;
		}
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NBT_READ)
	public void readFromNBT_ButterChurn(NBTTagCompound nbt)
	{
		this.shaftState = nbt.getInteger("shaft_state");
		this.churns = nbt.getInteger("churns");
	}

	@EventHandler(type=EventHandler.EventType.NBT_WRITE)
	public void writeToNBT_ButterChurn(NBTTagCompound nbt)
	{
		nbt.setInteger("shaft_state", shaftState);
		nbt.setInteger("churns", churns);
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_ButterChurn(ByteBuf stream) throws IOException
	{
		this.shaftState = stream.readInt();
		this.churns = stream.readInt();
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_ButterChurn(ByteBuf stream) throws IOException
	{
		stream.writeInt(shaftState);
		stream.writeInt(churns);
		return false;
	}
}
