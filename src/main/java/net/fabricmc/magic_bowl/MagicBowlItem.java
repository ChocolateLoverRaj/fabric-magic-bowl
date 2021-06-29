package net.fabricmc.magic_bowl;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MagicBowlItem extends Item {
  public MagicBowlItem() {
    super(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
  }
}
