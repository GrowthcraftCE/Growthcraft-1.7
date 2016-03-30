/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.client.render.item;

import growthcraft.core.util.RenderUtils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraft.util.ResourceLocation;

public abstract class GrcItemRenderer implements IItemRenderer
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

	protected void bindTexture(ResourceLocation res)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(res);
	}

	protected abstract void render(ItemRenderType type, ItemStack item, Object... data);

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
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
			render(type, item, data);
		}
		RenderUtils.endInventoryRender();
	}
}
