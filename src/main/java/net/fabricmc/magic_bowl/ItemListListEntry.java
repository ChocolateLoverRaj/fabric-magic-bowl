package net.fabricmc.magic_bowl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class ItemListListEntry extends AbstractListListEntry<Item, ItemListListEntry.ItemListCell, ItemListListEntry> {
  public ItemListListEntry(Text fieldName, List<Item> value, boolean defaultExpanded,
      Supplier<Optional<Text[]>> tooltipSupplier, Consumer<List<Item>> saveConsumer, Supplier<List<Item>> defaultValue,
      Text resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
    super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey,
        requiresRestart, deleteButtonEnabled, insertInFront, ItemListCell::new);
  }

  public static class ItemListCell
      extends AbstractListListEntry.AbstractListCell<Item, ItemListCell, ItemListListEntry> {

    private DropdownBoxEntry<Item> widget;

    public ItemListCell(@Nullable Item value, ItemListListEntry listListEntry) {
      super(value, listListEntry);
      widget = new DropdownMenuBuilder<Item>(listListEntry.resetWidget.getMessage(), listListEntry.getFieldName(),
          DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(substituteDefault(value)),
          DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).build();
    }

    @Override
    public SelectionType getType() {
      return null;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
      // TODO Auto-generated method stub

    }

    @Override
    public List<? extends Element> children() {
      return widget.children();
    }

    @Override
    public Item getValue() {
      return widget.getValue();
    }

    @Override
    public int getCellHeight() {
      return widget.getItemHeight();
    }

    @Override
    public Optional<Text> getError() {
      return widget.getError();
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
        int mouseY, boolean isHovered, float delta) {
      widget.render(matrices, index, x, y, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    }

    private Item substituteDefault(@Nullable Item value) {
      if (value == null)
        return Items.AIR;
      else
        return value;
    }
  }

  @Override
  public ItemListListEntry self() {
    return this;
  }

  @Override
  public void render(MatrixStack arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7,
      boolean arg8, float arg9) {
    // TODO Auto-generated method stub
    super.render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
  }
}
