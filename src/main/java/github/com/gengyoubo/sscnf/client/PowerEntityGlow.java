package github.com.gengyoubo.sscnf.client;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;

public final class PowerEntityGlow {
    private PowerEntityGlow() { }
    public static JsonObject matchingPower(Entity target) {
        var player = Minecraft.getInstance().player;
        if (player == null || player == target) return null;
        final JsonObject[] matched = {null};
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if (matched[0] == null && "shape_shifter_curse:entity_glow".equals(FormPowerRegistry.typeOf(power))
                    && FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))
                    && FormPowerRuntime.test(player, target, power.getAsJsonObject("bientity_condition"))) matched[0] = power;
        });
        return matched[0];
    }
}
