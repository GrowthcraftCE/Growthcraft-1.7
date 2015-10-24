package growthcraft.api.cellar.brewing;

import growthcraft.api.cellar.common.ProcessingResult;
import growthcraft.api.cellar.common.Residue;

import net.minecraftforge.fluids.FluidStack;

public class BrewingResult extends ProcessingResult
{
	public BrewingResult(FluidStack f, int t, Residue r)
	{
		super(f, t, r);
	}
}
