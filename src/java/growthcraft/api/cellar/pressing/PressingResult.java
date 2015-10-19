package growthcraft.api.cellar.pressing;

import growthcraft.api.cellar.common.ProcessingResult;
import growthcraft.api.cellar.common.Residue;

import net.minecraftforge.fluids.Fluid;

public class PressingResult extends ProcessingResult
{
	public PressingResult(Fluid f, int t, int a, Residue r)
	{
		super(f, t, a, r);
	}
}
