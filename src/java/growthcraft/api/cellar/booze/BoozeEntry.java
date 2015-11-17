package growthcraft.api.cellar.booze;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import net.minecraftforge.fluids.Fluid;

public class BoozeEntry
{
	private final Fluid fluid;
	private final Set<String> tags;
	private final BoozeEffect effect;

	public BoozeEntry(Fluid flus)
	{
		this.fluid = flus;
		this.tags = new HashSet<String>();
		this.effect = new BoozeEffect();
	}

	public BoozeEffect getEffect()
	{
		return effect;
	}

	public Fluid getFluid()
	{
		return fluid;
	}

	public Collection<String> getTags()
	{
		return tags;
	}

	public void addTags(String... newtags)
	{
		for (String tag : newtags) tags.add(tag);
	}

	public boolean hasTags(String... checktags)
	{
		for (String tag : checktags) if (!tags.contains(tag)) return false;
		return true;
	}
}
