package github.com.gengyoubo.sscnf.other.config;

import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.network.GameplayConfigPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class GameplayConfigSync {
    private static Boolean lastFood, lastStack;
    private GameplayConfigSync() {}
    @SubscribeEvent public static void tick(ServerTickEvent.Post event) {
        var server = event.getServer();
        if (server.getTickCount() % 20 != 0) return;
        boolean food = SscCommonConfig.FOOD_HABIT.get(), stack = SscCommonConfig.FULL_STACK_UPGRADE.get();
        if (lastFood != null && lastFood == food && lastStack == stack) return;
        lastFood = food; lastStack = stack;
        var packet = new GameplayConfigPacket(food, stack);
        for (var player : server.getPlayerList().getPlayers()) PacketDistributor.sendToPlayer(player, packet);
    }
}
