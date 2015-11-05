package growthcraft.cellar;

import growthcraft.core.ConfigBase;

public class Config extends ConfigBase
{
	@ConfigOption(catergory="Potions", name="Potion Tipsy ID")
	public int potionTipsyID = 50;

	@ConfigOption(catergory="Villager", name="Brewer ID")
	public int villagerBrewerID = 10;


	@ConfigOption(catergory="Booze Fluid", name="Booze blocks use water material", desc="Should Booze blocks behave like water (introduces water bottle bug, but fluids behave like fluids.)?")
	public boolean boozeIsWater = true;


	@ConfigOption(catergory="Fermenting Barrel", name="Ferment Barrel fermenting time", desc="[Higher -> Slower]")
	public int fermentSpeed = 24000;

	@ConfigOption(catergory="Fermenting Barrel", name="Ferment Barrels form yeast", desc="Should ferment barrels create yeast (with a young booze present)?")
	public boolean formYeastInBarrels = true;

	@ConfigOption(catergory="Fermenting Barrel", name="Fluid Capacity", desc="How much fluid can a Fermenting Barrel hold? (in mB (milli buckets))")
	public int fermentBarrelMaxCap = 3000;


	@ConfigOption(catergory="Brew Kettle", name="Drop items in Brew Kettle", desc="Enable to have brew kettles pick up dropped items")
	public boolean dropItemsInBrewKettle;

	@ConfigOption(catergory="Brew Kettle", name="Fluid Capacity", desc="How much fluid can a Brew Kettle hold? (in mB (milli buckets))")
	public int brewKettleMaxCap = 1000;


	@ConfigOption(catergory="Fruit Press", name="Fluid Capacity", desc="How much fluid can a Fruit Press hold? (in mB (milli buckets))")
	public int fruitPressMaxCap = 1000;


	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
