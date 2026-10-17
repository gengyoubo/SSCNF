package github.com.gengyoubo.sscnf.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.server.level.ServerPlayer;
import github.com.gengyoubo.sscnf.api.PlayerFormData;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.block.entity.FormAttunerBlockEntity;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;

import java.util.Optional;
import java.util.Set;

public final class ModNetwork {
    private ModNetwork() {}
    public static void initialize(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(SyncFormPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> SyncFormPacket.encode(packet, buffer), SyncFormPacket::decode), SyncFormPacket::handle);
        registrar.playToServer(ActivePowerKeyPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> ActivePowerKeyPacket.encode(packet, buffer), ActivePowerKeyPacket::decode), ActivePowerKeyPacket::handle);
        registrar.playToClient(PowerAnimationPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> PowerAnimationPacket.encode(packet, buffer), PowerAnimationPacket::decode), PowerAnimationPacket::handle);
        registrar.playToServer(UpdateSkinPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> UpdateSkinPacket.encode(packet, buffer), UpdateSkinPacket::decode), UpdateSkinPacket::handle);
        registrar.playToClient(SyncSkinPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> SyncSkinPacket.encode(packet, buffer), SyncSkinPacket::decode), SyncSkinPacket::handle);
        registrar.playToServer(ValidateStartBookPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> ValidateStartBookPacket.encode(packet, buffer), ValidateStartBookPacket::decode), ValidateStartBookPacket::handle);
        registrar.playToClient(OpenSelectFormPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> OpenSelectFormPacket.encode(packet, buffer), OpenSelectFormPacket::decode), OpenSelectFormPacket::handle);
        registrar.playToServer(SetFormPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> SetFormPacket.encode(packet, buffer), SetFormPacket::decode), SetFormPacket::handle);
        registrar.playToClient(ModifyFcdPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> ModifyFcdPacket.encode(packet, buffer), ModifyFcdPacket::decode), ModifyFcdPacket::handle);
        registrar.playToClient(OpenColorMenuPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> OpenColorMenuPacket.encode(packet, buffer), OpenColorMenuPacket::decode), OpenColorMenuPacket::handle);
        registrar.playToClient(CursedMoonSyncPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> CursedMoonSyncPacket.encode(packet, buffer), CursedMoonSyncPacket::decode), CursedMoonSyncPacket::handle);
        registrar.playToClient(OpenFormAttunerPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> OpenFormAttunerPacket.encode(packet, buffer), OpenFormAttunerPacket::decode), OpenFormAttunerPacket::handle);
        registrar.playToServer(UnlockPerkPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> UnlockPerkPacket.encode(packet, buffer), UnlockPerkPacket::decode), UnlockPerkPacket::handle);
        registrar.playToClient(ManaSyncPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> ManaSyncPacket.encode(packet, buffer), ManaSyncPacket::decode), ManaSyncPacket::handle);
        registrar.playToClient(VirtualTotemPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> VirtualTotemPacket.encode(packet, buffer), VirtualTotemPacket::decode), VirtualTotemPacket::handle);
        registrar.playToClient(InstinctSyncPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> InstinctSyncPacket.encode(packet, buffer), InstinctSyncPacket::decode), InstinctSyncPacket::handle);
        registrar.playToClient(ItemStoresSyncPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> ItemStoresSyncPacket.encode(packet, buffer), ItemStoresSyncPacket::decode), ItemStoresSyncPacket::handle);
        registrar.playToClient(BatAttachStatePacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> BatAttachStatePacket.encode(packet, buffer), BatAttachStatePacket::decode), BatAttachStatePacket::handle);
        registrar.playToServer(BatDetachRequestPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> BatDetachRequestPacket.encode(packet, buffer), BatDetachRequestPacket::decode), BatDetachRequestPacket::handle);
        registrar.playToClient(TransformStatePacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> TransformStatePacket.encode(packet, buffer), TransformStatePacket::decode), TransformStatePacket::handle);
        registrar.playToClient(MovementLockPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> MovementLockPacket.encode(packet, buffer), MovementLockPacket::decode), MovementLockPacket::handle);
        registrar.playToClient(SyncForcedSneakingPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> SyncForcedSneakingPacket.encode(packet, buffer), SyncForcedSneakingPacket::decode), SyncForcedSneakingPacket::handle);
        registrar.playToServer(RequestPowerAnimationPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> RequestPowerAnimationPacket.encode(packet, buffer), RequestPowerAnimationPacket::decode), RequestPowerAnimationPacket::handle);
        registrar.playToClient(DietSyncPacket.TYPE, net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> DietSyncPacket.encode(packet, buffer), DietSyncPacket::decode), DietSyncPacket::handle);
    }

    public static void sendFormSync(ServerPlayer player) {
        sendFormSync(player, false);
    }

    /** A true marker is sent only by FormManager after a genuine server-side form change. */
    public static void sendFormSync(ServerPlayer player, boolean playTransformAnimation) {
        SscApi.currentForm(player).ifPresent(data -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
                packetFor(player, data, playTransformAnimation)
        ));
    }

    public static void sendFormSyncTo(ServerPlayer target, ServerPlayer receiver) {
        SscApi.currentForm(target).ifPresent(data -> net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(receiver,
                packetFor(target, data, false)
        ));
    }

    public static void sendForcedSneakingSync(ServerPlayer player, boolean forced) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
                new SyncForcedSneakingPacket(player.getId(), forced));
    }

    public static void sendForcedSneakingSyncTo(ServerPlayer target, ServerPlayer receiver, boolean forced) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(receiver,
                new SyncForcedSneakingPacket(target.getId(), forced));
    }

    public static void sendPowerAnimation(ServerPlayer player, PowerAnimationPacket packet) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, packet.forEntity(player.getId()));
    }

    public static void sendBatAttachState(ServerPlayer player, net.minecraft.core.BlockPos pos,
                                          net.minecraft.core.Direction side) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
                new BatAttachStatePacket(player.getUUID(), pos, side));
    }

    public static void sendTransformState(ServerPlayer player, boolean transforming,
                                          net.minecraft.resources.ResourceLocation startForm,
                                          net.minecraft.resources.ResourceLocation endForm) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
                new TransformStatePacket(player.getId(), transforming,
                        startForm == null ? null : startForm.toString(),
                        endForm == null ? null : endForm.toString()));
    }

    public static void sendMovementLock(ServerPlayer player, int noMoveTicks, int noJumpTicks) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new MovementLockPacket(noMoveTicks, noJumpTicks));
    }

    public static void sendPowerAnimationTo(ServerPlayer target, ServerPlayer receiver, PowerAnimationPacket packet) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(receiver, packet.forEntity(target.getId()));
    }

    public static void sendSkinSync(ServerPlayer player) {
        SscApi.currentSkin(player).ifPresent(data -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
                SyncSkinPacket.forPlayer(player, data)
        ));
    }

    public static void sendSkinSyncTo(ServerPlayer target, ServerPlayer receiver) {
        SscApi.currentSkin(target).ifPresent(data -> net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(receiver,
                SyncSkinPacket.forPlayer(target, data)
        ));
    }

    public static void sendCursedMoonSync(ServerPlayer player, boolean cursedMoonDay) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new CursedMoonSyncPacket(cursedMoonDay));
    }

    public static void sendManaSync(ServerPlayer player, String manaType, float mana, float maximum) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new ManaSyncPacket(manaType, mana, maximum));
    }

    public static void sendInstinctSync(ServerPlayer player, float value, float rate,
                                        boolean visible, boolean locked) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new InstinctSyncPacket(value, rate, visible, locked));
    }

    public static void sendItemStores(ServerPlayer player, net.minecraft.nbt.CompoundTag stores) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new ItemStoresSyncPacket(stores.copy()));
    }

    /** Plays the totem activation animation for a virtual totem's (possibly custom) stack. */
    public static void sendVirtualTotem(ServerPlayer player, net.minecraft.world.item.ItemStack stack) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new VirtualTotemPacket(stack));
    }

    /** Opens the form select menu for {@code player}, acting on {@code target}. */
    public static void sendOpenSelectForm(ServerPlayer player, Player target) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new OpenSelectFormPacket(target.getGameProfile().getName(), target.getUUID()));
    }

    public static void sendOpenFormAttuner(ServerPlayer player, int level, int maxLevel, String formGroupId) {
        sendOpenFormAttuner(player, level, maxLevel, formGroupId, "");
    }

    private static void sendOpenFormAttuner(ServerPlayer player, int level, int maxLevel, String formGroupId,
                                             String statusKey) {
        Set<String> unlocked = SscApi.currentForm(player).map(data -> data.getUnlockedPerks().stream()
                .map(ResourceLocation::toString).collect(java.util.stream.Collectors.toUnmodifiableSet())).orElse(Set.of());
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new OpenFormAttunerPacket(level, maxLevel, formGroupId == null ? "" : formGroupId, unlocked,
                        statusKey == null ? "" : statusKey));
    }

    /** Reopens/refreshes the client view after an authoritative Perk purchase. */
    public static void refreshFormAttuner(ServerPlayer player) {
        refreshFormAttuner(player, "");
    }

    public static void refreshFormAttuner(ServerPlayer player, String statusKey) {
        var attuner = FormAttunerBlockEntity.getLastUsed(player);
        if (attuner == null) return;
        sendOpenFormAttuner(player, attuner.getAttunementLevel(),
                FormAttunerBlockEntity.getMaxLevel(),
                SscApi.currentForm(player).map(PlayerFormData::getFormGroupId).orElse(""), statusKey);
    }


    private static SyncFormPacket packetFor(ServerPlayer player,
                                            PlayerFormData data,
                                            boolean playTransformAnimation) {
        ResourceLocation formId = ResourceLocation.tryParse(data.getFormId());
        var assignedPowerIds = formId == null ? java.util.List.<ResourceLocation>of()
                : FormPowerRegistry.idsForForm(formId);
        return new SyncFormPacket(player.getId(), data.getFormId(), data.getPreviousFormId(), data.getFormGroupId(),
                data.getFormTier(), data.isContentEnabled(), assignedPowerIds, playTransformAnimation);
    }
}
