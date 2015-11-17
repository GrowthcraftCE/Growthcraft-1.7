package growthcraft.cellar;

import growthcraft.core.ConfigBase;

public class GrcCellarConfig extends ConfigBase
{
	@ConfigOption(catergory="Potions", name="Potion Tipsy ID")
	public int potionTipsyID = 50;

	@ConfigOption(catergory="Villager", name="Brewer ID")
	public int villagerBrewerID = 10;


	@ConfigOption(catergory="Booze Fluid", name="Booze blocks use water material", desc="Should Booze blocks behave like water (introduces water bottle bug, but fluids behave like fluids.)?")
	public boolean boozeIsWater = true;


	@ConfigOption(catergory="Booze", name="Bottle Capacity", desc="How much booze does a bottle hold?")
	public int bottleCapacity = 333;

	@ConfigOption(catergory="Booze", name="Water Bag Capacity", desc="How much booze does a water bag hold (normally 5x a bottle)?")
	public int waterBagCapacity = 333 * 5;

	@ConfigOption(catergory="Booze", name="Water Bag Dosage", desc="How much booze is used when you drink from water bag (normally 1 bottle)?")
	public int waterBagDosage = 333;


	@ConfigOption(catergory="Fermenting Barrel", name="Ferment Barrel fermenting time", desc="[Higher -> Slower]")
	public int fermentSpeed = 24000;

	@ConfigOption(catergory="Fermenting Barrel", name="Fluid Capacity", desc="How much fluid can a Fermenting Barrel hold? (in mB (milli buckets))")
	public int fermentBarrelMaxCap = 3000;


	@ConfigOption(catergory="Fermenting Jar", name="Generation Time", desc="How long does it take for a ferment jar to produce 1 yeast? (number of ticks)")
	public int fermentJarTimeMax = 1200;

	@ConfigOption(catergory="Fermenting Jar", name="Fluid per Yeast", desc="How much fluid is used per yeast? (normally 4 yeast per bucket)")
	public int fermentJarConsumption = 1000 / 4;

	@ConfigOption(catergory="Fermenting Jar", name="Fluid Capacity", desc="How much fluid can a Fermenting Jar hold? (in mB (milli buckets))")
	public int fermentJarMaxCap = 1000;


	@ConfigOption(catergory="Brew Kettle", name="Drop items in Brew Kettle", desc="Enable to have brew kettles pick up dropped items")
	public boolean dropItemsInBrewKettle;

	@ConfigOption(catergory="Brew Kettle", name="Fluid Capacity", desc="How much fluid can a Brew Kettle hold? (in mB (milli buckets))")
	public int brewKettleMaxCap = 1000;


	@ConfigOption(catergory="Fruit Press", name="Fluid Capacity", desc="How much fluid can a Fruit Press hold? (in mB (milli buckets))")
	public int fruitPressMaxCap = 1000;


	@ConfigOption(catergory="Integration", name="Enable Waila Integration", desc="Should we integrate with Waila (if available)?")
	public boolean enableWailaIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
