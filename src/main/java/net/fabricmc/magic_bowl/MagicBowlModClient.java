package net.fabricmc.magic_bowl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class MagicBowlModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ScreenRegistry.register(MagicBowlMod.MAGIC_BOWL_SCREEN_HANDLER, MagicBowlScreen::new);
  }
}
