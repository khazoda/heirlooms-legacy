package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.HeirloomsState;
import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class MixinAbstractContainerMenu {

    @Shadow
    public abstract Slot getSlot(int slotId);

    @Inject(method = "clicked", at = @At("HEAD"))
    private void heirlooms$onStackClicked(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        HeirloomsState.setCapturing(null);
        if (slotId < 0) return;

        try {
            Slot slot = this.getSlot(slotId);
            if (slot != null && slot.hasItem()) {
                ItemStack stack = slot.getItem();
                
                if (clickType == ClickType.QUICK_MOVE) {
                   if (!slot.mayPlace(stack) || slot instanceof ResultSlot || slot.container instanceof ResultContainer) {
                       // Apply NBT to the item in the slot and force an update.
                       SlotResultModifier.handleCraftedItem(player, stack);
                       slot.set(stack);
                       // Set capturing flag temporarily to add support for mods that bypass Slot.getItem()
                       // Any item entering the PlayerInventory via add or SetItem during this click event will be intercepted and tagged.
                       HeirloomsState.setCapturing(player);
                   }
                }
            }
        } catch (Exception ignored) {
            // Ignore index out of bounds or other errors
        }
    }

    /* Cleans up state immediately after click finishes */
    @Inject(method = "clicked", at = @At("RETURN"))
    private void heirlooms$onStackClickedReturn(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        HeirloomsState.setCapturing(null);
    }
}
