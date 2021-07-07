package net.fabricmc.magic_bowl;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class MagicBowlScreenHandler extends ScreenHandler {
  private final Inventory inventory;
  private final CraftingResultInventory output = new CraftingResultInventory();
  private final Inventory input = new SimpleInventory(2);
  private final String errorsId = "custom." + MagicBowlMod.MOD_NAME + ".screens." + MagicBowlMod.MOD_NAME + ".errors.";

  protected MagicBowlScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new SimpleInventory(3));
  }

  // This constructor gets called from the BlockEntity on the server without
  // calling the other constructor first, the server knows the inventory of the
  // container
  // and can therefore directly provide it as an argument. This inventory will
  // then be synced to the client.
  public MagicBowlScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
    super(MagicBowlMod.MAGIC_BOWL_SCREEN_HANDLER, syncId);
    checkSize(inventory, 3);
    this.inventory = inventory;
    // some inventories do custom logic when a player opens it.
    inventory.onOpen(playerInventory.player);
    Slot ogSlot = new Slot(input, 0, 27, 47) {
      @Override
      public ItemStack insertStack(ItemStack stack, int count) {
        updateOutput(0, stack);
        return super.insertStack(stack, count);
      }

      @Override
      public ItemStack takeStack(int amount) {
        updateOutput(0, ItemStack.EMPTY);
        return super.takeStack(amount);
      }

      @Override
      public void setStack(ItemStack stack) {
        updateOutput(0, stack);
        super.setStack(stack);
      }
    };
    int magicBowlSlot = getMagicBowlSlot(playerInventory);
    this.addSlot(ogSlot);
    this.addSlot(new Slot(input, 1, 76, 47) {
      @Override
      public int getMaxItemCount() {
        return 1;
      }

      @Override
      public ItemStack insertStack(ItemStack stack, int count) {
        updateOutput(1, stack);
        return super.insertStack(stack, count);
      }

      @Override
      public ItemStack takeStack(int amount) {
        updateOutput(1, ItemStack.EMPTY);
        return super.takeStack(amount);
      }

      @Override
      public void setStack(ItemStack stack) {
        updateOutput(1, stack);
        super.setStack(stack);
      }
    });
    this.addSlot(new Slot(this.output, 2, 134, 47) {
      @Override
      public boolean canInsert(ItemStack stack) {
        return false;
      }

      @Override
      public boolean canTakeItems(PlayerEntity playerEntity) {
        return true;
      }

      @Override
      public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
        clearInputs();
      }
    });
    int k;
    for (k = 0; k < 3; ++k) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
      }
    }
    for (k = 0; k < 9; ++k) {
      this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
    }
    ogSlot.setStack(playerInventory.getStack(magicBowlSlot));
    playerInventory.removeOne(playerInventory.getStack(magicBowlSlot));
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
        } else if (invSlot == 2)
          clearInputs();
      } else if (!this.insertItem(originalStack, originalStack.getItem() instanceof MagicBowlItem ? 0 : 1,
          this.inventory.size(), false)) {
        return ItemStack.EMPTY;
      } else {
        this.updateOutput();
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
    MagicBowlItem.onClose(playerEntity.world.isClient);
    super.close(playerEntity);
    this.dropInventory(playerEntity, this.input);
  }

  private static int getMagicBowlSlot(PlayerInventory playerInventory) {
    if (playerInventory.getStack(playerInventory.selectedSlot).getItem() instanceof MagicBowlItem)
      return playerInventory.selectedSlot;
    else
      return PlayerInventory.OFF_HAND_SLOT;
  }

  private void updateOutput(ItemStack bowlStack, ItemStack addStack) {
    slots.get(2).setStack(canCombine(bowlStack, addStack) ? MagicBowlItem.containing(addStack) : ItemStack.EMPTY);
  }

  public void updateOutput() {
    updateOutput(slots.get(0).getStack(), slots.get(1).getStack());
  }

  public void updateOutput(int slot, ItemStack stack) throws IllegalArgumentException {
    if (slot == 0) {
      updateOutput(stack, slots.get(1).getStack());
    } else if (slot == 1) {
      updateOutput(slots.get(0).getStack(), stack);
    } else
      throw new IllegalArgumentException("slot can only be 0 or 1");
  }

  private void clearInputs() {
    input.removeStack(0);
    input.removeStack(1);
  }

  public boolean canCombine(ItemStack bowlStack, ItemStack addStack) {
    final boolean addSlotEmpty = addStack.isEmpty();
    if (bowlStack.isEmpty())
      return addSlotEmpty;
    else
      return bowlStack.getItem() instanceof MagicBowlItem && !MagicBowlItem.containsItem(bowlStack) && !addSlotEmpty
          && (!MagicBowlMod.CONFIG.getValue(MagicBowlMod.MOD_NAME + "." + MagicBowlConfig.FOOD_ONLY, Boolean.class)
              || addStack.isFood());
  }

  public boolean canCombine() {
    return canCombine(slots.get(0).getStack(), slots.get(1).getStack());
  }

  public boolean canCombine(int slot, ItemStack stack) throws IllegalArgumentException {
    if (slot == 0) {
      return canCombine(stack, slots.get(1).getStack());
    } else if (slot == 1) {
      return canCombine(slots.get(0).getStack(), stack);
    } else
      throw new IllegalArgumentException("slot can only be 0 or 1");
  }

  @Nullable
  public Text getErrorText() {
    ItemStack bowlStack = slots.get(0).getStack();
    if (bowlStack.getItem() instanceof MagicBowlItem) {
      if (MagicBowlItem.containsItem(bowlStack))
        return new TranslatableText(errorsId + "bowl_already_filled");
    } else if (!(bowlStack.getItem() instanceof AirBlockItem))
      return new TranslatableText(errorsId + "base_must_be_a_magic_bowl",
          new TranslatableText(MagicBowlMod.MAGIC_BOWL.getTranslationKey()));
    ItemStack addStack = slots.get(1).getStack();
    if (MagicBowlMod.CONFIG.getValue(MagicBowlMod.MOD_NAME + "." + MagicBowlConfig.FOOD_ONLY, Boolean.class)
        && !addStack.isEmpty() && !addStack.isFood())
      return new TranslatableText(errorsId + "food_only");
    return null;
  }
}
