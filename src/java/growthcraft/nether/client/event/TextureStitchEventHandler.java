package growthcraft.nether.client.event;

import growthcraft.core.GrowthCraftCore;
import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent;

public class TextureStitchEventHandler
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			GrowthCraftNether.booze.setBoozeIcons(GrowthCraftCore.liquidSmoothTexture);
		}
	}
}
