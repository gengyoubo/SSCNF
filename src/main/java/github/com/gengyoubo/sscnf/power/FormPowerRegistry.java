package github.com.gengyoubo.sscnf.power;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.api.registry.SscJavaRegistries;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.form.FormRegistry;
import github.com.gengyoubo.sscnf.util.AccessoryPowers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import org.slf4j.Logger;

/**
 * Replaces Apoli's power registry for this mod only.  It reads the retained JSON directly from
 * the datapack, so normal Forge /reload also refreshes form powers without requiring Apoli.
 */
public final class FormPowerRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static volatile Map<ResourceLocation, FormPowerDefinition> powers = Map.of();
    private static volatile Map<ResourceLocation, FormPowerDefinition> dynamicPowers = Map.of();
    /** Server-authoritative base assignments mirrored to clients alongside the current form. */
    private static final Map<java.util.UUID, SyncedPowerSet> CLIENT_SYNCED_POWER_SETS = new java.util.HashMap<>();

    private FormPowerRegistry() {
    }

    public static FormPowerDefinition get(ResourceLocation id) {
        FormPowerDefinition dynamic = dynamicPowers.get(id);
        return dynamic == null ? powers.get(id) : dynamic;
    }

    public static Map<ResourceLocation, FormPowerDefinition> all() {
        Map<ResourceLocation, FormPowerDefinition> all = new LinkedHashMap<>(powers);
        all.putAll(dynamicPowers);
        return Collections.unmodifiableMap(all);
    }

    /** Compact server-side view used to diagnose the Forge-native power data pipeline. */
    public static DebugInfo debug(Player player) {
        List<ResourceLocation> assigned = idsFor(player);
        int resolved = (int) assigned.stream().filter(id -> get(id) != null || SscJavaRegistries.power(id).isPresent()).count();
        return new DebugInfo(all().size(), FormRegistry.forms().size(), FormManager.current(player).id(), assigned, resolved);
    }

    public static List<ResourceLocation> idsFor(Player player) {
        ResourceLocation formId = FormManager.current(player).id();
        List<ResourceLocation> assigned = idsForForm(formId);
        if (player.level().isClientSide) {
            SyncedPowerSet synced = CLIENT_SYNCED_POWER_SETS.get(player.getUUID());
            if (synced != null && synced.formId().equals(formId)) {
                assigned = synced.powerIds();
            }
        }
        return AccessoryPowers.effectivePowerIds(player, assigned);
    }

    /** Installs the server's base form-power assignment for a client player. */
    public static boolean applySyncedPowerIds(Player player, ResourceLocation formId,
                                              List<ResourceLocation> powerIds) {
        if (!player.level().isClientSide || formId == null) {
            return false;
        }
        SyncedPowerSet updated = new SyncedPowerSet(formId, List.copyOf(powerIds));
        SyncedPowerSet previous = CLIENT_SYNCED_POWER_SETS.put(player.getUUID(), updated);
        return !updated.equals(previous);
    }

    /** Discards client snapshots when its world changes. */
    public static void clearSyncedClientPowerIds() {
        CLIENT_SYNCED_POWER_SETS.clear();
    }

    private record SyncedPowerSet(ResourceLocation formId, List<ResourceLocation> powerIds) { }

    /** Power assignments for one resolved form, before player-specific accessory changes. */
    public static List<ResourceLocation> idsForForm(ResourceLocation formId) {
        if (FormRegistry.get(formId) == null) {
            return List.of();
        }
        return List.copyOf(SscJavaRegistries.powersForForm(formId));
    }

    public static void register(ResourceLocation id, JsonObject data) {
        if (powers.containsKey(id)) throw new IllegalStateException("Duplicate power: " + id);
        var updated = new LinkedHashMap<>(powers);
        updated.put(id, FormPowerDefinition.fromJson(id, data));
        powers = Collections.unmodifiableMap(updated);
    }

    private static void addAll(Set<ResourceLocation> target, List<ResourceLocation> values) {
        if (values != null) target.addAll(values);
    }

    public static boolean has(Player player, ResourceLocation id) {
        return idsFor(player).contains(id);
    }

    public static void visitActive(Player player, BiConsumer<ResourceLocation, JsonObject> visitor) {
        ResourceLocation formId = FormManager.current(player).id();
        List<ResourceLocation> ids = idsFor(player);

        //System.out.println("formId = " + formId);
        //System.out.println("power ids = " + ids);

        for (ResourceLocation id : ids) {
            FormPowerDefinition definition = get(id);
            //System.out.println("power = " + id + ", definition = " + definition);

            if (definition != null) {
                visitDefinition(id, definition.data(), visitor);
            }
        }
    }

    private static void visitDefinition(ResourceLocation id, JsonObject data,
                                        BiConsumer<ResourceLocation, JsonObject> visitor) {
        visitor.accept(id, data);
        if (!"shape_shifter_curse:multiple".equals(typeOf(data))) {
            return;
        }

        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
            if (entry.getValue().isJsonObject() && entry.getValue().getAsJsonObject().has("type")) {
                visitDefinition(id, entry.getValue().getAsJsonObject(), visitor);
            }
        }
    }

    public static String typeOf(JsonObject data) {
        return data.has("type") ? data.get("type").getAsString() : "";
    }

    public record DebugInfo(int loadedPowers, int assignedForms, ResourceLocation currentForm,
                            List<ResourceLocation> assignedPowers, int resolvedPowers) {
    }
}
