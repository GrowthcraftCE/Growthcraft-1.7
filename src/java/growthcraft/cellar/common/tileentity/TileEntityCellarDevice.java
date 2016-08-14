/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.cellar.common.tileentity;

import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.core.common.tileentity.feature.IGuiNetworkSync;
import growthcraft.core.common.tileentity.feature.IInteractionObject;
import growthcraft.core.common.tileentity.GrcTileEntityDeviceBase;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class TileEntityCellarDevice extends GrcTileEntityDeviceBase implements IGuiNetworkSync, IInteractionObject
{
	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		// 50 is more than generous right?
		if (id >= 50)
		{
			final int tankIndex = (id - 50) / 3;
			switch ((id - 50) % 3)
			{
				// Fluid ID
				case 0:
				{
					final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(tankIndex));
					if (result != null) getFluidTank(tankIndex).setFluid(result);
				} break;
				// Fluid amounts CAN exceed a 16bit integer, in order to handle the capacity, the value is split across 2 data points, by high and low bytes
				// Fluid Amount (LOW Bytes)
				case 1:
				{
					final int t = getFluidAmount(tankIndex);
					setFluidStack(tankIndex, FluidUtils.updateFluidStackAmount(getFluidStack(tankIndex), (t & 0xFFFF0000) | v));
				} break;
				// Fluid Amount (HIGH Bytes)
				case 2:
				{
					final int t = getFluidAmount(tankIndex);
					getFluidTank(tankIndex).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(tankIndex), (t & 0xFFFF) | (v << 16)));
				} break;
				default:
					break;
			}
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		int i = 0;
		for (FluidTank tank : getFluidTanks())
		{
			final int offset = 50 + i * 3;
			final FluidStack fluid = getFluidStack(i);
			iCrafting.sendProgressBarUpdate(container, offset, fluid != null ? fluid.getFluidID() : 0);
			iCrafting.sendProgressBarUpdate(container, offset + 1, fluid != null ? (fluid.amount & 0xFFFF) : 0);
			iCrafting.sendProgressBarUpdate(container, offset + 2, fluid != null ? ((fluid.amount >> 16) & 0xFFFF) : 0);
			i++;
		}
	}
}
