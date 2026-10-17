package github.com.gengyoubo.sscnf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.client.color.FormColorData;
import github.com.gengyoubo.sscnf.client.render.FormTextureUtils;
import github.com.gengyoubo.sscnf.other.config.SscClientConfig;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.network.UpdateSkinPacket;

/**
 * Pushes local color preferences up once shortly after joining, mirroring the
 * delayed sendUpdateCustomSetting on Fabric. The server already pushes its
 * authoritative state down on login; this covers auto_sync the other way.
 */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, value = Dist.CLIENT)
public final class ColorAutoSync {
    private static ClientLevel lastLevel;
    private static int ticksInLevel;

    private ColorAutoSync() {
    }

    @SubscribeEvent
    public static void tick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            lastLevel = null;
            ticksInLevel = 0;
            return;
        }
        if (minecraft.level != lastLevel) {
            lastLevel = minecraft.level;
            ticksInLevel = 0;
        }
        if (++ticksInLevel != 20) {
            return;
        }
        if (!SscClientConfig.CUSTOM_AUTO_SYNC_CONFIG.get()) {
            return;
        }
        boolean keep = SscClientConfig.CUSTOM_KEEP_ORIGINAL_SKIN.get();
        boolean enable = SscClientConfig.CUSTOM_ENABLE_FORM_COLOR.get();
        FormTextureUtils.ColorSetting colors;
        if (SscClientConfig.CUSTOM_AUTO_SYNC_COLOR_CONFIG.get()) {
            colors = FormColorData.argb2Abgr(new FormTextureUtils.ColorSetting(
                    SscClientConfig.CUSTOM_PRIMARY_COLOR.get(),
                    SscClientConfig.CUSTOM_ACCENT_COLOR_1.get(),
                    SscClientConfig.CUSTOM_ACCENT_COLOR_2.get(),
                    SscClientConfig.CUSTOM_EYE_COLOR_A.get(),
                    SscClientConfig.CUSTOM_EYE_COLOR_B.get(),
                    SscClientConfig.CUSTOM_PRIMARY_GREY_REVERSE.get(),
                    SscClientConfig.CUSTOM_ACCENT_1_GREY_REVERSE.get(),
                    SscClientConfig.CUSTOM_ACCENT_2_GREY_REVERSE.get()));
        } else {
            var skin = SscApi.currentSkin(minecraft.player).orElse(null);
            colors = skin == null ? null : skin.getFormColor();
            if (colors == null) {
                return;
            }
        }
        net.neoforged.neoforge.network.PacketDistributor.sendToServer(new UpdateSkinPacket(true, keep, enable,
                colors.primaryColor(), colors.accentColor1(), colors.accentColor2(),
                colors.eyeColorA(), colors.eyeColorB(), colors.primaryGreyReverse(),
                colors.accent1GreyReverse(), colors.accent2GreyReverse()));
    }
}
