package totemic_commons.pokefenn.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/*
 * Created by Pokefenn.
 * Licensed under MIT (If this is one of my Mods)
 */
/**
 * An interface for blocks that react when they're right-clicked with a Totemic Staff
 */
public interface TotemicStaffUsage
{

    /**
     * Gets called when a player right-clicks the block with a Totemic Staff
     * @param world     the world
     * @param player    the player who clicked
     * @param itemStack the Totemic Staff that was clicked
     * @return true if something happened
     */
    public boolean onTotemicStaffRightClick(World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack);

}
