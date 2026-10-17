package github.com.gengyoubo.sscnf.power;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.api.PlayerFormData;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Recovers form-power transitions if a caller updates form data without running FormManager's hooks. */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class FormPowerSyncGuard {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<UUID, String> LAST_RECONCILED_FORMS = new HashMap<>();

    private FormPowerSyncGuard() { }

    /** Call after the normal form-change hooks have run, preventing duplicate reconciliation next tick. */
    public static void markReconciled(Player player) {
        if (player.level().isClientSide) return;
        currentFormId(player).ifPresent(id -> LAST_RECONCILED_FORMS.put(player.getUUID(), id));
    }

    @SubscribeEvent
    public static void playerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide
                || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        String currentForm = currentFormId(player).orElse(null);
        if (currentForm == null) return;

        String previousForm = LAST_RECONCILED_FORMS.putIfAbsent(player.getUUID(), currentForm);
        if (previousForm == null || previousForm.equals(currentForm)) return;

        // Record first so re-entrant callbacks cannot run this recovery twice.
        LAST_RECONCILED_FORMS.put(player.getUUID(), currentForm);
        reconcileMissedTransition(player, previousForm, currentForm);
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_RECONCILED_FORMS.remove(event.getEntity().getUUID());
    }

    private static void reconcileMissedTransition(ServerPlayer player, String previousForm, String currentForm) {
        LOGGER.warn("Recovered missed form-power transition for {}: {} -> {}",
                player.getGameProfile().getName(), previousForm, currentForm);

        FormActivePowerService.onFormChanged(player);
        BatAttachService.clear(player);
        PowerAnimationService.stop(player);
        player.refreshDimensions();
        FormPowerEvents.onFormChanged(player);
        MissingPowerEvents.onFormChanged(player);
        InstinctService.applyImmediatePowers(player);

        ModNetwork.sendFormSync(player);
        MovementPowerService.synchronizeForcedSneaking(player);
        FormActivePowerService.synchronizeMana(player);
        InstinctService.synchronizeHud(player);
    }

    private static java.util.Optional<String> currentFormId(Player player) {
        return SscApi.currentForm(player)
                .map(PlayerFormData::getFormId)
                .filter(id -> id != null && !id.isBlank());
    }
}
