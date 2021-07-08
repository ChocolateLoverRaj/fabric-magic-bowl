package net.fabricmc.magic_bowl;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import me.lortseam.completeconfig.data.Config;

public final class MagicBowlConfig extends Config implements ConfigContainer {

  MagicBowlConfig() {
    super(MagicBowlMod.MOD_NAME);
  }

  @Transitive
  @ConfigEntries
  public static class Config implements ConfigGroup {
    public static boolean foodOnly = true;
    @ConfigEntry.BoundedInteger(min = 0)
    public static int cooldown = 60;
  }
}
