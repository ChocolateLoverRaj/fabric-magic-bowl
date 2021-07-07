package net.fabricmc.magic_bowl;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItemGroup;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

import com.oroarmor.config.BooleanConfigItem;
import static com.google.common.collect.ImmutableList.of;

public class MagicBowlConfig extends Config {
  public static final String FOOD_ONLY = "food_only";

  public MagicBowlConfig() {
    super(of(new ConfigItemGroup(of(new BooleanConfigItem(FOOD_ONLY, true, FOOD_ONLY)), MagicBowlMod.MOD_NAME)),
        new File(FabricLoader.getInstance().getConfigDir().toFile(), MagicBowlMod.MOD_NAME + ".json"),
        MagicBowlMod.MOD_NAME);
  }
}
