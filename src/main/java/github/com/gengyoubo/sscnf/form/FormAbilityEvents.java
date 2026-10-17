package github.com.gengyoubo.sscnf.form;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;
import github.com.gengyoubo.sscnf.power.CrawlingScaleService;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.GAME)
public final class FormAbilityEvents {
    private static final int EFFECT_DURATION = 80;

    private FormAbilityEvents() {
    }
    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void resize(EntityEvent.Size event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        FormDefinition form = FormManager.current(player);

        event.setNewSize(CrawlingScaleService.expectedDimensions(player, event.getPose()));

        event.setNewSize(event.getNewSize().withEyeHeight(CrawlingScaleService.expectedEyeHeight(player, event.getPose())));
    }

    @SubscribeEvent
    public static void tick(net.neoforged.neoforge.event.tick.EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        FormDefinition form = FormManager.current(player);
        if (!player.level().isClientSide && player.tickCount % 40 == 0) {
            applyEffect(player, form, "night_vision", MobEffects.NIGHT_VISION);
            applyEffect(player, form, "water_breathing", MobEffects.WATER_BREATHING);
        }

        Vec3 movement = player.getDeltaMovement();
        if (form.hasFlag("slow_fall") && !player.onGround() && movement.y < -0.08D) {
            player.setDeltaMovement(movement.x, -0.08D, movement.z);
            player.resetFallDistance();
        }
        if (form.hasFlag("climb") && player.horizontalCollision && !player.onGround()) {
            player.setDeltaMovement(movement.x, Math.max(movement.y, -0.15D), movement.z);
            player.resetFallDistance();
        }

        updateStepHeight(player, form);
        // Sneak toggles do not refresh dimensions by themselves; without this the
        // crawling scale would stick after release instead of returning to default.
        CrawlingScaleService.tick(player);
    }

    @SubscribeEvent
    public static void fall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            FormDefinition form = FormManager.current(player);
            if (form.hasFlag("slow_fall")) {
                event.setDistance(0.0F);
                event.setDamageMultiplier(0.0F);
            } else if (form.fallProtectionDistance() > 0.0F && !hasDataDrivenFallProtection(player)) {
                event.setDistance(Math.max(0.0F, event.getDistance() - form.fallProtectionDistance()));
            }
        }
    }

    private static boolean hasDataDrivenFallProtection(Player player) {
        final boolean[] found = {false};
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if ("shape-shifter-curse:falling_protection".equals(FormPowerRegistry.typeOf(power))
                    && FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))) {
                found[0] = true;
            }
        });
        return found[0];
    }

    private static void applyEffect(Player player, FormDefinition form, String flag, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
        if (form.hasFlag(flag)) {
            player.addEffect(new MobEffectInstance(effect, EFFECT_DURATION, 0, true, false, false));
        }
    }

    @SubscribeEvent
    public static void jump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        float addition = FormManager.current(player).jumpVelocityAddition();
        if (addition > 0.0F) {
            Vec3 movement = player.getDeltaMovement();
            player.setDeltaMovement(movement.x, movement.y + addition, movement.z);
        }
    }

    @SubscribeEvent
    public static void effectMaintenance(net.neoforged.neoforge.event.tick.EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        if (FormManager.current(player).hasFlag("poison_immune")) {
            player.removeEffect(MobEffects.POISON);
        }
    }

    private static void updateStepHeight(Player player, FormDefinition form) {
        float multiplier = 1.0F;
        String path = form.id().getPath();
        if (path.equals("spider_3")) {
            multiplier = 2.0F;
        } else if (path.equals("ocelot_3") && player.isCrouching()) {
            multiplier = 2.0F;
        } else if (path.equals("axolotl_3") && player.isSprinting()) {
            multiplier = 2.0F;
        }

        final float[] powerMultiplier = {multiplier};
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if (!"shape-shifter-curse:modify_step_height".equals(FormPowerRegistry.typeOf(power))
                    || (player.isCrouching() && power.has("affect_sneak") && !power.get("affect_sneak").getAsBoolean())
                    || !FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))) return;
            powerMultiplier[0] = Math.max(powerMultiplier[0],
                    FormPowerRuntime.floatValue(power, "step_height_scale", 1.0F));
        });

        float target = 0.6F * powerMultiplier[0];
        if (Math.abs(player.maxUpStep() - target) > 0.001F) {
            var attribute = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.STEP_HEIGHT);
            if (attribute != null) attribute.setBaseValue(target);
        }
    }
}
