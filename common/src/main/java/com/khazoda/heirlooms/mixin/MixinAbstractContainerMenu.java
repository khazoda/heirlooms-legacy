package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.HeirloomsState;
import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public abstract class MixinAbstractContainerMenu {

    @Unique
    private static final List<Class<? extends AbstractContainerMenu>> EXCLUDED_CLASSES = List.of(MerchantMenu.class, AnvilMenu.class, GrindstoneMenu.class);

    @Shadow
    public abstract Slot getSlot(int slotId);

    @WrapMethod(method = "clicked")
    private void heirlooms$captureOutput(int slotId, int button, ClickType clickType, Player player, Operation<Void> original) {
        HeirloomsState.setCapturing(null);
        try {
            // FIXME special handle grindstones, merchants and anvils
            if (clickType == ClickType.QUICK_MOVE && slotId >= 0 && player != null && EXCLUDED_CLASSES.stream().noneMatch(cls -> cls.isInstance(this))) {
                try {
                    Slot slot = this.getSlot(slotId);
                    if (slot != null && slot.hasItem()) {
                        ItemStack stack = slot.getItem();
                          if (!slot.mayPlace(stack) || slot instanceof ResultSlot || slot.container instanceof ResultContainer) {
                              // Apply NBT to the item in the slot and force an update.
                              SlotResultModifier.handleCraftedItem(player, stack);
                              slot.set(stack);
                              // Set capturing flag temporarily to add support for mods that bypass Slot.getItem()
                              // Any item entering the PlayerInventory via add or SetItem during this click event will be intercepted and tagged.
                              HeirloomsState.setCapturing(player);
                          }
                    }
                } catch (Exception ignored) {
                    // Ignore index out of bounds or other errors
                }
            }

            original.call(slotId, button, clickType, player);
        } finally {
            HeirloomsState.setCapturing(null);
        }
    }
}
