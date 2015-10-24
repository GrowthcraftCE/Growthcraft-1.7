package growthcraft.api.cellar.fermenting;

import growthcraft.api.cellar.common.ProcessingResult;
import growthcraft.api.cellar.common.Residue;

import net.minecraftforge.fluids.FluidStack;

public class FermentationResult extends ProcessingResult
{
	public FermentationResult(FluidStack f, int t, Residue r)
	{
		super(f, t, r);
	}
}
