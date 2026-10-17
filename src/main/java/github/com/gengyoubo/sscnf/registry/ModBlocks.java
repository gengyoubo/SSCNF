package github.com.gengyoubo.sscnf.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE
    );

    public static final DeferredHolder<Block, ? extends Block> MOONDUST_CRYSTAL_GRIT = BLOCKS.register(
            "moondust_crystal_grit",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.6F))
    );

    public static final DeferredHolder<Block, ? extends Block> TEMP_WEB_BRIDGE = BLOCKS.register(
            "temp_web_bridge", () -> new TemporaryWebBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOL).strength(4.0F).noLootTable().randomTicks()
                    .noCollission().noOcclusion())
    );

    // --- 1.10.0 fabric parity: blocks that existed in fabric jar but were missing in forge port ---
    public static final DeferredHolder<Block, ? extends Block> DEW_COVERED_COBWEB = BLOCKS.register(
            "dew_covered_cobweb",
            () -> new github.com.gengyoubo.sscnf.block.DewCoveredCobwebBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).strength(0.4F)
                            .noCollission().sound(net.minecraft.world.level.block.SoundType.WOOL))
    );

    public static final DeferredHolder<Block, ? extends Block> WEB_COMPOSTER = BLOCKS.register(
            "web_composter",
            () -> new github.com.gengyoubo.sscnf.block.WebComposterBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F)
                            .sound(net.minecraft.world.level.block.SoundType.WOOD))
    );

    public static final DeferredHolder<Block, ? extends Block> ALTAR = BLOCKS.register(
            "altar",
            () -> new github.com.gengyoubo.sscnf.block.AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(net.minecraft.world.level.block.SoundType.WOOD).noOcclusion())
    );

    public static final DeferredHolder<Block, ? extends Block> FORM_ATTUNER = BLOCKS.register(
            "form_attuner",
            () -> new github.com.gengyoubo.sscnf.block.FormAttunerBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5F)
                            .sound(net.minecraft.world.level.block.SoundType.METAL).noOcclusion())
    );

    private ModBlocks() {
    }
}
