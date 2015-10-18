package growthcraft.nether.utils;

import net.minecraft.util.DamageSource;

public class DamageSources
{
	static class GrcDamageSource extends DamageSource
	{
		public GrcDamageSource(String str)
		{
			super(str);
		}
	}

	public static final DamageSource muertecapFood = new GrcDamageSource("muertecapFood");
	public static final DamageSource knifeBush = new GrcDamageSource("knifeBush");

	private DamageSources() {}
}
