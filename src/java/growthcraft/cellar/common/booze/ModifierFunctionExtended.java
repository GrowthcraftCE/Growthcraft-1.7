package growthcraft.cellar.common.booze;

public class ModifierFunctionExtended extends AbstractModifierFunction
{
	@Override
	public int applyTime(int t)
	{
		return (int)(t * 2.67F);
	}
}
