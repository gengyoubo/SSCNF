package github.com.gengyoubo.sscnf.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import github.com.gengyoubo.sscnf.api.PlayerFormData;
import github.com.gengyoubo.sscnf.block.entity.FormAttunerBlockEntity;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** World-side Form Attuner; the Perk-tree UI is implemented separately. */
@SuppressWarnings("deprecation")
public final class FormAttunerBlock extends BaseEntityBlock implements BeaconBeamBlock {
    @Override protected com.mojang.serialization.MapCodec<FormAttunerBlock> codec() { return simpleCodec(FormAttunerBlock::new); }

    public FormAttunerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.PURPLE;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FormAttunerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                    BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FORM_ATTUNER.get(), FormAttunerBlockEntity::tick);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof FormAttunerBlockEntity attuner
                && player instanceof ServerPlayer serverPlayer) {
            FormAttunerBlockEntity.rememberUser(serverPlayer, pos);
            ModNetwork.sendOpenFormAttuner(serverPlayer, attuner.getAttunementLevel(),
                    FormAttunerBlockEntity.getMaxLevel(),
                    SscApi.currentForm(serverPlayer).map(PlayerFormData::getFormGroupId).orElse(""));
        }
        return InteractionResult.CONSUME;
    }
}
