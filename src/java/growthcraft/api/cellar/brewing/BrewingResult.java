package growthcraft.api.cellar.brewing;

import growthcraft.api.cellar.common.ProcessingResult;
import growthcraft.api.cellar.common.Residue;

import net.minecraftforge.fluids.Fluid;

public class BrewingResult extends ProcessingResult
{
	public BrewingResult(Fluid f, int t, int a, Residue r)
	{
		super(f, t, a, r);
	}
}
