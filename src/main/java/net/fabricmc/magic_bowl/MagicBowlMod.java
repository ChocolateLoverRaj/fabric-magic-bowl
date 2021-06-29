package net.fabricmc.magic_bowl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MagicBowlMod implements ModInitializer {
	public static final String MOD_NAME = "magic_bowl";
	public static final Item MAGIC_BOWL = new MagicBowlItem();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.ITEM, new Identifier(MOD_NAME, "magic_bowl"), MAGIC_BOWL);
	}
}
