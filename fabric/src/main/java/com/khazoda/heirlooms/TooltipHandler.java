package com.khazoda.heirlooms;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import java.util.List;

public class TooltipHandler implements ItemTooltipCallback {

  public static void register() {
    ItemTooltipCallback.EVENT.register(new TooltipHandler());
  }

  @Override
  public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> list) {
    boolean pressedCTRL = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LCONTROL);
    boolean pressedALT = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LALT);

    CommonTooltipHandler.handleTooltip(stack, list, tooltipFlag, pressedCTRL, pressedALT);
  }
}