package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.CommonTooltipHandler;
import com.khazoda.heirlooms.block.DisplayCaseBlockEntity;
import com.khazoda.heirlooms.block.DisplayRackBlockEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Gui.class)
public class ClientMixinGUI {

  @Unique
  private final Minecraft heirlooms$minecraft = Minecraft.getInstance();

  @Unique
  private static final int ORIGINAL_BROWN = 9068607;
  @Unique
  private static final int ORIGINAL_BLUE = 4157834;
  @Unique
  private static final int HUD_GOLD = 0xFFD27D;
  @Unique
  private static final int HUD_SKY = 0x8ACEEB;

  @Unique
  private static final float TEXT_SCALE = 1f;

  @Inject(at = @At("TAIL"), method = "render")
  public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    if (this.heirlooms$minecraft.options.hideGui || this.heirlooms$minecraft.player == null || this.heirlooms$minecraft.level == null)
      return;

    HitResult hitResult = this.heirlooms$minecraft.hitResult;

    if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
      BlockHitResult blockHit = (BlockHitResult) hitResult;
      BlockPos pos = blockHit.getBlockPos();
      BlockEntity be = this.heirlooms$minecraft.level.getBlockEntity(pos);

      if (be instanceof DisplayCaseBlockEntity displayCase) {
        ItemStack stack = displayCase.getItem(0);
        if (!stack.isEmpty()) heirlooms$renderHudExtras(guiGraphics, stack);
      } else if (be instanceof DisplayRackBlockEntity displayRack) {
        ItemStack stack = displayRack.getItem(0);
        if (!stack.isEmpty()) heirlooms$renderHudExtras(guiGraphics, stack);
      }
    }
  }

  @Unique
  private void heirlooms$renderHudExtras(GuiGraphics guiGraphics, ItemStack stack) {
    Component name = stack.getHoverName();
    List<Component> dataLines = new ArrayList<>();
    CommonTooltipHandler.handleTooltip(stack, dataLines, TooltipFlag.NORMAL, true, true);

    /* Change text colouring to be more readable */
    for (int i = 0; i < dataLines.size(); i++) {
      Component line = dataLines.get(i);
      TextColor color = line.getStyle().getColor();
      if (color != null) {
        if (color.getValue() == ORIGINAL_BROWN) dataLines.set(i, line.copy().withColor(HUD_GOLD));
        else if (color.getValue() == ORIGINAL_BLUE) dataLines.set(i, line.copy().withColor(HUD_SKY));
      }
    }

    int screenWidth = this.heirlooms$minecraft.getWindow().getGuiScaledWidth();
    int screenHeight = this.heirlooms$minecraft.getWindow().getGuiScaledHeight();
    int centerX = screenWidth / 2;
    int startY = (screenHeight / 2) + 35;

    /* Item name & icon*/
    int nameWidth = this.heirlooms$minecraft.font.width(name);
    int iconWidth = 16;
    int gap = 4;
    int totalWidth = iconWidth + gap + nameWidth;
    int groupStartX = centerX - (totalWidth / 2);

    guiGraphics.renderItem(stack, groupStartX, startY - 4);
    guiGraphics.drawString(this.heirlooms$minecraft.font, name, groupStartX + iconWidth + gap, startY, 0xFFFFFFFF, true);

    if (dataLines.isEmpty()) return;
    int baseDataY = startY + 18;
    var poseStack = guiGraphics.pose();
    poseStack.pushMatrix();
    poseStack.scale(TEXT_SCALE, TEXT_SCALE);

    /* Recalculate coordinates for scaled down text */
    int sCenterX = (int) (centerX / TEXT_SCALE);
    int sLeftX = (int) ((centerX - 65) / TEXT_SCALE);
    int sRightX = (int) ((centerX + 65) / TEXT_SCALE);
    int sBaseY = (int) (baseDataY / TEXT_SCALE);

    if (dataLines.size() >= 4) {
      int currentY = sBaseY;
      /* Left column */
      for (int i = 0; i < 2; i++) {
        guiGraphics.drawCenteredString(this.heirlooms$minecraft.font, dataLines.get(i), sLeftX, currentY, 0xFFFFFFFF);
        currentY += this.heirlooms$minecraft.font.lineHeight + 2;
      }
      /* Right column */
      currentY = sBaseY;
      for (int i = 2; i < dataLines.size(); i++) {
        guiGraphics.drawCenteredString(this.heirlooms$minecraft.font, dataLines.get(i), sRightX, currentY, 0xFFFFFFFF);
        currentY += this.heirlooms$minecraft.font.lineHeight + 2;
      }

    } else {
      /* Single centered column*/
      int currentY = sBaseY;
      for (Component line : dataLines) {
        guiGraphics.drawCenteredString(this.heirlooms$minecraft.font, line, sCenterX, currentY, 0xFFFFFFFF);
        currentY += this.heirlooms$minecraft.font.lineHeight + 2;
      }
    }
    poseStack.popMatrix();
  }
}