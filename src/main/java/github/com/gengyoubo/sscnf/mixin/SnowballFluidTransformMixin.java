package github.com.gengyoubo.sscnf.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import github.com.gengyoubo.sscnf.power.SpecialPowerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Mirrors Fabric's EntitySnowballTransformMixin injection points. */
@Mixin(Entity.class)
public abstract class SnowballFluidTransformMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void ssc$transformSnowballAtTick(CallbackInfo ci) {
        if ((Object) this instanceof Snowball snowball) {
            SpecialPowerEvents.transformSnowballFluid(snowball, true);
        }
    }

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("HEAD"))
    private void ssc$transformSnowballAtFluidUpdate(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Snowball snowball) {
            SpecialPowerEvents.transformSnowballFluid(snowball, false);
        }
    }
}
