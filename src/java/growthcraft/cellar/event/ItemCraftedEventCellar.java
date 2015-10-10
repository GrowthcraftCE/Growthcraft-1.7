package growthcraft.cellar.event;

import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.Item;

public class ItemCraftedEventCellar
{
	@SubscribeEvent
	public void onItemCrafting(ItemCraftedEvent event)
	{
		if (GrowthCraftCellar.fermentBarrel.getItem() == event.crafting.getItem())
		{
			event.player.addStat(GrowthCraftCellar.craftBarrel, 1);
		}
	}
}
