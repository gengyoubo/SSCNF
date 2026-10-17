package github.com.gengyoubo.sscnf.recipe.altar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import github.com.gengyoubo.sscnf.registry.ModRecipeSerializers;

public class AltarShapelessRecipe implements AltarRecipe {
    private final ResourceLocation id;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final Ingredient catalyst;
    private final int recipeTime;
    private final int fuelCost;
    private final ResourceLocation requireAdvancement;

    public AltarShapelessRecipe(ResourceLocation id, ItemStack result, NonNullList<Ingredient> ingredients, Ingredient catalyst, int recipeTime, int fuelCost, ResourceLocation requireAdvancement) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
        this.catalyst = catalyst;
        this.recipeTime = recipeTime;
        this.fuelCost = fuelCost;
        this.requireAdvancement = requireAdvancement;
    }

    @Override public int getRecipeTime() { return recipeTime; }
    @Override public int getFuelCost() { return fuelCost; }
    @Override public Ingredient getCatalyst() { return catalyst; }

    @Override
    public boolean canCraft(Player player) {
        if (requireAdvancement == null) return true;
        if (!(player instanceof net.minecraft.server.level.ServerPlayer sp)) return false;
        var server = sp.getServer();
        if (server == null) return false;
        var adv = server.getAdvancements().get(requireAdvancement);
        if (adv == null) return false;
        var progress = sp.getAdvancements().getOrStartProgress(adv);
        return progress.isDone();
    }

    @Override
    public boolean matches(AltarInput inv, Level level) {
        if (catalyst != null && !catalyst.isEmpty()) {
            ItemStack catStack = inv.getItem(9);
            if (!catalyst.test(catStack)) return false;
        }
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack s = inv.getItem(i);
            if (!s.isEmpty()) inputs.add(s);
        }
        if (inputs.size() != ingredients.size()) return false;
        java.util.List<Ingredient> remaining = new java.util.ArrayList<>(ingredients);
        for (ItemStack stack : inputs) {
            boolean matched = false;
            var it = remaining.iterator();
            while (it.hasNext()) {
                Ingredient ing = it.next();
                if (ing.test(stack)) { it.remove(); matched = true; break; }
            }
            if (!matched) return false;
        }
        return remaining.isEmpty();
    }

    // Fabric uses StackedContents helper via RecipeMatcher, we simplify by using shapeless check
    @Override public boolean canCraftInDimensions(int w, int h) { return w * h >= ingredients.size(); }

    @Override public ItemStack assemble(AltarInput inv, net.minecraft.core.HolderLookup.Provider access) { return result.copy(); }
    @Override public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider access) { return result.copy(); }
    public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipeSerializers.ALTAR_SHAPELESS.get(); }
    @Override public RecipeType<?> getType() { return ModRecipeSerializers.ALTAR_SHAPELESS_TYPE.get(); }

    public NonNullList<Ingredient> getIngredients() { return ingredients; }

    private AltarShapelessRecipe(java.util.List<Ingredient> ingredients, ItemStack result, java.util.Optional<Ingredient> catalyst, int time, int fuel, java.util.Optional<ResourceLocation> advancement) {
        this.id = ResourceLocation.parse("shape-shifter-curse:altar"); this.ingredients = NonNullList.create(); this.ingredients.addAll(ingredients);
        if (ingredients.isEmpty() || ingredients.size() > 9) throw new IllegalArgumentException("Altar requires 1 to 9 ingredients");
        this.result = result; this.catalyst = catalyst.orElse(Ingredient.EMPTY); this.recipeTime = time; this.fuelCost = fuel; this.requireAdvancement = advancement.orElse(null);
    }
    public static final class Serializer implements RecipeSerializer<AltarShapelessRecipe> {
        private static final com.mojang.serialization.MapCodec<AltarShapelessRecipe> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(r -> java.util.List.copyOf(r.ingredients)),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("catalyst").forGetter(r -> java.util.Optional.ofNullable(r.catalyst).filter(i -> !i.isEmpty())),
            com.mojang.serialization.Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("time", 200).forGetter(r -> r.recipeTime),
            com.mojang.serialization.Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("fuel_cost", 1).forGetter(r -> r.fuelCost),
            ResourceLocation.CODEC.optionalFieldOf("require_advancement").forGetter(r -> java.util.Optional.ofNullable(r.requireAdvancement))
        ).apply(instance, AltarShapelessRecipe::new));
        @Override public com.mojang.serialization.MapCodec<AltarShapelessRecipe> codec() { return CODEC; }
        @Override public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, AltarShapelessRecipe> streamCodec() { return net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistries(CODEC.codec()); }
    }
}
