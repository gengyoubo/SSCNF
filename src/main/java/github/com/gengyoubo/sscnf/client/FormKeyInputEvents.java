package github.com.gengyoubo.sscnf.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.network.ActivePowerKeyPacket;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, value = Dist.CLIENT)
public final class FormKeyInputEvents {
    private static final Map<KeyMapping, Boolean> LAST_STATE = new HashMap<>();
    private static final ResourceLocation TOGGLE_CLIP_POWER = ResourceLocation.fromNamespaceAndPath(
            "shape-shifter-curse", "toggle_clip_at_ledge");
    private static boolean clipAtLedgeDisabled;

    private FormKeyInputEvents() {
    }

    @SubscribeEvent
    public static void clientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            clipAtLedgeDisabled = false;
            LAST_STATE.clear();
            return;
        } else if (!FormPowerRegistry.has(minecraft.player, TOGGLE_CLIP_POWER)) {
            clipAtLedgeDisabled = false;
        }
        for (KeyMapping key : FormKeyMappings.ACTIVE_SKILLS) {
            boolean pressed = key.isDown();
            boolean previous = LAST_STATE.getOrDefault(key, false);
            if (pressed != previous) {
                net.neoforged.neoforge.network.PacketDistributor.sendToServer(new ActivePowerKeyPacket(key.getName(), pressed));
                LAST_STATE.put(key, pressed);
                if (key == FormKeyMappings.TOGGLE_CLIP_AT_LEDGE && pressed
                        && minecraft.player != null
                        && FormPowerRegistry.has(minecraft.player, TOGGLE_CLIP_POWER)) {
                    clipAtLedgeDisabled = !clipAtLedgeDisabled;
                }
            }
        }
        KeyMapping jump = minecraft.options.keyJump;
        github.com.gengyoubo.sscnf.power.LevitatePowerService.tick(minecraft.player, jump.isDown());
        boolean pressed = jump.isDown();
        boolean previous = LAST_STATE.getOrDefault(jump, false);
        if (pressed != previous || pressed
                && github.com.gengyoubo.sscnf.power.LevitatePowerService.isAvailable(minecraft.player)) {
            net.neoforged.neoforge.network.PacketDistributor.sendToServer(new ActivePowerKeyPacket("key.jump", pressed));
            LAST_STATE.put(jump, pressed);
        }
        KeyMapping sprint = minecraft.options.keySprint;
        pressed = sprint.isDown();
        previous = LAST_STATE.getOrDefault(sprint, false);
        if (pressed != previous) {
            net.neoforged.neoforge.network.PacketDistributor.sendToServer(new ActivePowerKeyPacket("key.sprint", pressed));
            LAST_STATE.put(sprint, pressed);
        }
    }

    public static boolean isClipAtLedgeDisabled() {
        Minecraft minecraft = Minecraft.getInstance();
        return clipAtLedgeDisabled && minecraft.player != null
                && FormPowerRegistry.has(minecraft.player, TOGGLE_CLIP_POWER);
    }
}
