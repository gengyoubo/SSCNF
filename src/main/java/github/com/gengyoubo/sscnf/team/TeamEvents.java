package github.com.gengyoubo.sscnf.team;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;

/**
 * Refreshes sorcery team membership on login and periodically. Kept out of the
 * per-player power tick to avoid repeated scoreboard mutations.
 */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class TeamEvents {
    private static int tickCounter;

    private TeamEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerTeamHandler.updatePlayerTeam(player);
        }
    }

    @SubscribeEvent
    public static void onServerTick(net.neoforged.neoforge.event.tick.ServerTickEvent.Post event) {
if (++tickCounter % 100 != 0) {
            return;
        }
        var server = event.getServer();
        if (server == null) {
            return;
        }
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PlayerTeamHandler.updatePlayerTeam(player);
        }
    }
}