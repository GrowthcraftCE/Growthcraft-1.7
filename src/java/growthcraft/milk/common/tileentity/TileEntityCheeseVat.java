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

import growthcraft.api.core.util.FluidTest;
import growthcraft.api.milk.util.MilkTest;
import growthcraft.core.common.tileentity.GrcTileEntityDeviceBase;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCheeseVat extends GrcTileEntityDeviceBase
{
	private static int[][] accessibleSlots = {
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 }
	};

	@Override
	protected FluidTank[] createTanks()
	{
		return new FluidTank[] {
			// milk
			new FluidTank(5000),
			// rennet
			new FluidTank(333),
			// whey
			new FluidTank(1000)
		};
	}

	public int getVatFluidCapacity()
	{
		return getFluidTank(0).getCapacity() + getFluidTank(2).getCapacity();
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 3);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grcmilk.CheeseVat";
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessibleSlots[side];
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return true;
	}

	@Override
	protected void updateDevice()
	{

	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain)
	{
		return drainFluidTank(2, amount, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		if (!FluidTest.areStacksEqual(getFluidStack(2), stack)) return null;
		return doDrain(dir, stack.amount, doDrain);
	}

	@Override
	protected int doFill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		int result = 0;

		if (MilkTest.isMilk(stack))
		{
			result = fillFluidTank(0, stack, doFill);
		}
		else if (FluidTest.isValidAndExpected(GrowthCraftMilk.fluids.rennet.getFluid(), stack))
		{
			result = fillFluidTank(1, stack, doFill);
		}

		return result;
	}

	@Override
	protected void markForFluidUpdate()
	{
		markForBlockUpdate();
	}
}
