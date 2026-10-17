package github.com.gengyoubo.sscnf.recipe.altar;

import com.google.gson.JsonElement;
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
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import github.com.gengyoubo.sscnf.registry.ModRecipeSerializers;

import java.util.HashMap;
import java.util.Map;

/**
 * Positional (shaped) counterpart of {@link AltarShapelessRecipe} for the Altar's
 * 3x3 grid (slots 0-8) plus catalyst slot 9. Pattern/key parsing follows the
 * vanilla shaped-recipe JSON format.
 */
public class AltarShapedRecipe implements AltarRecipe {
    private final ResourceLocation id;
    private final net.minecraft.world.item.crafting.ShapedRecipePattern pattern;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final Ingredient catalyst;
    private final int recipeTime;
    private final int fuelCost;
    private final ResourceLocation requireAdvancement;

    public AltarShapedRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients,
                             ItemStack result, Ingredient catalyst, int recipeTime, int fuelCost,
                             ResourceLocation requireAdvancement) {
        this.pattern = new net.minecraft.world.item.crafting.ShapedRecipePattern(width, height, ingredients, java.util.Optional.empty());
        this.id = id;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.result = result;
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
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Ingredient expected = ingredientAt(x, y);
                ItemStack stack = inv.getItem(x + y * 3);
                if (expected.isEmpty()) {
                    if (!stack.isEmpty()) return false;
                } else if (!expected.test(stack)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Ingredient ingredientAt(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return Ingredient.EMPTY;
        return ingredients.get(x + y * width);
    }

    @Override public boolean canCraftInDimensions(int w, int h) { return w >= width && h >= height; }

    @Override public ItemStack assemble(AltarInput inv, net.minecraft.core.HolderLookup.Provider access) { return result.copy(); }
    @Override public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider access) { return result.copy(); }
    public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipeSerializers.ALTAR_SHAPED.get(); }
    @Override public RecipeType<?> getType() { return ModRecipeSerializers.ALTAR_SHAPED_TYPE.get(); }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public NonNullList<Ingredient> getIngredients() { return ingredients; }

    private AltarShapedRecipe(net.minecraft.world.item.crafting.ShapedRecipePattern pattern, ItemStack result, java.util.Optional<Ingredient> catalyst, int time, int fuel, java.util.Optional<ResourceLocation> advancement) {
        this.pattern = pattern; this.id = ResourceLocation.parse("shape-shifter-curse:altar");
        this.width = pattern.width(); this.height = pattern.height(); this.ingredients = pattern.ingredients();
        this.result = result; this.catalyst = catalyst.orElse(Ingredient.EMPTY); this.recipeTime = time; this.fuelCost = fuel; this.requireAdvancement = advancement.orElse(null);
    }
    public static final class Serializer implements RecipeSerializer<AltarShapedRecipe> {
        private static final com.mojang.serialization.MapCodec<AltarShapedRecipe> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
            net.minecraft.world.item.crafting.ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("catalyst").forGetter(r -> java.util.Optional.ofNullable(r.catalyst).filter(i -> !i.isEmpty())),
            com.mojang.serialization.Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("time", 200).forGetter(r -> r.recipeTime),
            com.mojang.serialization.Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("fuel_cost", 1).forGetter(r -> r.fuelCost),
            ResourceLocation.CODEC.optionalFieldOf("require_advancement").forGetter(r -> java.util.Optional.ofNullable(r.requireAdvancement))
        ).apply(instance, AltarShapedRecipe::new));
        @Override public com.mojang.serialization.MapCodec<AltarShapedRecipe> codec() { return CODEC; }
        @Override public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, AltarShapedRecipe> streamCodec() { return net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistries(CODEC.codec()); }
    }
}
