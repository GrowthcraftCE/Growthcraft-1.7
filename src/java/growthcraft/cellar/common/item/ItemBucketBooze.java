package growthcraft.cellar.common.item;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.item.ItemBucketFluid;
import growthcraft.core.lib.GrcCoreState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;

public class ItemBucketBooze extends ItemBucketFluid
{
	public ItemBucketBooze(Block block, Fluid buze, CreativeTabs creativeTab)
	{
		super(block, buze, creativeTab);
		setUnlocalizedName("grc.boozeBucket");
	}

	public ItemBucketBooze(Block block, Fluid buze)
	{
		this(block, buze, GrowthCraftCellar.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(stack, player, list, bool);
		if (GrcCoreState.showDetailedInformation())
		{
			BoozeUtils.addInformation(getFluid(stack), stack, player, list, bool);
		}
		else
		{
			list.add(EnumChatFormatting.GRAY +
					GrcI18n.translate("grc.tooltip.detailed_information",
						EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY));
		}
	}
}
