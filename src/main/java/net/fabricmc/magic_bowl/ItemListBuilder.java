package net.fabricmc.magic_bowl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

public class ItemListBuilder extends FieldBuilder<List<Item>, ItemListListEntry> {
  private final List<Item> value;
  private Consumer<List<Item>> saveConsumer = null;
  private boolean expanded = false;
  private boolean deleteButtonEnabled = true, insertInFront = true;
  private Function<List<Item>, Optional<Text[]>> tooltipSupplier = list -> Optional.empty();

  public ItemListBuilder(Text resetButtonKey, Text fieldNameKey, List<Item> value) {
    super(resetButtonKey, fieldNameKey);
    this.value = value;
  }

  public ItemListBuilder setSaveConsumer(Consumer<List<Item>> saveConsumer) {
    this.saveConsumer = saveConsumer;
    return this;
  }

  public ItemListBuilder setDefaultValue(List<Item> defaultValue) {
    this.defaultValue = () -> defaultValue;
    return this;
  }

  public ItemListBuilder setTooltip(Optional<Text[]> tooltip) {
    this.tooltipSupplier = list -> tooltip;
    return this;
  }

  @Override
  public @NotNull ItemListListEntry build() {
    ItemListListEntry entry = new ItemListListEntry(getFieldNameKey(), value, expanded, null, saveConsumer,
        defaultValue, getResetButtonKey(), requireRestart, deleteButtonEnabled, insertInFront);
    entry.setTooltipSupplier(() -> tooltipSupplier.apply(entry.getValue()));
    return entry;
  }

}
