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
import net.minecraft.world.level.block.state.BlockState;
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
    renderState.renderedBlockState = null;
    renderState.item = null;
    renderState.isBlockItem = false;

    ItemStack stack = blockEntity.getItem(0);
    if (!stack.isEmpty()) {
      boolean useBlockRender = false;
      if (stack.getItem() instanceof BlockItem blockItem) {
        BlockState state = blockItem.getBlock().defaultBlockState();
        if (state.isCollisionShapeFullBlock(blockEntity.getLevel(), blockEntity.getBlockPos())) {
          renderState.renderedBlockState = state;
          useBlockRender = true;
        }
      }
      if (!useBlockRender) {
        renderState.item = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(renderState.item, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
      }

      renderState.isBlockItem = useBlockRender;
    }
  }

  @Override
  public void submit(DisplayRackRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
    if (renderState.renderedBlockState == null && renderState.item == null) return;

    poseStack.pushPose();
    poseStack.translate(0.5D, 0.5D, 0.5D);
    poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
    if (renderState.renderedBlockState != null) {
      poseStack.translate(Parameters.DISPLAY_RACK.BLOCK.X_OFFSET, Parameters.DISPLAY_RACK.BLOCK.Y_OFFSET, Parameters.DISPLAY_RACK.BLOCK.Z_OFFSET);
      poseStack.mulPose(Axis.XP.rotationDegrees(Parameters.DISPLAY_RACK.BLOCK.ROTATION));
      float scale = Parameters.DISPLAY_RACK.BLOCK.SCALE;
      poseStack.scale(scale, scale, scale);
      poseStack.translate(-0.5F, -0.5F, -0.5F);

      nodeCollector.submitBlock(poseStack, renderState.renderedBlockState, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    } else if (renderState.item != null) {
      poseStack.translate(Parameters.DISPLAY_RACK.ITEM.X_OFFSET, Parameters.DISPLAY_RACK.ITEM.Y_OFFSET, Parameters.DISPLAY_RACK.ITEM.Z_OFFSET);
      poseStack.mulPose(Axis.XP.rotationDegrees(Parameters.DISPLAY_RACK.ITEM.ROTATION));
      float scale = Parameters.DISPLAY_RACK.ITEM.SCALE;
      poseStack.scale(scale, scale, scale);
      renderState.item.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
    poseStack.popPose();
  }
}