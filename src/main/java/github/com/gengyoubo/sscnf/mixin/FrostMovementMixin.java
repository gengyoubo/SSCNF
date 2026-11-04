package github.com.gengyoubo.sscnf.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import github.com.gengyoubo.sscnf.effect.FrostClawEffect;
import github.com.gengyoubo.sscnf.power.FrostDiveService;
import github.com.gengyoubo.sscnf.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class FrostMovementMixin {
    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    private Vec3 ssc$frostMove(Vec3 movement) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity living && living.hasEffect(ModEffects.FROST_CLAW)) {
            FrostClawEffect.beforeMove(living, movement);
        }
        if (self instanceof Player player && FrostDiveService.isDiving(player)) {
            player.resetFallDistance();
            return new Vec3(0, Math.min(movement.y, 0), 0);
        }
        return movement;
    }
}
