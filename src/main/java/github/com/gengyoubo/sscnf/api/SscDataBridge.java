package github.com.gengyoubo.sscnf.api;

import java.util.Optional;

import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.data.PlayerAttachments;

/**
 * Stable access point for SSCF player data.
 *
 * <p>SSCF currently resolves these values through Forge Capabilities. That is
 * an implementation detail and must remain isolated in this class so the
 * backend can later be replaced by a dual-loader adapter.</p>
 */
@SuppressWarnings("deprecation")
final class SscDataBridge {
    private SscDataBridge() {
    }

    static Optional<PlayerFormData> getFormData(Player player) {
        return Optional.of(player.getData(PlayerAttachments.PLAYER_FORM));
    }

    static Optional<PlayerSkinData> getSkinData(Player player) {
        return Optional.of(player.getData(PlayerAttachments.PLAYER_SKIN));
    }

    static void copyPlayerData(Player original, Player replacement) {
        getFormData(original).ifPresent(oldData ->
                getFormData(replacement).ifPresent(newData -> newData.copyFrom(oldData)));
        getSkinData(original).ifPresent(oldData ->
                getSkinData(replacement).ifPresent(newData -> newData.copyFrom(oldData)));
    }
}
