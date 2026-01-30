package com.khazoda.heirlooms.mixin;

import com.khazoda.heirlooms.registry.DataComponentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* This mixin is purely for migrating 1.20.1 nbt code to data component format */
@Mixin(ItemStack.class)
public class MixinItemStack {

  @Inject(method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V", at = @At("RETURN"))
  private void heirlooms$migrate(Holder<Item> item, int count, DataComponentPatch components, CallbackInfo ci) {
    ItemStack self = (ItemStack) (Object) this;
    if (self.isEmpty()) return;
    if (!self.has(DataComponents.CUSTOM_DATA)) return;

    CustomData customData = self.get(DataComponents.CUSTOM_DATA);
    if (customData != null) {
      CompoundTag nbt = customData.copyTag();
      boolean changed = false;

      changed |= heirlooms$migrate(self, nbt, "crafted_timestamp", DataComponentRegistry.CRAFTED_TIMESTAMP);
      changed |= heirlooms$migrate(self, nbt, "crafted_by", DataComponentRegistry.CRAFTED_BY);
      changed |= heirlooms$migrate(self, nbt, "enchanted_timestamp", DataComponentRegistry.ENCHANTED_TIMESTAMP);
      changed |= heirlooms$migrate(self, nbt, "enchanted_by", DataComponentRegistry.ENCHANTED_BY);

      if (changed) {
        if (nbt.isEmpty()) {
          self.remove(DataComponents.CUSTOM_DATA);
        } else {
          self.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        }
      }
    }
  }

  @Unique
  private boolean heirlooms$migrate(ItemStack self, CompoundTag nbt, String key, DataComponentType<String> type) {
    if (nbt.contains("heirlooms:" + key)) {
      self.set(type, nbt.getString("heirlooms:" + key));
      nbt.remove("heirlooms:" + key);
      return true;
    } else if (nbt.contains(key)) {
      self.set(type, nbt.getString(key));
      nbt.remove(key);
      return true;
    }
    return false;
  }
}
