package net.fabricmc.magic_bowl;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MagicBowlScreen extends HandledScreen<MagicBowlScreenHandler> {
  // A path to the gui texture. In this example we use the texture from the
  // dispenser
  private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/anvil.png");
  private static final int RED_COLOR = 16736352;
  private static final float ERROR_TEXT_Y = 106;
  private static final int ERROR_TEXT_X_OFFSET = 110;

  public MagicBowlScreen(MagicBowlScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
  }

  @Override
  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    int x = (width - backgroundWidth) / 2;
    int y = (height - backgroundHeight) / 2;
    drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    if (!this.handler.canCombine())
      this.drawTexture(matrices, x + 99, y + 45, this.backgroundWidth, 0, 28, 21);
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    renderBackground(matrices);
    super.render(matrices, mouseX, mouseY, delta);
    Text errorText = this.handler.getErrorText();
    if (errorText != null) {
      int x = this.backgroundWidth - this.textRenderer.getWidth(errorText) + ERROR_TEXT_X_OFFSET;
      fill(matrices, x - 2, (int) ERROR_TEXT_Y - 2, this.backgroundWidth + ERROR_TEXT_X_OFFSET, (int) ERROR_TEXT_Y + 10,
          1325400064);
      this.textRenderer.drawWithShadow(matrices, errorText, (float) x, (float) ERROR_TEXT_Y, RED_COLOR);
    }
    drawMouseoverTooltip(matrices, mouseX, mouseY);
  }

  @Override
  protected void init() {
    super.init();
    // Center the title
    titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
  }
}