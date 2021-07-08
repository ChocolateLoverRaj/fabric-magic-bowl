package net.fabricmc.magic_bowl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MagicBowlMod implements ModInitializer {
	public static final String MOD_NAME = "magic_bowl";
	public static final Item MAGIC_BOWL = new MagicBowlItem();
	public static final MagicBowlConfig CONFIG = new MagicBowlConfig();
	public static final Identifier CONFIG_PACKET_ID = new Identifier(MOD_NAME, "config");

	public static final ScreenHandlerType<MagicBowlScreenHandler> MAGIC_BOWL_SCREEN_HANDLER;

	static {
		MAGIC_BOWL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(MagicBowlScreenHandler.IDENTIFIER,
				MagicBowlScreenHandler::new);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		MagicBowlMod.CONFIG.load();
		Registry.register(Registry.ITEM, new Identifier(MOD_NAME, "magic_bowl"), MAGIC_BOWL);
	}
}
