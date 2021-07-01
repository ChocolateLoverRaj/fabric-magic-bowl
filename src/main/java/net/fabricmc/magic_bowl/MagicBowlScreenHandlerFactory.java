package net.fabricmc.magic_bowl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class MagicBowlScreenHandlerFactory implements NamedScreenHandlerFactory {

  @Override
  public Text getDisplayName() {
    return new TranslatableText(MagicBowlMod.MAGIC_BOWL.getTranslationKey());
  }

  @Override
  public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
    return new MagicBowlScreenHandler(syncId, inventory);
  }
}
