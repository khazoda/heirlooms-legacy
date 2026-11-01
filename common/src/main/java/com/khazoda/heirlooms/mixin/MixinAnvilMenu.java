package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.mixinutils.SlotResultModifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {

    private MixinAnvilMenu(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void heirlooms$onItemSmithed(Player player, ItemStack stack, CallbackInfo ci) {
        var originalItem = this.inputSlots.getItem(AnvilMenu.INPUT_SLOT);

        if(!Objects.equals(originalItem.get(DataComponents.CUSTOM_NAME), stack.get(DataComponents.CUSTOM_NAME))) {
            // TODO handle rename
        }

        if(!Objects.equals(EnchantmentHelper.getEnchantmentsForCrafting(originalItem), EnchantmentHelper.getEnchantmentsForCrafting(stack))) {
            SlotResultModifier.handleEnchantedItem(player, stack);
        }
    }
}
