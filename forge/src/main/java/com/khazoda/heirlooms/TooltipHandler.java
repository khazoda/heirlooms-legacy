package com.khazoda.heirlooms;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TooltipHandler {

  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    boolean pressedCTRL = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL);
    boolean pressedALT = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LALT);

    CommonTooltipHandler.handleTooltip(stack, event.getToolTip(), event.getFlags(), pressedCTRL, pressedALT);
  }
}