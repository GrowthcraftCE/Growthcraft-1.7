package growthcraft.core.event;

import growthcraft.core.block.IRotatableBlock;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.ItemUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Wrenches are overrated, we use sticks.
 */
public class PlayerInteractEventAmazingStick
{
	@SubscribeEvent
	public void PlayerInteract(PlayerInteractEvent event)
	{
		if (event.action == event.action.RIGHT_CLICK_BLOCK)
		{
			final EntityPlayer player = event.entityPlayer;
			final ItemStack itemstack = player.getCurrentEquippedItem();
			if (itemstack != null && ItemUtils.isAmazingStick(itemstack))
			{
				final World world = player.worldObj;
				final Block block = world.getBlock(event.x, event.y, event.z);
				if (block != null && (block instanceof IRotatableBlock))
				{
					block.rotateBlock(world, event.x, event.y, event.z, ForgeDirection.getOrientation(event.face));
					event.useBlock = Result.DENY;
				}
			}
		}
	}
}
