package github.com.gengyoubo.sscnf.mixin;

import github.com.gengyoubo.sscnf.power.CombatPerkService;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerCombatPerkMixin {
    @Unique private boolean ssc$criticalAttack;
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/CommonHooks;fireCriticalHit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;ZF)Lnet/neoforged/neoforge/event/entity/player/CriticalHitEvent;"))
    private CriticalHitEvent ssc$captureCritical(Player player, Entity target, boolean vanilla, float multiplier) {
        CriticalHitEvent event = CommonHooks.fireCriticalHit(player, target, vanilla, multiplier);
        ssc$criticalAttack = event.isCriticalHit();
        return event;
    }
    @Redirect(method = "attack", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean ssc$primaryHit(Entity target, DamageSource source, float amount) {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide) return target.hurt(source, amount);
        float before = CombatPerkService.health(target);
        boolean critical = ssc$criticalAttack;
        boolean success = target.hurt(source, amount + CombatPerkService.bonus(player, target, critical));
        if (success && target instanceof LivingEntity living) {
            float dealt = before - CombatPerkService.health(target);
            CombatPerkService.fire(player, living, "melee", dealt);
            CombatPerkService.fire(player, living, critical ? "critical" : "non_critical_melee", dealt);
        }
        return success;
    }
}
