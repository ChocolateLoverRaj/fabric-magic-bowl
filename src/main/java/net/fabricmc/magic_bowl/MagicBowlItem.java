package net.fabricmc.magic_bowl;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext;

public class MagicBowlItem extends Item {
  public static final int NON_HOTBAR_SLOT = 9;
  public static final int INVENTORY_SLOTS = 27;
  public static final String CONTAINS_TAG_KEY = "contains";
  public static final int COOLDOWN_TICKS = 60;
  public static final int CONTAINS_NOTHING = -1;
  public static final boolean HAS_GLINT = true;
  private static ServerClient<Boolean> bowlScreenOpen = new ServerClient<Boolean>(false);
  private boolean bowlIsInMainHand = false;
  private boolean moving = false;

  @Override
  public int getMaxUseTime(ItemStack stack) {
    int contains = getContains(stack);
    if (contains != CONTAINS_NOTHING) {
      Item item = Item.byRawId(contains);
      return item.getMaxUseTime(item.getDefaultStack());
    }
    return 0;
  }

  public MagicBowlItem() {
    super(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    System.out.println(Item.getRawId(this));
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    ItemStack stack = user.getStackInHand(hand);
    if (!bowlScreenOpen.get(world.isClient)) {
      if (getContains(stack) != -1) {
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
        BlockPos blockPos = blockHitResult.getBlockPos();
        FluidState fluidState = world.getFluidState(blockPos);

        if (fluidState.isIn(FluidTags.WATER)) {
          playWashSound(world, user);
          removeContains(stack);
        } else {
          Item item = Item.byRawId(getContains(stack));
          user.getInventory().offerOrDrop(item.getDefaultStack());
          playTakeSound(world, user);
          startCooldown(user);
        }
      } else {
        bowlIsInMainHand = hand == Hand.MAIN_HAND;
        user.openHandledScreen(new MagicBowlScreenHandlerFactory());
        bowlScreenOpen.set(world.isClient, true);
      }
      return TypedActionResult.success(stack);
    }
    return TypedActionResult.pass(stack);
  }

  @Override
  public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player,
      StackReference cursorStackReference) {
    moving = bowlIsInMainHand && slot.id == player.getInventory().selectedSlot + INVENTORY_SLOTS + 1;
    return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
  }

  @Override
  public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
    if (slot.id == 0 && moving) {
      System.out.println("hi");
      return true;
    }
    return super.onStackClicked(stack, slot, clickType, player);
  }

  public static void onClose(boolean isClient) {
    bowlScreenOpen.set(isClient, false);
  }

  private void startCooldown(PlayerEntity player) {
    player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
  }

  private int getContains(ItemStack stack) {
    return stack.getTag() != null && stack.getTag().contains(CONTAINS_TAG_KEY) ? stack.getTag().getInt(CONTAINS_TAG_KEY)
        : -1;
  }

  private void removeContains(ItemStack stack) {
    stack.getTag().remove(CONTAINS_TAG_KEY);
  }

  @Override
  public Text getName(ItemStack stack) {
    int contains = getContains(stack);
    return contains == -1 ? super.getName()
        : new TranslatableText(getTranslationKey() + ".contains", Item.byRawId(contains).getName(),
            new TranslatableText(getTranslationKey()).getString());
  }

  @Override
  public boolean hasGlint(ItemStack stack) {
    return HAS_GLINT/* || super.hasGlint(stack) */;
  }

  private void playWashSound(World world, PlayerEntity user) {
    world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_SWIM,
        SoundCategory.NEUTRAL, 0.5F, 1);
  }

  private void playTakeSound(World world, PlayerEntity user) {
    world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ITEM_PICKUP,
        SoundCategory.NEUTRAL, 0.5F, 1);
  }

  public static ItemStack containing(ItemStack containsStack) {
    Item containsItem = containsStack.getItem();
    if (!(containsItem instanceof AirBlockItem)) {
      ItemStack itemStack = MagicBowlMod.MAGIC_BOWL.getDefaultStack();
      NbtCompound nbtCompound = new NbtCompound();
      nbtCompound.putInt(CONTAINS_TAG_KEY, Item.getRawId(containsItem));
      itemStack.setTag(nbtCompound);
      return itemStack;
    }
    return ItemStack.EMPTY;
  }

  public static boolean containsItem(ItemStack stack) {
    NbtCompound nbtCompound = stack.getTag();
    if (nbtCompound == null)
      return false;
    return nbtCompound.contains(CONTAINS_TAG_KEY);
  }
}
