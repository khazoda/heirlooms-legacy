package com.khazoda.heirlooms;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class TooltipHandler {

  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    boolean pressedCTRL = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LCONTROL);
    boolean pressedALT = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LALT);

    CommonTooltipHandler.handleTooltip(stack, event.getToolTip(), event.getFlags(), pressedCTRL, pressedALT);
  }
}