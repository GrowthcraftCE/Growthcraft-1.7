package growthcraft.cellar.common.booze;

public class ModifierFunctionPotent extends AbstractModifierFunction
{
	@Override
	public int applyLevel(int lvl)
	{
		return lvl + 1;
	}

	@Override
	public int applyTime(int t)
	{
		return t / 2;
	}
}
