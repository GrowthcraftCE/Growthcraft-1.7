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
package growthcraft.milk;

import growthcraft.core.ConfigBase;

public class GrcMilkConfig extends ConfigBase
{
	@ConfigOption(catergory="Milk", name="Enabled", desc="Should we enable a fluid for Milk? (you may bork the mod if you don't have an alternative, eg. Forestry's Milk)")
	public boolean milkEnabled = true;

	@ConfigOption(catergory="Milk", name="Color", desc="What color is milk?")
	public int milkColor = 0xFFFFFF;


	@ConfigOption(catergory="Booze/Kumis", name="Color", desc="What color is kumis?")
	public int kumisColor = 0xFFFFFF;

	@ConfigOption(catergory="Booze/Poisoned Kumis", name="Color", desc="What color is poisoned kumis?")
	public int poisonedKumisColor = 0x7F9A65;


	@ConfigOption(catergory="Item/Stomach", name="Drop Rate", desc="How often do baby calves drop their stomachs?")
	public float stomachDropRate = 0.25f;

	@ConfigOption(catergory="Item/Stomach", name="Min Dropped", desc="What is the minimum number of stomachs dropped?")
	public int stomachMinDropped = 2;

	@ConfigOption(catergory="Item/Stomach", name="Max Dropped", desc="What is the maximum number of stomachs dropped?")
	public int stomachMaxDropped = 4;


	@ConfigOption(catergory="Device/Cheese Vat", name="Primary Tank Capacity", desc="How much fluid does the primary tank hold? (the tank with Milk)")
	public int cheeseVatPrimaryTankCapacity = 5000;

	@ConfigOption(catergory="Device/Cheese Vat", name="Rennet Tank Capacity", desc="How much fluid does the rennet tank hold? (the tank with Rennet obviously)")
	public int cheeseVatRennetTankCapacity = 333;

	@ConfigOption(catergory="Device/Cheese Vat", name="Waste Tank Capacity", desc="How much fluid does the waste tank hold? (the tank with whey)")
	public int cheeseVatWasteTankCapacity = 1000;

	@ConfigOption(catergory="Device/Cheese Vat", name="Recipe Tank Capacity", desc="How much fluid does the recipe tank hold? (the tank with any recipe related fluid)")
	public int cheeseVatRecipeTankCapacity = 1000;

	@ConfigOption(catergory="Device/Cheese Vat", name="Whey Transition Time", desc="How long does it take to change whey to ricotta?")
	public int cheeseVatWheyTime = 2400;

	@ConfigOption(catergory="Device/Cheese Vat", name="Curd Transition Time", desc="How long does it take to change milk to curds?")
	public int cheeseVatCurdTime = 2400;

	@ConfigOption(catergory="Device/Cheese Vat", name="Cheese Transition Time", desc="How long does it take to change curds to cheese?")
	public int cheeseVatCheeseTime = 2400;

	@ConfigOption(catergory="Device/Cheese Vat", name="Milk To Curds Whey Amount", desc="How much Whey is produced when transitioning from Milk to Curds?")
	public int cheeseVatMilkToCurdsWheyAmount = 1000;

	@ConfigOption(catergory="Device/Cheese Vat", name="Whey To Ricotta Whey Amount", desc="How much Whey is produced when transitioning from Whey to Ricotta?")
	public int cheeseVatWheyToRicottaWheyAmount = 1000;


	@ConfigOption(catergory="Integration", name="Enable Waila Integration", desc="Should we integrate with Waila (if available)?")
	public boolean enableWailaIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
