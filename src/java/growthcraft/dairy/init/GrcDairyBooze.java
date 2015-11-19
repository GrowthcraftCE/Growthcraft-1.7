package growthcraft.dairy.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.core.common.GrcModuleBase;

public class GrcDairyBooze extends GrcModuleBase
{
	// not an actual Booze, but we use the class since it has the ability to set color
	public Booze milk;
	public Booze[] milkBooze;

	@Override
	public void preInit()
	{
		this.milk = new Booze("grc.milk");
	}
}
