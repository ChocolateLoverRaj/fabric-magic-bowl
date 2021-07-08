package net.fabricmc.magic_bowl;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;

public class MagicBowlModServer implements DedicatedServerModInitializer {
  @Override
  public void onInitializeServer() {
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
      System.out.println("Player joined: " + handler.getPlayer().getDisplayName().toString());
      PacketByteBuf buf = PacketByteBufs.create();
      // TODO: Send config to client
      buf.writeString("welcome gabg");
      ServerPlayNetworking.send(handler.getPlayer(), MagicBowlMod.CONFIG_PACKET_ID, buf);
    });
  }
}
