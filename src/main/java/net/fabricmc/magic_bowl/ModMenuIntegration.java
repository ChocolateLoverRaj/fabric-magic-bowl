package net.fabricmc.magic_bowl;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.reflect.TypeToken;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.lortseam.completeconfig.data.Entry;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.EntryBuilder;
import me.lortseam.completeconfig.gui.cloth.GuiProvider;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

// TODO: Fix banned items GUI
public class ModMenuIntegration implements ModMenuApi {
  private static final GuiProvider GUI_PROVIDER_ITEM = GuiProvider.create(new EntryBuilder<Entry<Item>>() {

    @Override
    public FieldBuilder<?, ?> apply(Entry<Item> entry) {
      return ConfigEntryBuilder.create()
          .startDropdownMenu(entry.getText(), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(entry.getValue()),
              DropdownMenuBuilder.CellCreatorBuilder.ofItemObject())
          .setDefaultValue(entry.getDefaultValue())
          .setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString))
              .collect(Collectors.toCollection(LinkedHashSet::new)))
          .setTooltip(entry.getTooltip()).setSaveConsumer(entry::setValue);
    }
  }, Item.class);

  private static final GuiProvider GUI_PROVIDER_ITEM_LIST = GuiProvider
      .create((Entry<List<Item>> entry) -> new ItemListBuilder(ConfigEntryBuilder.create().getResetButtonKey(),
          entry.getText(), entry.getValue()).setDefaultValue(entry.getDefaultValue()).setTooltip(entry.getTooltip())
              .setSaveConsumer(entry::setValue),
          new TypeToken<List<Item>>() {
          }.getType());

  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return new ConfigScreenFactory<Screen>() {
      @Override
      public Screen create(Screen parent) {
        ClothConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder();
        screenBuilder.getRegistry().add(GUI_PROVIDER_ITEM, GUI_PROVIDER_ITEM_LIST);
        return screenBuilder.build(parent, MagicBowlMod.CONFIG);
      }
    };
  }
}
