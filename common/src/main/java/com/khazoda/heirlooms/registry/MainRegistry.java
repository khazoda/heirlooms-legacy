package com.khazoda.heirlooms.registry;

import com.khazoda.heirlooms.block.DisplayCaseBlock;
import com.khazoda.heirlooms.block.DisplayCaseBlockEntity;
import com.khazoda.heirlooms.block.DisplayRackBlock;
import com.khazoda.heirlooms.block.DisplayRackBlockEntity;
import com.khazoda.heirlooms.platform.Services;
import com.khazoda.heirlooms.registry.helper.Reggie;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
  private static final Reggie<BlockEntityType<?>> BLOCK_ENTITY_REGISTRAR = REGISTRARS.get(Registries.BLOCK_ENTITY_TYPE);

  public static final Supplier<DisplayCaseBlock> DISPLAY_CASE = registerBlock("display_case", DisplayCaseBlock::new,
          id -> BlockBehaviour.Properties.of().strength(2.0f, 6.0F).noOcclusion().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE)
                  .isSuffocating((state, world, pos) -> false)
                  .isViewBlocking((state, world, pos) -> false)
                  .setId(id));
  public static final Supplier<DisplayRackBlock> DISPLAY_RACK = registerBlock("display_rack", DisplayRackBlock::new,
          id -> BlockBehaviour.Properties.of().strength(1.5f,4.0F).noOcclusion().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)
                  .isSuffocating((state, world, pos) -> false)
                  .isViewBlocking((state, world, pos) -> false)
                  .setId(id));

  public static final Supplier<BlockItem> DISPLAY_CASE_ITEM = registerBlockItem("display_case", DISPLAY_CASE::get);
  public static final Supplier<BlockItem> DISPLAY_RACK_ITEM = registerBlockItem("display_rack", DISPLAY_RACK::get);

  public static final Supplier<BlockEntityType<DisplayCaseBlockEntity>> DISPLAY_CASE_BE = BLOCK_ENTITY_REGISTRAR.register("display_case",
          () -> Services.PLATFORM.createBlockEntityType(
                  DisplayCaseBlockEntity::new,
                  DISPLAY_CASE.get()
          )
  );
  public static final Supplier<BlockEntityType<DisplayRackBlockEntity>> DISPLAY_RACK_BE = BLOCK_ENTITY_REGISTRAR.register("display_rack",
          () -> Services.PLATFORM.createBlockEntityType(
                  DisplayRackBlockEntity::new,
                  DISPLAY_RACK.get()
          )
  );

  public static void init() {
  }

  /* =============[ Helper Methods ]============== */
  private static <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, Function<ResourceKey<Block>, BlockBehaviour.Properties> props) {
    return BLOCK_REGISTRAR.register(name, () -> factory.apply(props.apply(blockKey(name))));
  }

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
