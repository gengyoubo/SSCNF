package github.com.gengyoubo.sscnf.integration.legendarysurvivaloverhaul;

import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.minecraft.core.registries.BuiltInRegistries;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.api.SscPowerApi;

/** Optional water temperature immunity using LSO's registered effect, without LSO class linkage. */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class LegendarySurvivalOverhaulIntegration {
    private static final String MOD_ID = "legendarysurvivaloverhaul";
    private static final ResourceLocation TEMPERATURE_IMMUNITY =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "temperature_immunity");
    private static final Set<ResourceLocation> AXOLOTL_FORMS = Set.of(
            ResourceLocation.fromNamespaceAndPath("shape-shifter-curse", "axolotl_0"),
            ResourceLocation.fromNamespaceAndPath("shape-shifter-curse", "axolotl_1"),
            ResourceLocation.fromNamespaceAndPath("shape-shifter-curse", "axolotl_2"),
            ResourceLocation.fromNamespaceAndPath("shape-shifter-curse", "axolotl_3"));
    private static final int IMMUNITY_DURATION = 40;

    private LegendarySurvivalOverhaulIntegration() {
    }

    // LivingEntity has already ticked/expired effects at END. Reapply before LSO's
    // normal-priority temperature tick so immunity remains continuous while in water.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void tick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !player.isAlive() || !player.isInWaterOrBubble()) {
            return;
        }
        ModList mods = ModList.get();
        if (mods == null || !mods.isLoaded(MOD_ID)
                || !AXOLOTL_FORMS.contains(SscPowerApi.currentFormId(player))) {
            return;
        }
        net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> immunity = BuiltInRegistries.MOB_EFFECT.getHolder(TEMPERATURE_IMMUNITY).orElse(null);
        if (immunity == null || player.hasEffect(immunity)) {
            return;
        }
        // Never refresh, replace or remove an existing instance: external duration,
        // amplifier, visibility and hidden effects belong to their original source.
        // Leaving water or changing form simply stops granting this short effect.
        player.addEffect(new MobEffectInstance(immunity, IMMUNITY_DURATION, 0, false, false, false));
    }
}
