package growthcraft.cellar.eventhandler;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.stats.CellarAchievement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class EventHandlerItemCraftedEventCellar
{
	@SubscribeEvent
	public void onItemCrafting(ItemCraftedEvent event)
	{
		if (GrowthCraftCellar.fermentBarrel.equals(event.crafting.getItem()))
		{
			CellarAchievement.CRAFT_BARREL.addStat(event.player, 1);
		}
	}
}
