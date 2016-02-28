package growthcraft.cellar.eventhandler;

import growthcraft.cellar.common.item.ItemWaterBag;
import growthcraft.cellar.common.itemblock.ItemBlockFermentBarrel;
import growthcraft.cellar.stats.CellarAchievement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.Item;

public class EventHandlerItemCraftedEventCellar
{
	@SubscribeEvent
	public void onItemCrafting(ItemCraftedEvent event)
	{
		if (event.crafting != null)
		{
			final Item craftedItem = event.crafting.getItem();
			if (craftedItem instanceof ItemBlockFermentBarrel)
			{
				CellarAchievement.CRAFT_BARREL.addStat(event.player, 1);
			}
			else if (craftedItem instanceof ItemWaterBag)
			{
				CellarAchievement.ON_THE_GO.addStat(event.player, 1);
			}
		}
	}
}
