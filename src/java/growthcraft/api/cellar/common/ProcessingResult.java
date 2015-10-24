package growthcraft.api.cellar.common;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ProcessingResult
{
	private final FluidStack fluid;
	public final int time;
	public final Residue residue;

	public ProcessingResult(FluidStack f, int t, Residue r)
	{
		this.fluid = f;
		this.time = t;
		this.residue = r;
	}

	public Fluid getFluid()
	{
		return fluid.getFluid();
	}

	public FluidStack getFluidStack()
	{
		return fluid;
	}

	public int getAmount()
	{
		return fluid.amount;
	}

	public FluidStack asFluidStack(int size)
	{
		final FluidStack result = fluid.copy();
		result.amount = size;
		return result;
	}
}
