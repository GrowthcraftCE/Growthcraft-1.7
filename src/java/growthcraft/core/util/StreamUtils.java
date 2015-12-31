/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.core.util;

import io.netty.buffer.ByteBuf;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * Utility class for handling data streams
 */
public class StreamUtils
{
	private StreamUtils() {}

	public static void readFluidTank(ByteBuf stream, FluidTank tank)
	{
		final int capacity = stream.readInt();
		final int fluidId = stream.readInt();
		final int fluidAmount = stream.readInt();

		final Fluid fluid = fluidId > -1 ? FluidRegistry.getFluid(fluidId) : null;
		final FluidStack fluidStack = fluid != null ? new FluidStack(fluid, fluidAmount) : null;

		tank.setCapacity(capacity);
		tank.setFluid(fluidStack);
	}

	public static void writeFluidTank(ByteBuf stream, FluidTank tank)
	{
		int fluidId = -1;
		int fluidAmount = 0;
		final int capacity = tank.getCapacity();
		final FluidStack fs = tank.getFluid();

		if (fs != null)
		{
			fluidId = fs.getFluidID();
			fluidAmount = fs.amount;
		}

		stream.writeInt(capacity);
		stream.writeInt(fluidId);
		stream.writeInt(fluidAmount);
	}
}
