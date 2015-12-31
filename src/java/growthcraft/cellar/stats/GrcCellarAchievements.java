package growthcraft.cellar.stats;

import java.util.Map;
import java.util.EnumMap;

import growthcraft.core.common.AchievementPageGrowthcraft;
import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;

public class GrcCellarAchievements
{
	private static final Achievement NO_ACHIEVEMENT = (Achievement)null;

	private final Map<CellarAchievement, Achievement> achievements = new EnumMap<CellarAchievement, Achievement>(CellarAchievement.class);

	public GrcCellarAchievements()
	{
		achievements.put(CellarAchievement.CRAFT_BARREL,
			(new Achievement("grc.achievement.craftBarrel", "craftBarrel",
				-4, -4,
				GrowthCraftCellar.fermentBarrel.getBlock(),
				NO_ACHIEVEMENT)
			).initIndependentStat().registerStat()
		);
		achievements.put(CellarAchievement.FERMENT_BOOZE,
			(new Achievement("grc.achievement.fermentBooze", "fermentBooze",
				-2, -4,
				Items.nether_wart,
				achievements.get(CellarAchievement.CRAFT_BARREL))
			).registerStat()
		);
		achievements.put(CellarAchievement.GET_DRUNK,
			(new Achievement("grc.achievement.getDrunk", "getDrunk",
				0, -4,
				GrowthCraftCellar.chievItemDummy.asStack(),
				achievements.get(CellarAchievement.FERMENT_BOOZE))
			).setSpecial().registerStat()
		);
		achievements.put(CellarAchievement.ON_THE_GO,
			(new Achievement("grc.achievement.onTheGo", "onTheGo",
				2, -4,
				GrowthCraftCellar.waterBag.asStack(1, 16),
				NO_ACHIEVEMENT)
			).registerStat()
		);

		for (Achievement a : achievements.values())
		{
			AchievementPageGrowthcraft.chievMasterList.add(a);
		}
	}

	public void unlock(CellarAchievement a, EntityPlayer player)
	{
		final Achievement achievement = achievements.get(a);
		if (achievement != null)
		{
			player.triggerAchievement(achievement);
		}
	}

	public void addStat(CellarAchievement a, EntityPlayer player, int n)
	{
		final Achievement achievement = achievements.get(a);
		if (achievement != null)
		{
			player.addStat(achievement, n);
		}
	}
}
