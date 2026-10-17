package github.com.gengyoubo.sscnf.mixin.client;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.client.NightVisionPowerLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Mirrors Apoli's night-vision fog condition without installing a potion effect. */
@Mixin(FogRenderer.class)
public abstract class NightVisionFogMixin {
    @Redirect(method = "setupColor", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z",
            ordinal = 0))
    private static boolean ssc$nightVisionFog(LivingEntity entity, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
        if (entity instanceof Player player && effect == MobEffects.NIGHT_VISION
                && !entity.hasEffect(MobEffects.NIGHT_VISION)
                && NightVisionPowerLookup.strength(player) >= 0.0F) {
            return true;
        }
        return entity.hasEffect(effect);
    }
}
