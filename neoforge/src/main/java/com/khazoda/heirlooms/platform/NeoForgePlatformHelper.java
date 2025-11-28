package com.khazoda.heirlooms.platform;

import com.khazoda.heirlooms.block.DisplayCaseBlock;
import com.khazoda.heirlooms.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.util.Set;
import java.util.function.BiFunction;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> factory, DisplayCaseBlock displayCaseBlock) {
        return new BlockEntityType<>(factory::apply, Set.of(displayCaseBlock));
    }
}