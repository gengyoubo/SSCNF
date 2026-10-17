package github.com.gengyoubo.sscnf.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.power.TransformativeEffectService;

/** Shared attack hook for Fabric's transformative mob families. */
final class TransformativeMobEffects {
    private TransformativeMobEffects() { }

    static void tryApply(Entity target, float chance, String targetFormPath) {
        if (target instanceof ServerPlayer player && player.getRandom().nextFloat() < chance) {
            TransformativeEffectService.apply(player, ResourceLocation.fromNamespaceAndPath(
                    ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE, targetFormPath));
        }
    }
}
