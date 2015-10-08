package growthcraft.cellar.item;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.core.utils.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;

public class ItemBlockFluidBooze extends ItemBlock
{
	protected int color = 0xFFFFFF;
	protected Fluid booze;

	public ItemBlockFluidBooze(Block block)
	{
		super(block);
		if (block instanceof BlockFluidBooze)
		{
			final BlockFluidBooze boozeBlock = (BlockFluidBooze)block;
			this.color = boozeBlock.getColor();
			this.booze = boozeBlock.getFluid();
		}
	}

	public Fluid getBooze()
	{
		return this.booze;
	}

	public int getBoozeColor()
	{
		return this.color;
	}

	protected void writeModifierTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		final Fluid booze = getBooze();
		final String modifier = UnitFormatter.fluidModifier(booze);
		if (modifier != null) list.add(modifier);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		writeModifierTooltip(stack, player, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return getBoozeColor();
	}
}
