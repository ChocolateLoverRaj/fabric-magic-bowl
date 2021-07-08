package net.fabricmc.magic_bowl;

import java.lang.reflect.Type;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import me.lortseam.completeconfig.extensions.CompleteConfigExtension;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CompleteConfigExtensionItem implements CompleteConfigExtension {
  @Override
  public TypeSerializerCollection getTypeSerializers() {
    return TypeSerializerCollection.builder().register(Item.class, new TypeSerializer<Item>() {

      @Override
      public Item deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return Registry.ITEM.get(new Identifier(node.getString()));
      }

      @Override
      public void serialize(Type type, @Nullable Item obj, ConfigurationNode node) throws SerializationException {
        if (obj != null)
          node.set(Registry.ITEM.getId(obj).toString());
      }

    }).build();
  }
}
