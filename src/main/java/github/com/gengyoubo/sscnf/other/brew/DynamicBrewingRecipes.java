package github.com.gengyoubo.sscnf.other.brew;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.power.TransformativeEffectService;
import github.com.gengyoubo.sscnf.util.PotionComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class DynamicBrewingRecipes {
    private static final String TARGET_FORM = "targetForm";
    private static volatile List<Recipe> recipes = List.of();
    public static final IBrewingRecipe DELEGATE = new IBrewingRecipe() {
        @Override public boolean isInput(ItemStack input) { return recipes.stream().anyMatch(recipe -> recipe.matchesInput(input)); }
        @Override public boolean isIngredient(ItemStack ingredient) { return recipes.stream().anyMatch(recipe -> ingredient.is(recipe.ingredient)); }
        @Override public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            for (Recipe recipe : recipes) if (recipe.matchesInput(input) && ingredient.is(recipe.ingredient)) return recipe.output();
            return ItemStack.EMPTY;
        }
    };
    private DynamicBrewingRecipes() {}

    @SubscribeEvent public static void addReloadListener(AddReloadListenerEvent event) { event.addListener(new ReloadListener()); }
    @SubscribeEvent public static void finishPotion(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getItem().getItem() instanceof PotionItem)) return;
        var data = event.getItem().get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        ResourceLocation id = ResourceLocation.tryParse(data.copyTag().getString(TARGET_FORM));
        if (id != null) TransformativeEffectService.apply(player, id);
    }

    static void replace(Map<ResourceLocation, JsonElement> json) {
        List<Recipe> loaded = new ArrayList<>();
        json.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            try { loaded.add(parse(entry.getValue().getAsJsonObject())); }
            catch (RuntimeException ex) { ShapeShifterCurseNeoForge.LOGGER.warn("Invalid dynamic brewing recipe {}: {}", entry.getKey(), ex.getMessage()); }
        });
        recipes = List.copyOf(loaded);
        ShapeShifterCurseNeoForge.LOGGER.info("Loaded {} dynamic brewing recipes", recipes.size());
    }

    private static Recipe parse(JsonObject json) {
        String type = json.get("type").getAsString();
        ResourceLocation input = ResourceLocation.parse(json.get("input").getAsString());
        ResourceLocation ingredient = ResourceLocation.parse(json.get("ingredient").getAsString());
        ResourceLocation output = ResourceLocation.parse(json.get("output").getAsString());
        ResourceLocation target = json.has("target_form") ? ResourceLocation.parse(json.get("target_form").getAsString()) : null;
        var ingredientItem = BuiltInRegistries.ITEM.getOptional(ingredient).filter(item -> item != Items.AIR).orElseThrow();
        if ("potion".equals(type)) {
            var inputPotion = BuiltInRegistries.POTION.getHolder(input).orElseThrow();
            var outputPotion = BuiltInRegistries.POTION.getHolder(output).orElseThrow();
            return new Recipe(Items.POTION, ingredientItem, inputPotion, PotionComponents.setPotion(new ItemStack(Items.POTION), outputPotion), target);
        }
        if ("item".equals(type)) {
            var inputItem = BuiltInRegistries.ITEM.getOptional(input).filter(item -> item != Items.AIR).orElseThrow();
            var outputItem = BuiltInRegistries.ITEM.getOptional(output).filter(item -> item != Items.AIR).orElseThrow();
            return new Recipe(inputItem, ingredientItem, null, new ItemStack(outputItem), target);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
    private record Recipe(net.minecraft.world.item.Item input, net.minecraft.world.item.Item ingredient,
                          net.minecraft.core.Holder<Potion> potion, ItemStack result, ResourceLocation target) {
        boolean matchesInput(ItemStack stack) {
            return stack.is(input) && (potion == null || PotionComponents.getPotion(stack).equals(potion));
        }
        ItemStack output() {
            ItemStack stack = result.copy();
            if (target != null) CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(TARGET_FORM, target.toString()));
            return stack;
        }
    }
    private static final class ReloadListener extends SimpleJsonResourceReloadListener {
        private ReloadListener() { super(new Gson(), "dynamic_brewing_recipes"); }
        @Override protected void apply(Map<ResourceLocation, JsonElement> json, ResourceManager manager, ProfilerFiller profiler) { replace(json); }
    }
}
