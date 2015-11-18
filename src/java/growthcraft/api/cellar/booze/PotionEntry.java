package growthcraft.api.cellar.booze;

public class PotionEntry
{
	private int id;
	private int time;
	private int level;

	public PotionEntry(int i, int tm, int lvl)
	{
		this.id = i;
		this.time = tm;
		this.level = lvl;
	}

	public int getID()
	{
		return id;
	}

	public int getTime()
	{
		return time;
	}

	public int getLevel()
	{
		return level;
	}
}
