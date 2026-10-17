package github.com.gengyoubo.sscnf.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public abstract class StatusEffectMixin {
    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    private void ssc$effectTick(LivingEntity target, int amplifier, CallbackInfoReturnable<Boolean> ci) {
        if (ssc$applyScaledEffect(target, null, null, amplifier, 1.0D, false)) ci.setReturnValue(true);
    }

    @Inject(method = "applyInstantenousEffect", at = @At("HEAD"), cancellable = true)
    private void ssc$instantEffect(Entity source, Entity attacker, LivingEntity target,
                                   int amplifier, double proximity, CallbackInfo ci) {
        if (ssc$applyScaledEffect(target, source, attacker, amplifier, proximity, true)) ci.cancel();
    }

    private boolean ssc$applyScaledEffect(LivingEntity target, Entity source, Entity attacker,
                                          int amplifier, double proximity, boolean instant) {
        if (!(target instanceof Player player)) return false;
        net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect = net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder((MobEffect) (Object) this);
        boolean health = effect == MobEffects.HEAL;
        if (!health && effect != MobEffects.HARM) return false;
        String type = health ? "shape-shifter-curse:modify_instant_health_scale"
                : "shape-shifter-curse:modify_instant_damage_scale";
        final float[] scale = {1.0F};
        final boolean[] found = {false};
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if (!type.equals(FormPowerRegistry.typeOf(power))
                    || !FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))) return;
            found[0] = true;
            scale[0] *= FormPowerRuntime.floatValue(power, "scale", 1.0F);
        });
        if (!found[0]) return false;
        boolean undead = target.isInvertedHealAndHarm();
        int base = health ? (undead ? -6 : 4) : (undead ? 6 : -4);
        float value = base << amplifier;
        if (instant) value = (float) (proximity * value + 0.5D);
        value *= scale[0];
        if (value > 0.0F) target.heal(value);
        else if (value < 0.0F) target.hurt(instant && source != null && attacker != null
                ? target.damageSources().indirectMagic(source, attacker)
                : target.damageSources().magic(), -value);
        return true;
    }
}
