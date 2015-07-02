package growthcraft.core.event;

import growthcraft.api.core.CoreRegistry;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HarvestDropsEventCore 
{
	private final int r = 5;

	@SubscribeEvent
	public void onHarvestDrops(HarvestDropsEvent event)
	{
		if (event.block == Blocks.vine && !event.isSilkTouching && event.harvester != null)
		{
			if (event.harvester.getHeldItem() == null)
			{
				doDrops(event);
			}
			else if (event.harvester.getHeldItem().getItem() != Items.shears)
			{
				doDrops(event);
			}
		}
	}

	private void doDrops(HarvestDropsEvent event)
	{
		if (!CoreRegistry.instance().getList().isEmpty())
		{
			if (new Random().nextInt(r) == 0)
			{
				event.drops.clear();
				event.dropChance = 1.0F;
				ItemStack stack = CoreRegistry.instance().getVineList(event.world);
				if (stack != null)
				{
					event.drops.add(new ItemStack(stack.getItem(), event.world.rand.nextInt(stack.stackSize) + 1));
				}
			}
		}
	}
}
