package github.com.gengyoubo.sscnf.client;

import net.minecraft.world.entity.player.Player;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;

/** Returns the strongest active Apoli night-vision power or -1 when absent. */
public final class NightVisionPowerLookup {
    private NightVisionPowerLookup() { }

    public static float strength(Player player) {
        final float[] strength = {-1.0F};
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if ("shape_shifter_curse:night_vision".equals(FormPowerRegistry.typeOf(power))
                    && FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))) {
                strength[0] = Math.max(strength[0], FormPowerRuntime.floatValue(power, "strength", 1.0F));
            }
        });
        return strength[0];
    }
}
