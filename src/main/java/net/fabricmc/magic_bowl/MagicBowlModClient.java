package net.fabricmc.magic_bowl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class MagicBowlModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ScreenRegistry.register(MagicBowlMod.MAGIC_BOWL_SCREEN_HANDLER, MagicBowlScreen::new);
    ClientPlayNetworking.registerGlobalReceiver(MagicBowlMod.CONFIG_PACKET_ID,
        (client, handler, buf, responseSender) -> {
          // TODO: Load config sent by server
          System.out.println(buf.readString());
        });
  }
}
