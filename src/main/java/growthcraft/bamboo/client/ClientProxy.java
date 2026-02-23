/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.bamboo.client;

import growthcraft.bamboo.client.renderer.RenderBamboo;
import growthcraft.bamboo.client.renderer.RenderBambooFence;
import growthcraft.bamboo.client.renderer.RenderBambooRaft;
import growthcraft.bamboo.client.renderer.RenderBambooScaffold;
import growthcraft.bamboo.client.renderer.RenderBambooWall;
import growthcraft.bamboo.common.CommonProxy;
import growthcraft.bamboo.common.entity.EntityBambooRaft;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		super.init();
		RenderingRegistry.registerBlockHandler(new RenderBamboo());
		RenderingRegistry.registerBlockHandler(new RenderBambooFence());
		RenderingRegistry.registerBlockHandler(new RenderBambooWall());
		RenderingRegistry.registerBlockHandler(new RenderBambooScaffold());

		RenderingRegistry.registerEntityRenderingHandler(EntityBambooRaft.class, new RenderBambooRaft());
	}
}
