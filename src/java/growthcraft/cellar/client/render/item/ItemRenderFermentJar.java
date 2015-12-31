package growthcraft.cellar.client.render.item;

import growthcraft.core.util.RenderUtils;
import growthcraft.cellar.client.render.model.ModelFermentJar;
import growthcraft.cellar.client.resource.GrcCellarResources;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderFermentJar implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem( ItemRenderType type, ItemStack item, Object... data )
	{
		RenderUtils.startInventoryRender();
		{
			switch (type)
			{
				case ENTITY:
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					break;
				case INVENTORY:
					GL11.glTranslatef(-0.5F, -0.9F, -0.5F);
					break;
				case EQUIPPED_FIRST_PERSON:
					GL11.glTranslatef(0.5F, 0.0F, 0.5F);
					break;
				default:
					break;
			}
			Minecraft.getMinecraft().renderEngine.bindTexture(GrcCellarResources.INSTANCE.textureFermentJar);
			GrcCellarResources.INSTANCE.modelFermentJar.renderForInventory(ModelFermentJar.SCALE);
		}
		RenderUtils.endInventoryRender();
	}
}
