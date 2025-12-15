package com.khazoda.heirlooms.block.renderer;

import com.khazoda.heirlooms.block.DisplayCaseBlock;
import com.khazoda.heirlooms.block.DisplayCaseBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DisplayCaseRenderer implements BlockEntityRenderer<DisplayCaseBlockEntity, DisplayCaseRenderState> {

  private final ItemModelResolver itemModelResolver;

  private static final org.joml.Quaternionf[] ROTATIONS = new org.joml.Quaternionf[4];

  static {
    ROTATIONS[0] = Axis.YP.rotationDegrees(0).mul(Axis.XP.rotationDegrees(90.0F));   // South
    ROTATIONS[1] = Axis.YP.rotationDegrees(90).mul(Axis.XP.rotationDegrees(90.0F));  // West
    ROTATIONS[2] = Axis.YP.rotationDegrees(180).mul(Axis.XP.rotationDegrees(90.0F)); // North
    ROTATIONS[3] = Axis.YP.rotationDegrees(270).mul(Axis.XP.rotationDegrees(90.0F)); // East
  }

  public DisplayCaseRenderer(BlockEntityRendererProvider.Context context) {
    this.itemModelResolver = context.itemModelResolver();
  }

  @Override
  public DisplayCaseRenderState createRenderState() {
    return new DisplayCaseRenderState();
  }

  @Override
  public void extractRenderState(DisplayCaseBlockEntity blockEntity, DisplayCaseRenderState renderState, float partialTick, Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

    renderState.facing = blockEntity.getBlockState().getValue(DisplayCaseBlock.FACING);
    renderState.renderedBlockState = null;
    renderState.item = null;

    ItemStack stack = blockEntity.getItem(0);
    if (!stack.isEmpty()) {
      if (stack.getItem() instanceof BlockItem blockItem) {
        renderState.renderedBlockState = blockItem.getBlock().defaultBlockState();
      } else {
        renderState.item = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(renderState.item, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
      }
    }
  }

  @Override
  public void submit(DisplayCaseRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
    if (renderState.renderedBlockState == null && renderState.item == null) return;

    poseStack.pushPose();
    poseStack.translate(0.5D, 0.5D, 0.5D);

    if (renderState.renderedBlockState != null) {
      poseStack.translate(Parameters.DISPLAY_CASE.BLOCK.X_OFFSET, Parameters.DISPLAY_CASE.BLOCK.Y_OFFSET, Parameters.DISPLAY_CASE.BLOCK.Z_OFFSET);

      poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180));

      float scale = Parameters.DISPLAY_CASE.BLOCK.SCALE;
      poseStack.scale(scale, scale, scale);

      poseStack.translate(-0.5F, -0.5F, -0.5F);
      nodeCollector.submitBlock(poseStack, renderState.renderedBlockState, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    } else if (renderState.item != null) {
      poseStack.translate(Parameters.DISPLAY_CASE.ITEM.X_OFFSET, Parameters.DISPLAY_CASE.ITEM.Y_OFFSET, Parameters.DISPLAY_CASE.ITEM.Z_OFFSET);

      int dirIndex = renderState.facing.get2DDataValue();
      if (dirIndex >= 0 && dirIndex < 4) {
        poseStack.mulPose(ROTATIONS[dirIndex]);
      } else {
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
      }

      float scale = Parameters.DISPLAY_CASE.ITEM.SCALE;
      poseStack.scale(scale, scale, scale);

      renderState.item.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
    poseStack.popPose();
  }
}