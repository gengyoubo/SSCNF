package github.com.gengyoubo.sscnf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.client.render.FormAnimationSystem;
import github.com.gengyoubo.sscnf.network.SyncFormPacket;
import github.com.gengyoubo.sscnf.other.config.SscClientConfig;

import java.util.HashSet;
import java.util.Set;

public final class FormSyncClientHandler {
    private static final Set<java.util.UUID> INITIAL_SYNCED_PLAYERS = new HashSet<>();
    private static ClientLevel syncedLevel;

    private FormSyncClientHandler() {
    }

    /** Applies a per-form default color on form change, mirroring onClientFormChange. */
    private static void applyDefaultFormColor(ResourceLocation form) {
        github.com.gengyoubo.sscnf.client.color.FormColorData data =
                github.com.gengyoubo.sscnf.client.color.FormColorData.client();
        if (!data.enableDefaultFormColor
                || !SscClientConfig
                        .CUSTOM_ENABLE_FORM_DEFAULT_COLOR_SYSTEM.get()) {
            return;
        }
        github.com.gengyoubo.sscnf.client.render.FormTextureUtils.ColorSetting def =
                data.formDefaultSetting.get(form);
        if (def == null) {
            return;
        }
        github.com.gengyoubo.sscnf.client.render.FormTextureUtils.ColorSetting abgr =
                github.com.gengyoubo.sscnf.client.color.FormColorData.argb2Abgr(def);
        net.neoforged.neoforge.network.PacketDistributor.sendToServer(
                new github.com.gengyoubo.sscnf.network.UpdateSkinPacket(false, false, false,
                        abgr.primaryColor(), abgr.accentColor1(), abgr.accentColor2(),
                        abgr.eyeColorA(), abgr.eyeColorB(), abgr.primaryGreyReverse(),
                        abgr.accent1GreyReverse(), abgr.accent2GreyReverse()));
    }

    public static void apply(SyncFormPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        if (syncedLevel != minecraft.level) {
            syncedLevel = minecraft.level;
            INITIAL_SYNCED_PLAYERS.clear();
            FormAnimationSystem.clearClientState();
            PowerAnimationClientHandler.ensureLevel(minecraft.level);
            github.com.gengyoubo.sscnf.power.FormPowerRegistry.clearSyncedClientPowerIds();
            github.com.gengyoubo.sscnf.power.MovementPowerService.clearClientForcedSneakingStates();
        }

        Entity entity = minecraft.level.getEntity(packet.entityId());
        if (entity instanceof Player player) {
            FormManager.applySyncedForm(player, packet.formId(), packet.groupId(), packet.tier(), packet.enabled(),
                    packet.assignedPowerIds());
            ResourceLocation syncedForm = ResourceLocation.tryParse(packet.formId());
            if (syncedForm != null) {
                github.com.gengyoubo.sscnf.client.color.FormColorData.client().unlockForm(syncedForm);
                applyDefaultFormColor(syncedForm);
            }
            // Login, respawn and tracking packets initialise the snapshot. Only a server
            // confirmed form change is allowed to start TransformingController's clip.
            if (packet.playTransformAnimation()) {
                FormAnimationSystem.startTransition(player, packet.previousFormId());
                INITIAL_SYNCED_PLAYERS.add(player.getUUID());
            } else if (INITIAL_SYNCED_PLAYERS.add(player.getUUID())
                    && !FormAnimationSystem.isTransitioning(player)) {
                FormAnimationSystem.prime(player);
            }
        }
    }
}
