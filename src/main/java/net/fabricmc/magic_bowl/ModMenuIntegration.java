package net.fabricmc.magic_bowl;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return new ConfigScreenFactory<Screen>() {

      @Override
      public Screen create(Screen parent) {
        ConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder();
        return screenBuilder.build(parent, MagicBowlMod.CONFIG);
      }

    };
  }
}
