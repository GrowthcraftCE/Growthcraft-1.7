package growthcraft.core.common;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementPageGrowthcraft
{
	public static List<Achievement> chievMasterList = new ArrayList<Achievement>();
	public static AchievementPage chievPage;

	private AchievementPageGrowthcraft() {}

	public static void init()
	{
		final Achievement[] chievList = new Achievement[chievMasterList.size()];
		for(int i = 0; i < chievList.length; i++)
		{
			chievList[i] = chievMasterList.get(i);
		}

		chievPage = new AchievementPage(GrcI18n.translate("achievementPage.pageGrowthCraft"), chievList);
		AchievementPage.registerAchievementPage(chievPage);
	}
}
