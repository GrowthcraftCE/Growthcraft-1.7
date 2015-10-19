package growthcraft.api.cellar.common;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ProcessingResult
{
	public final int time;
	public final int amount;
	public final Residue residue;
	private final Fluid fluid;

	public ProcessingResult(Fluid f, int t, int a, Residue r)
	{
		this.fluid = f;
		this.time = t;
		this.amount = a;
		this.residue = r;
	}

	public Fluid getFluid()
	{
		return fluid;
	}

	public FluidStack asFluidStack(int size)
	{
		return new FluidStack(fluid, size);
	}
}
