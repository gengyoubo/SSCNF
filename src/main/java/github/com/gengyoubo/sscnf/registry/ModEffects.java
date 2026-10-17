package github.com.gengyoubo.sscnf.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.effect.FeedEffect;
import github.com.gengyoubo.sscnf.effect.EntangledEffect;
import github.com.gengyoubo.sscnf.effect.ImmobilityEffect;
import github.com.gengyoubo.sscnf.effect.TransformativeStatusEffect;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> IMMOBILITY = EFFECTS.register("immobility_effect", ImmobilityEffect::new);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> FEED = EFFECTS.register("feed_effect", FeedEffect::new);
    public static final DeferredHolder<MobEffect, ? extends MobEffect> ENTANGLED = EFFECTS.register("entangled_effect", EntangledEffect::new);
    private static final java.util.UUID MOD_SPEED_UUID = java.util.UUID.nameUUIDFromBytes(
            "ssc.entangled_full_speed".getBytes(java.nio.charset.StandardCharsets.UTF_8));
    private static final java.util.UUID MOD_KNOCKBACK_UUID = java.util.UUID.nameUUIDFromBytes(
            "ssc.entangled_full_knockback".getBytes(java.nio.charset.StandardCharsets.UTF_8));
    private static final java.util.UUID MOD_ATTACK_UUID = java.util.UUID.nameUUIDFromBytes(
            "ssc.entangled_full_attack_speed".getBytes(java.nio.charset.StandardCharsets.UTF_8));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ENTANGLED_FULL = EFFECTS.register("entangled_full_effect", () -> new MobEffect(MobEffectCategory.HARMFUL, 0xFFFFFF) {}
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.MOD_ID, MOD_SPEED_UUID.toString()), -1D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.MOD_ID, MOD_KNOCKBACK_UUID.toString()), 100D, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.MOD_ID, MOD_ATTACK_UUID.toString()), -0.8D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    // Transformative effects target FormRegistry IDs. The "form_" prefix belongs
    // to Origin resource IDs and is not part of the form ID.
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_BAT = transform("to_bat_0_effect", "bat_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_AXOLOTL = transform("to_axolotl_0_effect", "axolotl_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_OCELOT = transform("to_ocelot_0_effect", "ocelot_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_FAMILIAR_FOX = transform("to_familiar_fox_0_effect", "familiar_fox_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_SNOW_FOX = transform("to_snow_fox_0_effect", "snow_fox_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_ANUBIS_WOLF = transform("to_anubis_wolf_0_effect", "anubis_wolf_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_SPIDER = transform("to_spider_0_effect", "spider_0");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_ALLAY = transform("to_allay_sp_effect", "allay_sp");
    public static final DeferredHolder<MobEffect, ? extends MobEffect> TO_FERAL_CAT = transform("to_feral_cat_sp_effect", "feral_cat_sp");

    private static DeferredHolder<MobEffect, ? extends MobEffect> transform(String id, String form) { return EFFECTS.register(id, () -> new TransformativeStatusEffect(ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE, form))); }
    private ModEffects() {}
}
