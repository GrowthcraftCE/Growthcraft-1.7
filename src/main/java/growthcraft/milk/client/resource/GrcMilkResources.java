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
package growthcraft.milk.client.resource;

import java.util.HashMap;
import java.util.Map;

import growthcraft.milk.client.model.ModelButterChurn;
import growthcraft.milk.client.model.ModelCheeseBlock;
import growthcraft.milk.client.model.ModelCheesePress;
import growthcraft.milk.client.model.ModelCheeseVat;
import growthcraft.milk.client.model.ModelHangingCurds;
import growthcraft.milk.client.model.ModelPancheon;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.EnumCheeseStage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GrcMilkResources
{
	public static GrcMilkResources INSTANCE;

	static final String DOMAIN = "grcmilk";

	// Textures
	/// Model Textures
	public final ResourceLocation textureButterChurn = new ResourceLocation(DOMAIN, "textures/models/butter_churn/butter_churn.png");
	public final Map<EnumCheeseType, Map<EnumCheeseStage, ResourceLocation>> texturesCheeseBlock;
	public final ResourceLocation textureCheesePress = new ResourceLocation(DOMAIN, "textures/models/cheese_press/cheese_press.png");
	public final ResourceLocation textureCheeseVat = new ResourceLocation(DOMAIN, "textures/models/cheese_vat/cheese_vat.png");
	public final ResourceLocation textureHangingCurds = new ResourceLocation(DOMAIN, "textures/models/hanging_curds/hanging_curds.png");
	public final ResourceLocation texturePancheon = new ResourceLocation(DOMAIN, "textures/models/pancheon/pancheon.png");

	// Models
	public final ModelButterChurn modelButterChurn = new ModelButterChurn();
	public final ModelCheeseBlock modelCheeseBlock = new ModelCheeseBlock();
	public final ModelCheesePress modelCheesePress = new ModelCheesePress();
	public final ModelCheeseVat modelCheeseVat = new ModelCheeseVat();
	public final ModelHangingCurds modelHangingCurds = new ModelHangingCurds();
	public final ModelPancheon modelPancheon = new ModelPancheon();

	public GrcMilkResources()
	{
		INSTANCE = this;
		this.texturesCheeseBlock = new HashMap<EnumCheeseType, Map<EnumCheeseStage, ResourceLocation>>();

		for (EnumCheeseType cheese : EnumCheeseType.VALUES)
		{
			final Map<EnumCheeseStage, ResourceLocation> stageMap = new HashMap<EnumCheeseStage, ResourceLocation>();
			texturesCheeseBlock.put(cheese, stageMap);
			for (EnumCheeseStage stage : cheese.stages)
			{
				final String basename = cheese.name + "_" + stage.name;
				stageMap.put(stage, new ResourceLocation(DOMAIN, "textures/models/cheese/" + basename + ".png"));
			}
		}
	}
}
