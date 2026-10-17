package github.com.gengyoubo.sscnf.mixin;

import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerSweepingMixin {
    @ModifyVariable(method = "attack", at = @At("LOAD"), ordinal = 3)
    private boolean ssc$forceVanillaSweep(boolean original) {
        if (original) return true;
        Player player = (Player) (Object) this;
        final boolean[] force = {false};
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if ("shape-shifter-curse:always_sweeping".equals(FormPowerRegistry.typeOf(power))
                    && FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))) {
                force[0] = true;
            }
        });
        return force[0];
    }
}
