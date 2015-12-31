package growthcraft.cellar.client.resource;

import growthcraft.cellar.client.render.model.ModelFermentJar;
import growthcraft.cellar.client.render.model.ModelFruitPresser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GrcCellarResources
{
	public static GrcCellarResources INSTANCE;

	static final String DOMAIN = "grccellar";

	// Textures
	public final ResourceLocation textureFermentJar = new ResourceLocation(DOMAIN, "textures/blocks/models/ferment_jar.png");
	public final ResourceLocation textureFruitPresser = new ResourceLocation(DOMAIN, "textures/blocks/models/fruit_presser.png");

	/// Guis
	public final ResourceLocation textureGuiBrewKettle = new ResourceLocation(DOMAIN, "textures/guis/brewkettle_gui.png");
	public final ResourceLocation textureGuiFermentBarrel = new ResourceLocation(DOMAIN, "textures/guis/fermentbarrel_gui.png");
	public final ResourceLocation textureGuiFermentJar = new ResourceLocation(DOMAIN, "textures/guis/gui_ferment_jar.png");
	public final ResourceLocation textureGuiFruitPress = new ResourceLocation(DOMAIN, "textures/guis/fruitpress_gui.png");

	// Models
	public final ModelFermentJar modelFermentJar = new ModelFermentJar();
	public final ModelFruitPresser modelFruitPresser = new ModelFruitPresser();

	public GrcCellarResources()
	{
		INSTANCE = this;
	}
}
