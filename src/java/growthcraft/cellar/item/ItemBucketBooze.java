package growthcraft.cellar.item;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.utils.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

public class ItemBucketBooze extends ItemBucket
{
	private String iconName;
	private Fluid[] boozes;
	private int index;

	@SideOnly(Side.CLIENT)
	private IIcon bucket;
	@SideOnly(Side.CLIENT)
	private IIcon contents;

	private int color = 16777215;

	public ItemBucketBooze(Block block, Fluid[] boozes, int index, CreativeTabs creativeTab)
	{
		super(block);
		setContainerItem(Items.bucket);
		setUnlocalizedName("grc.boozeBucket");
		setCreativeTab(creativeTab);
		this.index = index;
		this.boozes = boozes;
	}

	public ItemBucketBooze(Block block, Fluid[] boozes, int index)
	{
		this(block, boozes, index, GrowthCraftCellar.tab);
	}

	public Fluid getBooze(int id)
	{
		return boozes[id];
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String s = super.getItemStackDisplayName(stack);
		return s + " " + StatCollector.translateToLocal(CellarRegistry.instance().booze().getBoozeName(boozes));
	}

	public ItemBucketBooze setColor(int color)
	{
		this.color = color;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		writeModifierTooltip(stack, player, list, bool);
	}

	protected void writeModifierTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		final Fluid booze = getBooze(index);
		final String s = UnitFormatter.fluidModifier(booze);
		if (s != null) list.add(s);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg)
	{
		this.bucket = reg.registerIcon("bucket_empty");
		this.contents = reg.registerIcon("grccellar:bucket_contents");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		return pass == 1 ? this.contents : this.bucket;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return pass == 1 ? this.color : 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
}
