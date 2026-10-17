package github.com.gengyoubo.sscnf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.network.RequestPowerAnimationPacket;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, value = Dist.CLIENT)
public final class PowerAnimationClientEvents {
    private static final Set<Player> REQUESTED = Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void tick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        PowerAnimationClientHandler.ensureLevel(minecraft.level);
        if (minecraft.level == null || minecraft.getConnection() == null) {
            REQUESTED.clear();
            return;
        }
        for (Player player : minecraft.level.players()) {
            if (REQUESTED.add(player)) {
                net.neoforged.neoforge.network.PacketDistributor.sendToServer(new RequestPowerAnimationPacket(player.getId()));
            }
        }
    }

    @SubscribeEvent
    public static void leave(EntityLeaveLevelEvent event) {
        if (event.getLevel().isClientSide && event.getEntity() instanceof Player player) {
            REQUESTED.remove(player);
            PowerAnimationClientHandler.remove(player.getId());
        }
    }
}
