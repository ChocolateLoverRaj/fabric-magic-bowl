package net.fabricmc.magic_bowl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class MagicBowlScreenHandler extends ScreenHandler {
  private final Inventory inventory;

  protected MagicBowlScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new SimpleInventory(1));
  }

  // This constructor gets called from the BlockEntity on the server without
  // calling the other constructor first, the server knows the inventory of the
  // container
  // and can therefore directly provide it as an argument. This inventory will
  // then be synced to the client.
  public MagicBowlScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
    super(MagicBowlMod.MAGIC_BOWL_SCREEN_HANDLER, syncId);
    checkSize(inventory, 1);
    this.inventory = inventory;
    // some inventories do custom logic when a player opens it.
    inventory.onOpen(playerInventory.player);

    // This will place the slot in the correct locations for a 3x3 Grid. The slots
    // exist on both server and client!
    // This will not render the background of the slots however, this is the Screens
    // job
    int m;
    int l;
    // Our inventory
    for (m = 0; m < 1; ++m) {
      for (l = 0; l < 1; ++l) {
        this.addSlot(new MagicBowlSlot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
      }
    }
    // The player inventory
    for (m = 0; m < 3; ++m) {
      for (l = 0; l < 9; ++l) {
        this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
      }
    }
    // The player Hotbar
    for (m = 0; m < 9; ++m) {
      this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
    }

  }

  public static final String NAME = "magic_bowl";
  public static final Identifier IDENTIFIER = new Identifier(MagicBowlMod.MOD_NAME, NAME);

  @Override
  public boolean canUse(PlayerEntity player) {
    return true;
  }

  /**
   * Like {@Item}.insertItem but respects max stacking limit.
   */
  @Override
  protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
    boolean bl = false;
    int i = startIndex;
    if (fromLast) {
      i = endIndex - 1;
    }

    Slot slot2;
    ItemStack itemStack;
    if (stack.isStackable()) {
      while (!stack.isEmpty()) {
        if (fromLast) {
          if (i < startIndex) {
            break;
          }
        } else if (i >= endIndex) {
          break;
        }

        slot2 = (Slot) this.slots.get(i);
        itemStack = slot2.getStack();
        if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
          int j = itemStack.getCount() + stack.getCount();
          if (j <= slot2.getMaxItemCount()) {
            stack.setCount(0);
            itemStack.setCount(j);
            slot2.markDirty();
            bl = true;
          } else if (itemStack.getCount() < slot2.getMaxItemCount()) {
            stack.decrement(slot2.getMaxItemCount() - itemStack.getCount());
            itemStack.setCount(slot2.getMaxItemCount());
            slot2.markDirty();
            bl = true;
          }
        }

        if (fromLast) {
          --i;
        } else {
          ++i;
        }
      }
    }

    if (!stack.isEmpty()) {
      if (fromLast) {
        i = endIndex - 1;
      } else {
        i = startIndex;
      }

      while (true) {
        if (fromLast) {
          if (i < startIndex) {
            break;
          }
        } else if (i >= endIndex) {
          break;
        }

        slot2 = (Slot) this.slots.get(i);
        itemStack = slot2.getStack();
        if (itemStack.isEmpty() && slot2.canInsert(stack)) {
          if (stack.getCount() > slot2.getMaxItemCount()) {
            slot2.setStack(stack.split(slot2.getMaxItemCount()));
          } else {
            slot2.setStack(stack.split(stack.getCount()));
          }

          slot2.markDirty();
          bl = true;
          break;
        }

        if (fromLast) {
          --i;
        } else {
          ++i;
        }
      }
    }

    return bl;
  }

  // Shift + Player Inv Slot
  @Override
  public ItemStack transferSlot(PlayerEntity player, int invSlot) {
    ItemStack newStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(invSlot);
    if (slot != null && slot.hasStack()) {
      ItemStack originalStack = slot.getStack();
      newStack = originalStack.copy();
      if (invSlot < this.inventory.size()) {
        if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
        return ItemStack.EMPTY;
      }

      if (originalStack.isEmpty()) {
        slot.setStack(ItemStack.EMPTY);
      } else {
        slot.markDirty();
      }
    }

    return newStack;
  }

  @Override
  public void close(PlayerEntity playerEntity) {
    PlayerInventory inventory = playerEntity.getInventory();
    ItemStack mainHandStack = inventory.getStack(inventory.selectedSlot);
    ItemStack offHandStack = inventory.getStack(PlayerInventory.OFF_HAND_SLOT);
    Item mainHandItem = mainHandStack.getItem();
    Item offHandItem = offHandStack.getItem();
    boolean bowlIsInMainHand = Item.getRawId(mainHandItem) == Item.getRawId(MagicBowlMod.MAGIC_BOWL);

    ((MagicBowlItem) (bowlIsInMainHand ? mainHandItem : offHandItem)).onClose(slots.get(0).getStack(),
        playerEntity.world.isClient, bowlIsInMainHand ? mainHandStack : offHandStack, playerEntity);
    super.close(playerEntity);
  }
}
