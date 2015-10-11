package growthcraft.apples;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Alatyami on 8/30/2015.
 * Added per GitHub Issue #55
 */
public class AppleFuelHandler implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if (fuel != null)
        {
            Item item = fuel.getItem();
            if (item == Item.getItemFromBlock(GrowthCraftApples.appleSapling)) {
                return 100;
            }
        }
        return 0;
    }
}
