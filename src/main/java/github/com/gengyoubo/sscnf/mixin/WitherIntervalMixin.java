package github.com.gengyoubo.sscnf.mixin;

import github.com.gengyoubo.sscnf.power.PerkPowerService;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobEffectInstance.class)
public abstract class WitherIntervalMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;shouldApplyEffectTickThisTick(II)Z"))
    private boolean ssc$witherInterval(MobEffect effect, int duration, int amplifier, LivingEntity target, Runnable onExpiration) {
        boolean vanilla = effect.shouldApplyEffectTickThisTick(duration, amplifier);
        return effect == MobEffects.WITHER.value() && target instanceof Player player
                ? PerkPowerService.shouldApplyWither(player, duration, amplifier, vanilla) : vanilla;
    }
}
