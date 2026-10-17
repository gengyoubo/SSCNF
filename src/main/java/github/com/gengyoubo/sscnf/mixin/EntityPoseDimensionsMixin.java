package github.com.gengyoubo.sscnf.mixin;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.power.CrawlingScaleService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/** Pose clearance uses the same bounds as the form's live collision box. */
@Mixin(Player.class)
public abstract class EntityPoseDimensionsMixin {
    @Inject(method = "canPlayerFitWithinBlocksAndEntitiesWhen", at = @At("HEAD"), cancellable = true)
    private void ssc$formPoseBounds(Pose pose, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player)(Object)this;
        var bounds = CrawlingScaleService.expectedDimensions(player, pose).makeBoundingBox(player.position()).deflate(1.0E-7D);
        cir.setReturnValue(player.level().noCollision(player, bounds));
    }
}
