package growthcraft.core.common.inventory;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import growthcraft.api.core.item.ItemTest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryProcessorTest
{
	final InventoryProcessor invProc = InventoryProcessor.instance();
	Item testItem1 = new Item();
	Item testItem2 = new Item();
	ItemStack apple = new ItemStack(testItem1);
	ItemStack coal = new ItemStack(testItem2);

	public GrcInternalInventory testInv()
	{
		return new GrcInternalInventory(null, 4);
	}

	@Test
	public void test_ItemPresence()
	{
		assertTrue(ItemTest.isValid(apple));
		assertTrue(ItemTest.isValid(coal));
	}

	@Test
	public void test_slotIsEmpty()
	{
		final GrcInternalInventory inv = testInv();
		assertTrue(invProc.slotIsEmpty(inv, 0));
		assertTrue(invProc.slotIsEmpty(inv, 1));
		assertTrue(invProc.slotIsEmpty(inv, 2));
		assertTrue(invProc.slotIsEmpty(inv, 3));
		inv.setInventorySlotContents(0, coal);
		assertFalse(invProc.slotIsEmpty(inv, 0));
	}

	@Test
	public void test_slotsAreEmpty()
	{
		final GrcInternalInventory inv = testInv();
		assertTrue(invProc.slotsAreEmpty(inv));
		assertTrue(invProc.slotsAreEmpty(inv, new int[]{0,1,2,3}));

		inv.setInventorySlotContents(0, coal);

		assertFalse(invProc.slotsAreEmpty(inv));
		assertFalse(invProc.slotsAreEmpty(inv, new int[]{0,1,2,3}));
	}

	@Test
	public void test_mergeWithSlot()
	{
		final GrcInternalInventory inv = testInv();
		final ItemStack stack = apple.copy();
		final ItemStack stack2 = coal.copy();

		invProc.mergeWithSlot(inv, stack, 0);
		// IT SHOULD clear the stack
		{
			assertEquals(0, stack.stackSize);
		}
		// IT SHOULD set the item into the slot, if the slot was empty
		{
			assertTrue(ItemTest.itemMatches(apple, inv.getStackInSlot(0)));
			assertEquals(1, inv.getStackInSlot(0).stackSize);
		}

		// IT SHOULD replace an empty stack (a stack with a stackSize of 0)
		inv.getStackInSlot(0).stackSize = 0;
		invProc.mergeWithSlot(inv, stack2, 0);
		{
			assertTrue(ItemTest.itemMatches(coal, inv.getStackInSlot(0)));
			assertEquals(1, inv.getStackInSlot(0).stackSize);
		}

		// IT WILL NOT replace a stack of different items with a stackSize greater than 0
		invProc.mergeWithSlot(inv, stack, 0);
		{
			assertTrue(ItemTest.itemMatches(coal, inv.getStackInSlot(0)));
			assertEquals(1, inv.getStackInSlot(0).stackSize);
		}

		// IT WILL
	}

	@Test
	public void test_mergeWithSlots()
	{

	}

	@Test
	public void test_clearSlots()
	{
		final GrcInternalInventory inv = testInv();

		// With no specific slots
		{
			// nothing was cleared
			assertFalse(invProc.clearSlots(inv));

			inv.setInventorySlotContents(0, apple.copy());
			// something was cleared
			assertTrue(invProc.clearSlots(inv));
		}

		assertTrue(invProc.slotsAreEmpty(inv));

		// With specific slots
		{
			assertFalse(invProc.clearSlots(inv, new int[]{0,2}));
			inv.setInventorySlotContents(0, apple.copy());
			inv.setInventorySlotContents(2, coal.copy());
			inv.setInventorySlotContents(3, apple.copy());
			assertFalse(invProc.slotsAreEmpty(inv));
			assertTrue(invProc.clearSlots(inv, new int[]{0,2}));
			assertFalse(invProc.slotsAreEmpty(inv));
			assertTrue(invProc.slotsAreEmpty(inv, new int[]{0,2}));
		}
	}

	@Test
	public void test_yankSlot()
	{
		final GrcInternalInventory inv = testInv();

		{
			assertEquals(null, invProc.yankSlot(inv, 0));

			inv.setInventorySlotContents(2, apple.copy());

			assertTrue(ItemTest.areStacksEqual(apple, invProc.yankSlot(inv, 2)));

			assertEquals(null, inv.getStackInSlot(2));
		}
	}

	@Test
	public void test_checkSlot_ItemStack()
	{

	}

	@Test
	public void test_checkSlot_IMultiItemStacks()
	{

	}

	@Test
	public void test_findItemSlots()
	{

	}

	@Test
	public void test_findNextEmpty()
	{
		final GrcInternalInventory inv = testInv();

		{
			assertEquals(0, invProc.findNextEmpty(inv));
			inv.setInventorySlotContents(0, apple.copy());
			assertEquals(1, invProc.findNextEmpty(inv));
			inv.setInventorySlotContents(1, coal.copy());
			assertEquals(2, invProc.findNextEmpty(inv));
		}
	}

	@Test
	public void test_findNextEmptyFromEnd()
	{
		final GrcInternalInventory inv = testInv();

		{
			assertEquals(3, invProc.findNextEmptyFromEnd(inv));
			inv.setInventorySlotContents(0, apple.copy());
			assertEquals(3, invProc.findNextEmptyFromEnd(inv));
			inv.setInventorySlotContents(3, coal.copy());
			assertEquals(2, invProc.findNextEmptyFromEnd(inv));
		}
	}

	@Test
	public void test_findNextPresent()
	{
		final GrcInternalInventory inv = testInv();

		{
			assertEquals(-1, invProc.findNextPresent(inv));

			inv.setInventorySlotContents(0, apple.copy());
			assertEquals(0, invProc.findNextPresent(inv));

			// clear inventory and ensure that it doesn't find anything
			inv.clear();
			assertEquals(-1, invProc.findNextPresent(inv));


			inv.setInventorySlotContents(2, apple.copy());
			assertEquals(2, invProc.findNextPresent(inv));

			inv.setInventorySlotContents(1, coal.copy());
			assertEquals(1, invProc.findNextPresent(inv));
		}
	}

	@Test
	public void test_findNextPresentFromEnd()
	{
		final GrcInternalInventory inv = testInv();

		{
			assertEquals(-1, invProc.findNextPresentFromEnd(inv));
			inv.setInventorySlotContents(0, apple.copy());
			assertEquals(0, invProc.findNextPresentFromEnd(inv));

			// clear inventory and ensure that it doesn't find anything
			inv.clear();
			assertEquals(-1, invProc.findNextPresentFromEnd(inv));

			inv.setInventorySlotContents(2, apple.copy());
			assertEquals(2, invProc.findNextPresentFromEnd(inv));

			inv.setInventorySlotContents(3, apple.copy());
			assertEquals(3, invProc.findNextPresentFromEnd(inv));
		}
	}

	@Test
	public void test_checkSlotsUnordered()
	{

	}

	@Test
	public void test_slotsAreValid()
	{
		final GrcInternalInventory inv = testInv();

		assertTrue(invProc.slotsAreValid(inv, new int[]{0,1,2,3}));
		assertFalse(invProc.slotsAreValid(inv, new int[]{0,1,2,3,4}));
		assertFalse(invProc.slotsAreValid(inv, new int[]{0,-1,2,3}));
	}

	@Test
	public void test_consumeItemsInSlots()
	{

	}

	@Test
	public void test_consumeItems()
	{

	}

	@Test
	public void test_canInsertItem()
	{
		final GrcInternalInventory inv = testInv();

		assertTrue(invProc.canInsertItem(inv, apple, 0));

		{
			inv.setInventorySlotContents(0, apple.copy());

			assertTrue(invProc.canInsertItem(inv, apple, 0));
			assertFalse(invProc.canInsertItem(inv, coal, 0));
		}
	}

	@Test
	public void test_canExtractItem()
	{
		final GrcInternalInventory inv = testInv();

		assertFalse(invProc.canExtractItem(inv, apple, 0));

		{
			inv.setInventorySlotContents(0, apple.copy());
			assertTrue(invProc.canExtractItem(inv, apple, 0));
			assertFalse(invProc.canExtractItem(inv, coal, 0));
		}
	}
}
