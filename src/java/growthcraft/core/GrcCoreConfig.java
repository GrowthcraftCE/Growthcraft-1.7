package growthcraft.core;

public class GrcCoreConfig extends ConfigBase
{
	@ConfigOption(catergory="Minecraft/Debug", name="Enable Game Registry Dump", desc="Should Growthcraft dump the GameRegistry and FluidRegistry to text files?")
	public boolean dumpGameRegistry;


	@ConfigOption(catergory="Salt", name="Bucket Ocean Salt Water", desc="Should we enable the bucket of salt water event handler?")
	public boolean bucketOfOceanSaltWater;


	@ConfigOption(catergory="Fluid Container", name="Bottle Capacity", desc="How much booze does a bottle hold?")
	public int bottleCapacity = 333;
	
	@ConfigOption(catergory="Fluid Container", name="Water Bottle Capacity", desc="Change vanilla water bottles to GrowthCraft's capacity?")
	public boolean changeWaterBottleCapacity;
	
	@ConfigOption(catergory="Fluid Container", name="Water Bottle Container", desc="Should vanilla water bottles return the bottle on use?")
	public boolean changeWaterBottleContainer;


	@ConfigOption(catergory="Booze/Effects", name="Hide Poisoned", desc="Should purposely poisoned booze have its effect hidden?")
	public boolean hidePoisonedBooze = true;


	@ConfigOption(catergory="Integration", name="Enable Apple Core Integration", desc="Should we integrate with Apple Core (if available)?")
	public boolean enableAppleCoreIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Et-Futurum Integration", desc="Should we integrate with Et-Futurum (if available)?")
	public boolean enableEtfuturumIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Natura Integration", desc="Should we integrate with Natura (if available)?")
	public boolean enableNaturaIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Thaumcraft Integration", desc="Should we integrate with Thaumcraft (if available)?")
	public boolean enableThaumcraftIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Waila Integration", desc="Should we integrate with Waila (if available)?")
	public boolean enableWailaIntegration = true;

	@ConfigOption(catergory="Integration", name="Enable Woodstuff Integration", desc="Should we integrate with Woodstuff (if available)?")
	public boolean enableWoodstuffIntegration = true;
	
	@ConfigOption(catergory="Integration", name="Enable NEI Integration", desc="Should we integrate with NotEnoughItems (if available)?")
	public boolean enableNEIIntegration = true;
}
