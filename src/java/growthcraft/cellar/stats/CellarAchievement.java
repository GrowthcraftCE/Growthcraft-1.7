package growthcraft.cellar.stats;

import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.entity.player.EntityPlayer;

public enum CellarAchievement
{
	CRAFT_BARREL,
	FERMENT_BOOZE,
	GET_DRUNK,
	ON_THE_GO;

	public void unlock(EntityPlayer player)
	{
		GrowthCraftCellar.achievements.unlock(this, player);
	}

	public void addStat(EntityPlayer player, int n)
	{
		GrowthCraftCellar.achievements.addStat(this, player, n);
	}
}
