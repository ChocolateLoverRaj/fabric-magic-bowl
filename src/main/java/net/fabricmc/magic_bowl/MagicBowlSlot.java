package net.fabricmc.magic_bowl;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class MagicBowlSlot extends Slot {

  public MagicBowlSlot(Inventory inventory, int index, int x, int y) {
    super(inventory, index, x, y);
  }

  @Override
  public int getMaxItemCount() {
    return 1;
  }

}
