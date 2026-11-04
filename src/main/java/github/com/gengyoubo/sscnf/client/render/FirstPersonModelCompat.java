package github.com.gengyoubo.sscnf.client.render;

import com.mojang.logging.LogUtils;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.form.FormBodyType;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.other.config.SscClientConfig;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, value = Dist.CLIENT)
public final class FirstPersonModelCompat {
    private static boolean resolved, newer, changed;
    private static Field instance;
    private static Method getConfig, enabled;
    private static int[] original;
    private FirstPersonModelCompat() {}

    @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
        if (!resolve()) return;
        Minecraft mc = Minecraft.getInstance();
        boolean active = SscClientConfig.CHANGE_FPM_CONFIG.get() && mc.player != null
                && mc.options.getCameraType().isFirstPerson() && isEnabled()
                && FormManager.current(mc.player).bodyType() == FormBodyType.FERAL;
        try {
            Object config = config();
            if (config == null) return;
            String[] names = {"xOffset", "sitXOffset", "sneakXOffset"};
            if (active && !changed) {
                original = new int[3];
                for (int i = 0; i < 3; i++) original[i] = config.getClass().getField(names[i]).getInt(config);
                changed = true;
            }
            if (active) for (String name : names) config.getClass().getField(name).setInt(config, newer ? 12 : -25);
            else if (changed) {
                for (int i = 0; i < 3; i++) config.getClass().getField(names[i]).setInt(config, original[i]);
                changed = false;
            }
        } catch (ReflectiveOperationException | RuntimeException ex) { LogUtils.getLogger().warn("First-person Model offset integration failed", ex); instance = null; }
    }
    public static boolean isEnabled() {
        if (!resolve()) return false;
        try { Object core = instance.get(null); return core != null && (boolean) enabled.invoke(core); }
        catch (ReflectiveOperationException | RuntimeException ex) { LogUtils.getLogger().warn("First-person Model integration failed", ex); instance = null; return false; }
    }
    private static Object config() throws ReflectiveOperationException {
        Object core = instance.get(null); return core == null ? null : getConfig.invoke(core);
    }
    private static boolean resolve() {
        if (resolved) return instance != null;
        resolved = true;
        if (!ModList.get().isLoaded("firstperson")) return false;
        try {
            Class<?> type = Class.forName("dev.tr7zw.firstperson.FirstPersonModelCore");
            instance = type.getField("instance"); getConfig = type.getMethod("getConfig"); enabled = type.getMethod("isEnabled");
            newer = ModList.get().getModContainerById("firstperson").map(mod -> mod.getModInfo().getVersion().compareTo(new DefaultArtifactVersion("2.6.0")) >= 0).orElse(false);
            return true;
        } catch (ReflectiveOperationException | RuntimeException ex) { LogUtils.getLogger().warn("First-person Model API unavailable", ex); instance = null; return false; }
    }
}
