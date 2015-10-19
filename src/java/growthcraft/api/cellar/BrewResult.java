package growthcraft.api.cellar;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BrewResult
{
	public final int time;
	public final int amount;
	public final float residue;
	private final Fluid fluid;

	public BrewResult(Fluid f, int t, int a, float r)
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
