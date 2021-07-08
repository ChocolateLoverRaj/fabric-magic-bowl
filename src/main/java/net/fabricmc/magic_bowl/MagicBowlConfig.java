package net.fabricmc.magic_bowl;

import java.util.Arrays;
import java.util.List;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import me.lortseam.completeconfig.data.Config;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public final class MagicBowlConfig extends Config implements ConfigContainer {
  MagicBowlConfig() {
    super(MagicBowlMod.MOD_NAME);
  }

  @Transitive
  @ConfigEntries
  public static class Config implements ConfigGroup {
    public static boolean foodOnly = true;
    public static List<Item> bannedItems = Arrays.asList(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE);
    @ConfigEntry.BoundedInteger(min = 0)
    public static int cooldown = 60;
  }
}
