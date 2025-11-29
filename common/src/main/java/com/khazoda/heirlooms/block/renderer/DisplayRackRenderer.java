package com.khazoda.heirlooms.block.renderer;

import com.khazoda.heirlooms.block.DisplayRackBlock;
import com.khazoda.heirlooms.block.DisplayRackBlockEntity;
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

public class DisplayRackRenderer implements BlockEntityRenderer<DisplayRackBlockEntity, DisplayRackRenderState> {

  private final ItemModelResolver itemModelResolver;

  public DisplayRackRenderer(BlockEntityRendererProvider.Context context) {
    this.itemModelResolver = context.itemModelResolver();
  }

  @Override
  public DisplayRackRenderState createRenderState() {
    return new DisplayRackRenderState();
  }

  @Override
  public void extractRenderState(DisplayRackBlockEntity blockEntity, DisplayRackRenderState renderState, float partialTick, Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
    renderState.facing = blockEntity.getBlockState().getValue(DisplayRackBlock.FACING);
    ItemStack stack = blockEntity.getItem(0);
    if (!stack.isEmpty()) {
      if(stack.getItem() instanceof BlockItem) renderState.isBlockItem = true;
      renderState.item = new ItemStackRenderState();
      this.itemModelResolver.updateForTopItem(renderState.item, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    } else {
      renderState.item = null;
    }
  }

  @Override
  public void submit(DisplayRackRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
    if (renderState.item == null) return;
    poseStack.pushPose();
    poseStack.translate(0.5D, 0.5D, 0.5D);
    if(renderState.isBlockItem) {
      poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180));
      poseStack.scale(0.5F, 0.5F, 0.5F);
    } else {
      poseStack.translate(0, -0.11D, 0);
      poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180));
      poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
      poseStack.scale(0.5F, 0.5F, 0.5F);
    }
    renderState.item.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    poseStack.popPose();
  }
}