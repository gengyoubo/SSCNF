package github.com.gengyoubo.sscnf.content;
import static github.com.gengyoubo.sscnf.content.PowerParameters.*;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.api.registry.SscJavaRegistries;
import net.minecraft.resources.ResourceLocation;
final class BuiltinPowers23 {
    static void register() {
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:test_power"), object("type", "shape-shifter-curse:eat_entity", "food_map", array(object("entity", "minecraft:cod", "food", object("hunger", 8, "saturation", 0.8, "meat", true, "always_edible", true, "effects", array(object("effect", object("effect", "minecraft:regeneration", "duration", 80, "amplifier", 0, "show_particles", false, "show_icon", true), "chance", 1.0))))), "must_empty_hand", true, "condition", object("type", "shape_shifter_curse:sneaking", "inverted", true)));
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:toggle_clip_at_ledge"), object("type", "shape_shifter_curse:toggle", "active_by_default", false, "key", object("key", "key.shape-shifter-curse.active_skill_6")));
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:undead_group"), object("type", "shape_shifter_curse:entity_group", "group", "undead"));
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:velvet_paws"), object("type", "shape_shifter_curse:prevent_game_event", "event", "minecraft:step"));
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:water_vision"), object("type", "shape_shifter_curse:night_vision", "strength", 1.0, "condition", object("type", "shape_shifter_curse:submerged_in", "fluid", "minecraft:water")));
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:witch_friendly"), object("type", "shape-shifter-curse:witch_friendly", "condition", object("type", "shape_shifter_curse:constant", "value", true)));
        FormPowerRegistry.register(ResourceLocation.parse("shape-shifter-curse:witch_friendly_neutral"), object("type", "shape-shifter-curse:witch_friendly", "condition", object("type", "shape-shifter-curse:last_attack_witch_time", "comparison", ">=", "compare_to", "1200")));
    }
}
