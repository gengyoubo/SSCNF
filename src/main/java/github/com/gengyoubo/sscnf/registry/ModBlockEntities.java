package github.com.gengyoubo.sscnf.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.block.entity.AltarBlockEntity;
import github.com.gengyoubo.sscnf.block.entity.FormAttunerBlockEntity;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);

    public static final java.util.function.Supplier<BlockEntityType<AltarBlockEntity>> ALTAR = BLOCK_ENTITIES.register("altar",
            () -> BlockEntityType.Builder.of(AltarBlockEntity::new, ModBlocks.ALTAR.get()).build(null));
    public static final java.util.function.Supplier<BlockEntityType<FormAttunerBlockEntity>> FORM_ATTUNER = BLOCK_ENTITIES.register("form_attuner",
            () -> BlockEntityType.Builder.of(FormAttunerBlockEntity::new, ModBlocks.FORM_ATTUNER.get()).build(null));

    private ModBlockEntities() {}
}
