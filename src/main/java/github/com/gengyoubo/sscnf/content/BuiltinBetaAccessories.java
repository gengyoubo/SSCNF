package github.com.gengyoubo.sscnf.content;

import com.google.gson.JsonParser;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.util.AccessoryPowers;
import github.com.gengyoubo.sscnf.util.AccessoryPowers.AccessoryPowerData;
import github.com.gengyoubo.sscnf.util.AccessoryPowers.Changes;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Map;

/** Ported beta accessory definitions whose power types are implemented by the NeoForge runtime. */
final class BuiltinBetaAccessories {
    private static ResourceLocation id(String value) { return ResourceLocation.parse(value); }
    static void register() {
        FormPowerRegistry.register(id("shape-shifter-curse:form_bat_charm_of_night_crystal_trinket_plus"), JsonParser.parseString("{\"type\":\"shape_shifter_curse:action_over_time\",\"interval\":20,\"entity_action\":{\"type\":\"shape_shifter_curse:apply_effect\",\"effect\":{\"effect\":\"minecraft:resistance\",\"duration\":40,\"amplifier\":0}},\"condition\":{\"type\":\"shape_shifter_curse:in_block\",\"block_condition\":{\"type\":\"shape_shifter_curse:light_level\",\"comparison\":\"<=\",\"compare_to\":7}}}").getAsJsonObject());
        FormPowerRegistry.register(id("shape-shifter-curse:form_anubis_wolf_2_withered_bandage_trinket_plus"), JsonParser.parseString("{\"type\":\"shape_shifter_curse:self_action_when_hit\",\"entity_action\":{\"type\":\"shape_shifter_curse:apply_effect\",\"effect\":{\"effect\":\"minecraft:wither\",\"duration\":150,\"amplifier\":0,\"hide_particles\":false}},\"damage_condition\":{\"type\":\"shape_shifter_curse:attacker\"}}").getAsJsonObject());
        FormPowerRegistry.register(id("shape-shifter-curse:form_anubis_wolf_3_withered_bandage_trinket_plus"), JsonParser.parseString("{\"type\":\"shape_shifter_curse:self_action_when_hit\",\"entity_action\":{\"type\":\"shape_shifter_curse:apply_effect\",\"effect\":{\"effect\":\"minecraft:wither\",\"duration\":200,\"amplifier\":0,\"hide_particles\":false}},\"damage_condition\":{\"type\":\"shape_shifter_curse:attacker\"}}").getAsJsonObject());
        AccessoryPowers.registerAccessoryPower(id("shape-shifter-curse:charm_of_night_crystal_plus"), new AccessoryPowerData(List.of(), new Changes(List.of(), List.of()), Map.ofEntries(Map.entry(id("shape-shifter-curse:bat_0"), new Changes(List.of(id("shape-shifter-curse:form_bat_charm_of_night_crystal_trinket_plus")), List.of(id("shape-shifter-curse:form_bat_0_sun_health")))), Map.entry(id("shape-shifter-curse:bat_1"), new Changes(List.of(id("shape-shifter-curse:form_bat_charm_of_night_crystal_trinket_plus")), List.of(id("shape-shifter-curse:form_bat_1_sun_health")))), Map.entry(id("shape-shifter-curse:bat_2"), new Changes(List.of(id("shape-shifter-curse:form_bat_charm_of_night_crystal_trinket_plus")), List.of(id("shape-shifter-curse:form_bat_2_sun_health")))), Map.entry(id("shape-shifter-curse:bat_3"), new Changes(List.of(id("shape-shifter-curse:form_bat_charm_of_night_crystal_trinket_plus")), List.of(id("shape-shifter-curse:form_bat_2_sun_health")))))));
        AccessoryPowers.registerAccessoryPower(id("shape-shifter-curse:withered_bandage_plus"), new AccessoryPowerData(List.of(), new Changes(List.of(), List.of()), Map.ofEntries(Map.entry(id("shape-shifter-curse:anubis_wolf_0"), new Changes(List.of(), List.of())), Map.entry(id("shape-shifter-curse:anubis_wolf_1"), new Changes(List.of(), List.of())), Map.entry(id("shape-shifter-curse:anubis_wolf_2"), new Changes(List.of(id("shape-shifter-curse:form_anubis_wolf_2_withered_bandage_trinket_plus")), List.of(id("shape-shifter-curse:form_anubis_wolf_2_wither_on_hit")))), Map.entry(id("shape-shifter-curse:anubis_wolf_3"), new Changes(List.of(id("shape-shifter-curse:form_anubis_wolf_3_withered_bandage_trinket_plus")), List.of(id("shape-shifter-curse:form_anubis_wolf_3_wither_on_hit")))))));
    }
}
