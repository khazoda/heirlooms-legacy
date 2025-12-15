package com.khazoda.heirlooms;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

  public static final String MOD_ID = "heirlooms";
  public static final String MOD_NAME = "Heirlooms";
  public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

  public static Identifier ID(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }

  public static ResourceKey<Block> blockKey(String name) {
    return ResourceKey.create(Registries.BLOCK, ID(name));
  }

  public static ResourceKey<Item> itemKey(String name) {
    return ResourceKey.create(Registries.ITEM, ID(name));
  }

  public static ResourceKey<Recipe<?>> recipeKey(String name) {
    return ResourceKey.create(Registries.RECIPE, ID(name));
  }
}