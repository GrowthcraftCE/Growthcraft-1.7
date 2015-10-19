package growthcraft.api.cellar.pressing;

import growthcraft.api.cellar.common.PressingResult;
import growthcraft.api.cellar.common.Residue;

public class PressingResult extends ProcessingResult
{
	public PressingResult(Fluid f, int t, int a, Residue r)
	{
		super(f, t, a, r);
	}
}
