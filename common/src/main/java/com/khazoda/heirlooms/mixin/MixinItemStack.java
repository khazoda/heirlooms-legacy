package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(method = "onCraftedBy", at = @At("HEAD"))
    private void heirlooms$onCraftedBy(Level level, Player player, int amount, CallbackInfo ci) {
        SlotResultModifier.handleCraftedItem(player, (ItemStack) (Object) this);
    }
}
