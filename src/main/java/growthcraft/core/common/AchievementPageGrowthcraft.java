package growthcraft.core.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import growthcraft.api.core.i18n.GrcI18n;

public class AchievementPageGrowthcraft {

    public static List<Achievement> masterList = new ArrayList<Achievement>();
    public static AchievementPage masterPage;

    private AchievementPageGrowthcraft() {}

    public static void init() {
        final Achievement[] chievList = new Achievement[masterList.size()];
        for (int i = 0; i < chievList.length; i++) {
            chievList[i] = masterList.get(i);
        }

        masterPage = new AchievementPage(GrcI18n.translate("achievementPage.pageGrowthCraft"), chievList);
        AchievementPage.registerAchievementPage(masterPage);
    }
}
