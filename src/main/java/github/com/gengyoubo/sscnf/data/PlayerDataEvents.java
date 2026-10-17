package github.com.gengyoubo.sscnf.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.other.cursedmoon.CursedMoonService;
import github.com.gengyoubo.sscnf.other.advancement.SscAdvancementTriggers;
import github.com.gengyoubo.sscnf.power.FormActivePowerService;
import github.com.gengyoubo.sscnf.power.InstinctService;
import github.com.gengyoubo.sscnf.power.BatAttachService;
import github.com.gengyoubo.sscnf.power.MovementPowerService;

@SuppressWarnings("deprecation")
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class PlayerDataEvents {
    private static final ResourceLocation PLAYER_FORM_ID = ResourceLocation.fromNamespaceAndPath(
            ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE,
            "player_form"
    );
    private static final ResourceLocation PLAYER_SKIN_ID = ResourceLocation.fromNamespaceAndPath(
            ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE,
            "player_skin"
    );

    private PlayerDataEvents() {
    }


    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        SscApi.copyPlayerData(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            SscAdvancementTriggers.ON_FIRST_JOIN_WITH_MOD.trigger(player);
            ModNetwork.sendFormSync(player);
            MovementPowerService.synchronizeForcedSneaking(player);
            ModNetwork.sendSkinSync(player);
            SscApi.currentForm(player).ifPresent(data -> ModNetwork.sendItemStores(player, data.getItemStores()));
            CursedMoonService.sendDaySync(player);
            FormActivePowerService.synchronizeMana(player);
            InstinctService.synchronizeHud(player);
        }
    }

    @SubscribeEvent
    public static void playerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            BatAttachService.clear(player);
            github.com.gengyoubo.sscnf.power.PowerAnimationService.stop(player);
            ModNetwork.sendFormSync(player);
            MovementPowerService.synchronizeForcedSneaking(player);
            ModNetwork.sendSkinSync(player);
            SscApi.currentForm(player).ifPresent(data -> ModNetwork.sendItemStores(player, data.getItemStores()));
            CursedMoonService.sendDaySync(player);
            FormActivePowerService.synchronizeMana(player);
            InstinctService.synchronizeHud(player);
        }
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            BatAttachService.clear(player);
            ModNetwork.sendFormSync(player);
            MovementPowerService.synchronizeForcedSneaking(player);
            ModNetwork.sendSkinSync(player);
            SscApi.currentForm(player).ifPresent(data -> ModNetwork.sendItemStores(player, data.getItemStores()));
            CursedMoonService.sendDaySync(player);
            FormActivePowerService.synchronizeMana(player);
            InstinctService.synchronizeHud(player);
        }
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer tracker
                && event.getTarget() instanceof net.minecraft.server.level.ServerPlayer target) {
            ModNetwork.sendFormSyncTo(target, tracker);
            MovementPowerService.synchronizeForcedSneakingTo(target, tracker);
            ModNetwork.sendSkinSyncTo(target, tracker);
        }
    }
}
