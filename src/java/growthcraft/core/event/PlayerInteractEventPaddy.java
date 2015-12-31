package growthcraft.core.event;

import java.util.HashMap;
import java.util.Map;

import growthcraft.core.util.BlockFlags;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractEventPaddy
{
	public static Map<Block, Block> paddyBlocks = new HashMap<Block, Block>();

	@SubscribeEvent
	public void PlayerInteract(PlayerInteractEvent event)
	{
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (event.face != 1) return;

			final EntityPlayer player = event.entityPlayer;
			final ItemStack itemstack = player.getCurrentEquippedItem();
			if (itemstack != null && itemstack.getItem() instanceof ItemSpade)
			{
				final World world = player.worldObj;
				final Block targetBlock = world.getBlock(event.x, event.y, event.z);
				final Block paddyBlock = paddyBlocks.get(targetBlock);
				if (paddyBlock != null)
				{
					world.setBlock(event.x, event.y, event.z, paddyBlock, world.getBlockMetadata(event.x, event.y, event.z), BlockFlags.UPDATE_CLIENT);
					world.playSoundEffect((double)((float)event.x + 0.5F),
						(double)((float)event.y + 0.5F),
						(double)((float)event.z + 0.5F),
						paddyBlock.stepSound.func_150496_b(),
						(paddyBlock.stepSound.getVolume() + 1.0F) / 2.0F,
						paddyBlock.stepSound.getPitch() * 0.8F);

					itemstack.damageItem(1, player);
				}
			}
		}
	}
}
