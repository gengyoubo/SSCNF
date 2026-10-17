package github.com.gengyoubo.sscnf.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import github.com.gengyoubo.sscnf.registry.ModEffects;

/** Keeps the visible entanglement tier proportional to remaining binding time. */
public final class EntangledEffect extends MobEffect {
    public static final int TICKS_PER_LEVEL = 20 * 5;
    public EntangledEffect() { super(MobEffectCategory.HARMFUL, 0x9F9F9F); }
    @Override public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return duration >= 1; }
    @Override public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(ModEffects.ENTANGLED);
        if (instance == null) return true;
        int targetLevel = instance.getDuration() / TICKS_PER_LEVEL;
        if (instance.getAmplifier() != targetLevel) {
            entity.removeEffect(ModEffects.ENTANGLED);
            entity.addEffect(new MobEffectInstance(ModEffects.ENTANGLED, instance.getDuration(), targetLevel));
        }
    
        return true;
    }
}
