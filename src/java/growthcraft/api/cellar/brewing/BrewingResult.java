package growthcraft.api.cellar.brewing;

import growthcraft.api.cellar.common.ProcessingResult;
import growthcraft.api.cellar.common.Residue;

import net.minecraftforge.fluids.FluidStack;

public class BrewingResult extends ProcessingResult
{
	private FluidStack inputFluid;

	public BrewingResult(FluidStack i, FluidStack f, int t, Residue r)
	{
		super(f, t, r);
		this.inputFluid = i;
	}

	public FluidStack getInputFluidStack()
	{
		return inputFluid;
	}
}
