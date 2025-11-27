package com.khazoda.heirlooms.registry;

import com.khazoda.heirlooms.HeirloomsCommon;
import com.khazoda.heirlooms.registry.helper.Reggie;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.khazoda.heirlooms.Constants.blockKey;
import static com.khazoda.heirlooms.Constants.itemKey;
import static com.khazoda.heirlooms.HeirloomsCommon.REGISTRARS;

public class MainRegistry {
  private static final Reggie<Block> BLOCK_REGISTRAR = REGISTRARS.get(Registries.BLOCK);
  private static final Reggie<Item> ITEM_REGISTRAR = REGISTRARS.get(Registries.ITEM);

  public static final Supplier<Block> DISPLAY_CASE = registerBlock("display_case", 2.5f, 0, MapColor.STONE, NoteBlockInstrument.BASEDRUM, SoundType.STONE);
  public static final Supplier<BlockItem> DISPLAY_CASE_ITEM = registerBlockItem("display_case", DISPLAY_CASE);


  public static void init() {
  }

  /* =============[ Helper Methods ]============== */
  /* Register default BlockItem from Block */
  private static Supplier<BlockItem> registerBlockItem(String name, Supplier<Block> block) {
    return ITEM_REGISTRAR.register(name, () -> new BlockItem(block.get(), new Item.Properties().useBlockDescriptionPrefix().setId(itemKey(name))));
  }

  /* Register Block with properties */
  private static Supplier<Block> registerBlock(String name, Function<ResourceKey<Block>, BlockBehaviour.Properties> props) {
    return BLOCK_REGISTRAR.register(name, () -> new Block(props.apply(blockKey(name))));
  }

  /* Register Block with dedicated Block supplier */
  private static Supplier<Block> registerBlock(String name, Supplier<Block> supplier) {
    return BLOCK_REGISTRAR.register(name, supplier);
  }
  /* Register Block with a bunch of predefined property parameters */
  private static Supplier<Block> registerBlock(String name, float destroyTime, float explosionResistance, MapColor mapColor, NoteBlockInstrument instrument, SoundType soundType) {
    return registerBlock(name, id -> BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance).mapColor(mapColor).instrument(instrument).sound(soundType).requiresCorrectToolForDrops().setId(id));
  }
}
