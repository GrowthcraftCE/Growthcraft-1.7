package growthcraft.core.utils;

import buildcraft.api.tools.IToolWrench;

import net.minecraft.entity.player.EntityPlayer;

/**
 * In place of a missing wrench, you can use a stick, which results in
 * this amazing wrench, which does amazing things, no really.
 */
public class AmazingStickWrench implements IToolWrench
{
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		// we should have an achivement for this.
	}
}
