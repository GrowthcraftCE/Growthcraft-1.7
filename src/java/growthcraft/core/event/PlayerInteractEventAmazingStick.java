package growthcraft.core.event;

import growthcraft.core.block.IRotatableBlock;
import growthcraft.core.block.IWrenchable;
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
		if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

		final EntityPlayer player = event.entityPlayer;
		final World world = player.worldObj;

		if (world.isRemote) return;

		final ItemStack itemstack = player.getCurrentEquippedItem();
		if (itemstack != null && ItemUtils.isAmazingStick(itemstack))
		{
			final Block block = world.getBlock(event.x, event.y, event.z);
			if (!player.isSneaking())
			{
				if (block != null && (block instanceof IRotatableBlock))
				{
					final ForgeDirection side = ForgeDirection.getOrientation(event.face);
					if (((IRotatableBlock)block).isRotatable(world, event.x, event.y, event.z, side))
					{
						if (block.rotateBlock(world, event.x, event.y, event.z, side))
						{
							event.useBlock = Result.DENY;
						}
					}
				}
			}
			else
			{
				if (block != null && (block instanceof IWrenchable))
				{
					if (((IWrenchable)block).wrenchBlock(world, event.x, event.y, event.z, player, itemstack))
					{
						event.useBlock = Result.DENY;
					}
				}
			}
		}
	}
}
