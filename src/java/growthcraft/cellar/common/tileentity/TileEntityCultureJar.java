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

import java.io.IOException;

import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.tileentity.component.TileHeatingComponent;
import growthcraft.cellar.common.tileentity.device.CultureGenerator;
import growthcraft.cellar.common.tileentity.device.YeastGenerator;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.device.DeviceProgressive;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.ITileHeatedDevice;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;

import io.netty.buffer.ByteBuf;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCultureJar extends TileEntityCellarDevice implements ITileHeatedDevice, ITileProgressiveDevice
{
	public static enum CultureJarDataId
	{
		YEAST_GEN_TIME,
		YEAST_GEN_TIME_MAX,
		CULTURE_GEN_TIME,
		CULTURE_GEN_TIME_MAX,
		TANK_FLUID_ID,
		TANK_FLUID_AMOUNT,
		HEAT_AMOUNT,
		UNKNOWN;

		public static final CultureJarDataId[] VALID = new CultureJarDataId[]
		{
			YEAST_GEN_TIME,
			YEAST_GEN_TIME_MAX,
			CULTURE_GEN_TIME,
			CULTURE_GEN_TIME_MAX,
			TANK_FLUID_ID,
			TANK_FLUID_AMOUNT,
			HEAT_AMOUNT
		};

		public static CultureJarDataId fromInt(int i)
		{
			if (i >= 0 && i <= VALID.length) return VALID[i];
			return UNKNOWN;
		}
	}

	private static final int[] accessibleSlots = new int[] { 0 };
	private TileHeatingComponent heatComponent;
	private CultureGenerator cultureGen;
	private YeastGenerator yeastGen;
	private int jarDeviceState;

	public TileEntityCultureJar()
	{
		super();
		this.heatComponent = new TileHeatingComponent(this, 0.0f);
		this.cultureGen = new CultureGenerator(this, heatComponent, 0, 0);
		this.yeastGen = new YeastGenerator(this, 0, 0);
		this.yeastGen.setTimeMax(GrowthCraftCellar.getConfig().cultureJarTimeMax);
		this.yeastGen.setConsumption(GrowthCraftCellar.getConfig().cultureJarConsumption);
	}

	public boolean isHeated()
	{
		return cultureGen.isHeated();
	}

	public float getHeatMultiplier()
	{
		return cultureGen.getHeatMultiplier();
	}

	public boolean isCulturing()
	{
		return jarDeviceState == 1;
	}

	private DeviceProgressive getActiveDevice()
	{
		if (cultureGen.isHeated())
		{
			return cultureGen;
		}
		return yeastGen;
	}

	private DeviceProgressive getActiveClientDevice()
	{
		if (jarDeviceState == 1)
		{
			return cultureGen;
		}
		return yeastGen;
	}

	@Override
	protected FluidTank[] createTanks()
	{
		final int maxTankCap = GrowthCraftCellar.getConfig().cultureJarMaxCap;
		return new FluidTank[] { new CellarTank(maxTankCap, this) };
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 1);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.CultureJar";
	}

	protected void markForFluidUpdate()
	{
		// Ferment Jars need to update their rendering state when a fluid
		// changes, most of the other cellar blocks are unaffected by this
		markForBlockUpdate();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessibleSlots;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return index == 0;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return false;
	}

	@Override
	protected int doFill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return fillFluidTank(0, resource, doFill);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return drainFluidTank(0, maxDrain, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(getFluidTank(0).getFluid()))
		{
			return null;
		}
		return doDrain(from, resource.amount, doDrain);
	}

	@Override
	protected void updateDevice()
	{
		heatComponent.update();
		final int lastState = jarDeviceState;
		final DeviceProgressive prog = getActiveDevice();
		if (prog == cultureGen)
		{
			this.jarDeviceState = 1;
			yeastGen.resetTime();
		}
		else
		{
			this.jarDeviceState = 0;
			cultureGen.resetTime();
		}
		getActiveDevice().update();
		if (jarDeviceState != lastState)
		{
			GrowthCraftCellar.getLogger().info("Jar changed device state %d, {%s}", jarDeviceState, getActiveDevice());
			markForBlockUpdate();
		}
	}

	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		super.receiveGUINetworkData(id, v);
		switch (CultureJarDataId.fromInt(id))
		{
			case YEAST_GEN_TIME:
				yeastGen.setTime(v);
				break;
			case YEAST_GEN_TIME_MAX:
				yeastGen.setTimeMax(v);
				break;
			case CULTURE_GEN_TIME:
				cultureGen.setTime(v);
				break;
			case CULTURE_GEN_TIME_MAX:
				cultureGen.setTimeMax(v);
				break;
			case TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(0));
				if (result != null) getFluidTank(0).setFluid(result);
				break;
			case TANK_FLUID_AMOUNT:
				getFluidTank(0).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(0), v));
				break;
			case HEAT_AMOUNT:
				heatComponent.setHeatMultiplier((float)v / (float)0x7FFF);
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		super.sendGUINetworkData(container, iCrafting);
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.YEAST_GEN_TIME.ordinal(), yeastGen.getTime());
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.YEAST_GEN_TIME_MAX.ordinal(), yeastGen.getTimeMax());
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.CULTURE_GEN_TIME.ordinal(), cultureGen.getTime());
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.CULTURE_GEN_TIME_MAX.ordinal(), cultureGen.getTimeMax());
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.TANK_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.TANK_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
		iCrafting.sendProgressBarUpdate(container, CultureJarDataId.HEAT_AMOUNT.ordinal(), (int)(heatComponent.getHeatMultiplier() * 0x7FFF));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		yeastGen.readFromNBT(nbt, "yeastgen");
		cultureGen.readFromNBT(nbt, "culture_gen");
		heatComponent.readFromNBT(nbt, "heat_component");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		yeastGen.writeToNBT(nbt, "yeastgen");
		cultureGen.writeToNBT(nbt, "culture_gen");
		heatComponent.writeToNBT(nbt, "heat_component");
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_YeastGen(ByteBuf stream) throws IOException
	{
		this.jarDeviceState = stream.readInt();
		yeastGen.readFromStream(stream);
		cultureGen.readFromStream(stream);
		heatComponent.readFromStream(stream);
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public void writeToStream_YeastGen(ByteBuf stream) throws IOException
	{
		stream.writeInt(jarDeviceState);
		yeastGen.writeToStream(stream);
		cultureGen.writeToStream(stream);
		heatComponent.writeToStream(stream);
	}

	public int getProgressScaled(int scale)
	{
		return getActiveClientDevice().getProgressScaled(scale);
	}

	public int getHeatScaled(int scale)
	{
		return (int)(scale * getHeatMultiplier());
	}

	public float getDeviceProgress()
	{
		return getActiveClientDevice().getProgress();
	}
}
