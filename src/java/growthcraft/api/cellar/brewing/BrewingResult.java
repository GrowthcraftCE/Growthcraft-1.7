package growthcraft.api.cellar.brewing;

import growthcraft.api.cellar.common.PressingResult;
import growthcraft.api.cellar.common.Residue;

public class BrewingResult extends ProcessingResult
{
	public BrewingResult(Fluid f, int t, int a, Residue r)
	{
		super(f, t, a, r);
	}
}
