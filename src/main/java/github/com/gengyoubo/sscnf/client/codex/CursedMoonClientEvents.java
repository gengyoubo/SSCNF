package github.com.gengyoubo.sscnf.client.codex;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;

/** Client-side warning message, matching Fabric's midday Cursed Moon notification. */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, value = Dist.CLIENT)
public final class CursedMoonClientEvents {
    private CursedMoonClientEvents() {
    }

    @SubscribeEvent
    public static void clientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        if (true) {
            CursedMoonData.clientTick(Minecraft.getInstance().level);
        }
    }
}
