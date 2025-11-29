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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DisplayCaseRenderer implements BlockEntityRenderer<DisplayCaseBlockEntity, DisplayCaseRenderState> {

  private final ItemModelResolver itemModelResolver;

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
    ItemStack stack = blockEntity.getItem(0);
    if (!stack.isEmpty()) {
      renderState.item = new ItemStackRenderState();
      this.itemModelResolver.updateForTopItem(renderState.item, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    } else {
      renderState.item = null;
    }
  }

  @Override
  public void submit(DisplayCaseRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
    if (renderState.item == null) return;
    poseStack.pushPose();
    poseStack.translate(0.5D, 0.5D, 0.5D);
    poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180));
    poseStack.scale(0.5F, 0.5F, 0.5F);
    renderState.item.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    poseStack.popPose();
  }
}