package growthcraft.cellar.common.item;

import java.util.List;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.UnitFormatter;

//import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemWaterBag extends ItemFood implements IFluidContainerItem
{
	protected int capacity;
	protected int dosage;

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public ItemWaterBag()
	{
		super(0, 0, false);
		capacity = GrowthCraftCellar.getConfig().waterBagCapacity;
		dosage = GrowthCraftCellar.getConfig().waterBagDosage;
		setUnlocalizedName("grc.waterBag");
		setTextureName("grccellar:water_bag");
		setCreativeTab(GrowthCraftCellar.tab);
	}

	public NBTTagCompound getFluidTagFromStack(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.stackTagCompound != null)
			{
				if (stack.stackTagCompound.hasKey("Fluid"))
				{
					return stack.stackTagCompound.getCompoundTag("Fluid");
				}
			}
		}
		return null;
	}

	/* IFluidContainerItem */
	@Override
	public FluidStack getFluid(ItemStack container)
	{
		final NBTTagCompound tag = getFluidTagFromStack(container);
		return tag != null ? FluidStack.loadFluidStackFromNBT(tag) : null;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		final String basename = super.getItemStackDisplayName(stack);
		final String fluidName = UnitFormatter.fluidName(getFluid(stack));
		if (fluidName != null)
		{
			return StatCollector.translateToLocalFormatted("grc.cellar.format.waterBag.name", basename, fluidName);
		}
		return basename;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		final FluidStack fluidstack = getFluid(stack);
		if (fluidstack != null)
		{
			final String fluidname = UnitFormatter.fluidNameForContainer(fluidstack);
			list.add(StatCollector.translateToLocalFormatted("grc.cellar.format.waterBag.contents", fluidname, fluidstack.amount, getCapacity(stack)));
		}
		super.addInformation(stack, player, list, bool);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg)
	{
		this.icons = new IIcon[17];
		this.icons[0] = reg.registerIcon(getIconString() + "/white");
		this.icons[1] = reg.registerIcon(getIconString() + "/orange");
		this.icons[2] = reg.registerIcon(getIconString() + "/magenta");
		this.icons[3] = reg.registerIcon(getIconString() + "/light_blue");
		this.icons[4] = reg.registerIcon(getIconString() + "/yellow");
		this.icons[5] = reg.registerIcon(getIconString() + "/lime");
		this.icons[6] = reg.registerIcon(getIconString() + "/pink");
		this.icons[7] = reg.registerIcon(getIconString() + "/gray");
		this.icons[8] = reg.registerIcon(getIconString() + "/light_gray");
		this.icons[9] = reg.registerIcon(getIconString() + "/cyan");
		this.icons[10] = reg.registerIcon(getIconString() + "/purple");
		this.icons[11] = reg.registerIcon(getIconString() + "/blue");
		this.icons[12] = reg.registerIcon(getIconString() + "/brown");
		this.icons[13] = reg.registerIcon(getIconString() + "/green");
		this.icons[14] = reg.registerIcon(getIconString() + "/red");
		this.icons[15] = reg.registerIcon(getIconString() + "/black");
		this.icons[16] = reg.registerIcon(getIconString() + "/default");
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < 17; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return this.icons[meta];
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return capacity;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		if (resource == null)
		{
			return 0;
		}

		if (!doFill)
		{
			if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
			{
				return Math.min(capacity, resource.amount);
			}

			final FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));

			if (stack == null)
			{
				return Math.min(capacity, resource.amount);
			}

			if (!stack.isFluidEqual(resource))
			{
				return 0;
			}

			return Math.min(capacity - stack.amount, resource.amount);
		}

		if (container.stackTagCompound == null)
		{
			container.stackTagCompound = new NBTTagCompound();
		}

		if (!container.stackTagCompound.hasKey("Fluid"))
		{
			final NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

			if (capacity < resource.amount)
			{
				fluidTag.setInteger("Amount", capacity);
				container.stackTagCompound.setTag("Fluid", fluidTag);
				return capacity;
			}

			container.stackTagCompound.setTag("Fluid", fluidTag);
			return resource.amount;
		}

		final NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
		final FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

		if (!stack.isFluidEqual(resource))
		{
			return 0;
		}

		int filled = capacity - stack.amount;
		if (resource.amount < filled)
		{
			stack.amount += resource.amount;
			filled = resource.amount;
		}
		else
		{
			stack.amount = capacity;
		}

		container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
		{
			return null;
		}

		final FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
		if (stack == null)
		{
			return null;
		}

		final int currentAmount = stack.amount;
		stack.amount = Math.min(stack.amount, maxDrain);
		if (doDrain)
		{
			if (currentAmount == stack.amount)
			{
				container.stackTagCompound.removeTag("Fluid");

				if (container.stackTagCompound.hasNoTags())
				{
					container.stackTagCompound = null;
				}
				return stack;
			}

			final NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
			fluidTag.setInteger("Amount", currentAmount - stack.amount);
			container.stackTagCompound.setTag("Fluid", fluidTag);
		}
		return stack;
	}
}
