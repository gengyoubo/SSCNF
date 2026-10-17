package github.com.gengyoubo.sscnf.other.advancement;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import github.com.gengyoubo.sscnf.form.FormDefinition;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
public final class SscAdvancementTriggers {
    private static final net.neoforged.neoforge.registries.DeferredRegister<net.minecraft.advancements.CriterionTrigger<?>> TRIGGERS = net.neoforged.neoforge.registries.DeferredRegister.create(net.minecraft.core.registries.Registries.TRIGGER_TYPE, "shape-shifter-curse");
    public static final SscTrigger ON_ENABLE_MOD = register("on_enable_mod");
    public static final SscTrigger ON_END_CURSED_MOON = register("on_end_cursed_moon");
    public static final SscTrigger ON_END_CURSED_MOON_CURED = register("on_end_cursed_moon_cured");
    public static final SscTrigger ON_END_CURSED_MOON_CURED_FORM_2 = register("on_end_cursed_moon_cured_form_2");
    public static final SscTrigger ON_FIRST_JOIN_WITH_MOD = register("on_first_join_with_mod");
    public static final SscTrigger ON_GET_TRANSFORM_EFFECT = register("on_get_transform_effect");
    public static final SscTrigger ON_OPEN_BOOK_OF_SHAPE_SHIFTER = register("on_open_book_of_shape_shifter");
    public static final SscTrigger ON_SLEEP_WHEN_HAVE_TRANSFORM_EFFECT = register("on_sleep_when_have_transform_effect");
    public static final SscTrigger ON_TRANSFORM_BY_CATALYST = register("on_transform_by_catalyst");
    public static final SscTrigger ON_TRANSFORM_BY_CURE = register("on_transform_by_cure");
    public static final SscTrigger ON_TRANSFORM_BY_CURE_FINAL = register("on_transform_by_cure_final");
    public static final SscTrigger ON_TRANSFORM_EFFECT_FADE = register("on_transform_effect_fade");
    public static final SscTrigger ON_TRANSFORM_FORM = register("on_transform_form");
    public static final SscTrigger ON_TRIGGER_CURSED_MOON = register("on_trigger_cursed_moon");
    public static final SscTrigger ON_TRIGGER_CURSED_MOON_FORM_2 = register("on_trigger_cursed_moon_form_2");
    public static final SscTrigger ON_USE_GOLDEN_APPLE = register("on_use_golden_apple");
    public static final SscTrigger ON_WEB_ENTITY = register("on_web_entity");

    private SscAdvancementTriggers() {}
    public static void initialize(net.neoforged.bus.api.IEventBus bus) { TRIGGERS.register(bus); }
    private static SscTrigger register(String path) {
        SscTrigger trigger = new SscTrigger(); TRIGGERS.register(path, () -> trigger); return trigger;
    }
    public static final class SscTrigger extends SimpleCriterionTrigger<SscInstance> {
        @Override public Codec<SscInstance> codec() { return SscInstance.CODEC; }
        public void trigger(ServerPlayer player) { trigger(player, ignored -> true); }
        public void triggerForm(ServerPlayer player, FormDefinition form) { trigger(player, condition -> condition.matchesForm(form)); }
        public void triggerEntity(ServerPlayer player, ResourceLocation entity) { trigger(player, condition -> condition.entities().isEmpty() || condition.entities().contains(entity)); }
    }
    public record SscInstance(Optional<ContextAwarePredicate> player, List<ResourceLocation> forms, List<Integer> tiers, List<String> flags, List<String> notFlags, List<ResourceLocation> entities) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<SscInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(SscInstance::player),
            ResourceLocation.CODEC.listOf().optionalFieldOf("form", List.of()).forGetter(SscInstance::forms),
            Codec.INT.listOf().optionalFieldOf("form_tier", List.of()).forGetter(SscInstance::tiers),
            Codec.STRING.listOf().optionalFieldOf("flags", List.of()).forGetter(SscInstance::flags),
            Codec.STRING.listOf().optionalFieldOf("not_flags", List.of()).forGetter(SscInstance::notFlags),
            ResourceLocation.CODEC.listOf().optionalFieldOf("entity", List.of()).forGetter(SscInstance::entities)
        ).apply(instance, SscInstance::new));
        boolean matchesForm(FormDefinition form) {
            return (forms.isEmpty() || forms.contains(form.id())) && (tiers.isEmpty() || tiers.contains(form.stage()))
                && form.flags().containsAll(flags) && (notFlags.isEmpty() || !form.flags().containsAll(notFlags));
        }
    }
}
