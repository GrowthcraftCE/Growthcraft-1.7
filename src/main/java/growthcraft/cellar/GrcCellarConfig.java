package growthcraft.cellar;

import growthcraft.core.ConfigBase;

public class GrcCellarConfig extends ConfigBase
{
	@ConfigOption(catergory="Potions", name="Potion Tipsy ID")
	public int potionTipsyID = 50;

	@ConfigOption(catergory="Villager", name="Enabled", desc="Should we register Village Generation, and Villager Trades?")
	public boolean enableVillageGen = true;

	@ConfigOption(catergory="Villager", name="Brewer ID")
	public int villagerBrewerID = 6677;


	@ConfigOption(catergory="Events", name="Enable Discard Button", desc="Enable and show the Discard button from certain cellar blocks.")
	public boolean enableDiscardButton = true;


	@ConfigOption(catergory="Booze Fluid", name="Booze blocks use water material", desc="Should Booze blocks behave like water (introduces water bottle bug, but fluids behave like fluids.)?")
	public boolean boozeIsWater = true;


	@ConfigOption(catergory="Booze", name="Water Bag Capacity", desc="How much booze does a water bag hold (normally 5x a bottle)?")
	public int waterBagCapacity = 333 * 5;

	@ConfigOption(catergory="Booze", name="Water Bag Dosage", desc="How much booze is used when you drink from water bag (normally 1 bottle)?")
	public int waterBagDosage = 333;


	@ConfigOption(catergory="Booze/Effects", name="Enabled", desc="Should extra booze effects be enabled (does not affect tipsy)?")
	public boolean boozeEffectsEnabled = true;


	@ConfigOption(catergory="Fermenting Barrel", name="Ferment Barrel fermenting time", desc="[Higher -> Slower]")
	public int fermentTime = 24000;

	@ConfigOption(catergory="Fermenting Barrel", name="Fluid Capacity", desc="How much fluid can a Fermenting Barrel hold? (in mB (milli buckets))")
	public int fermentBarrelMaxCap = 3000;

	@ConfigOption(catergory="Fermenting Barrel", name="Use Cached Recipes?", desc="May increase performance, but causes issues with some fluid transport blocks.")
	public boolean fermentBarrelUseCachedRecipe = true;


	@ConfigOption(catergory="Fermenting Jar", name="Generation Time", desc="How long does it take for a ferment jar to produce 1 yeast? (number of ticks)")
	public int cultureJarTimeMax = 1200;

	@ConfigOption(catergory="Fermenting Jar", name="Fluid per Yeast", desc="How much fluid is used per yeast? (normally 4 yeast per bucket)")
	public int cultureJarConsumption = 1000 / 4;

	@ConfigOption(catergory="Fermenting Jar", name="Fluid Capacity", desc="How much fluid can a Fermenting Jar hold? (in mB (milli buckets))")
	public int cultureJarMaxCap = 1000;


	@ConfigOption(catergory="Brew Kettle", name="Drop items in Brew Kettle", desc="Enable to drop items in brew kettles.")
	public boolean dropItemsInBrewKettle = true;

	@ConfigOption(catergory="Brew Kettle", name="Fluid Capacity", desc="How much fluid can a Brew Kettle hold? (in mB (milli buckets))")
	public int brewKettleMaxCap = 1000;

	@ConfigOption(catergory="Brew Kettle", name="Fill by Rain", desc="Should the brew kettle fill from rain?")
	public boolean brewKettleFillsWithRain = true;

	@ConfigOption(catergory="Brew Kettle", name="Rain Fill Amount", desc="How much water is added to the brew kettle per rain tick? (in mB (milli buckets))")
	public int brewKettleRainFillPerUnit = 10;

	@ConfigOption(catergory="Brew Kettle", name="Set fire to fallen living entities", desc="Should the kettle set fire to entities that fall in it (if heated?)")
	public boolean setFireToFallenLivingEntities;


	@ConfigOption(catergory="Fruit Press", name="Fluid Capacity", desc="How much fluid can a Fruit Press hold? (in mB (milli buckets))")
	public int fruitPressMaxCap = 1000;


	@ConfigOption(catergory="Heat Sources", name="Fire Heat", desc="How much heat does fire provide? (this is normally 0.0..1.0)")
	public float fireHeatValue = 1.0f;

	@ConfigOption(catergory="Heat Sources", name="Lava Heat", desc="How much heat does lava provide? (this is normally 0.0..1.0)")
	public float lavaHeatValue = 0.7f;


	//@ConfigOption(catergory="Integration", name="Enable NEI Integration", desc="Should we integrate with NotEnoughItems (if available)?")
	//public boolean enableNEIIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Waila Integration", desc="Should we integrate with Waila (if available)?")
	public boolean enableWailaIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;
}
