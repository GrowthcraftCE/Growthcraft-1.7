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
