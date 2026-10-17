package github.com.gengyoubo.sscnf.integration.jei;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.recipe.altar.AltarRecipe;
import github.com.gengyoubo.sscnf.recipe.altar.AltarShapedRecipe;
import github.com.gengyoubo.sscnf.recipe.altar.AltarShapelessRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** JEI presentation data for the altar custom recipe types. */
public record MachineJeiRecipe(ResourceLocation id, List<Ingredient> grid, Ingredient catalyst,
                               ItemStack output, int recipeTime, int fuelCost) {
    public static List<MachineJeiRecipe> load(ResourceManager resources) {
        var minecraft = net.minecraft.client.Minecraft.getInstance();
        if (minecraft.level == null) return List.of();
        return minecraft.level.getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.value() instanceof AltarRecipe)
                .map(holder -> from(holder.id(), (AltarRecipe) holder.value(), minecraft.level.registryAccess()))
                .toList();
    }

    private static MachineJeiRecipe from(ResourceLocation id, AltarRecipe recipe, net.minecraft.core.HolderLookup.Provider registries) {
        NonNullList<Ingredient> grid = NonNullList.withSize(9, Ingredient.EMPTY);
        if (recipe instanceof AltarShapedRecipe shaped) {
            for (int y = 0; y < shaped.getHeight(); y++) {
                for (int x = 0; x < shaped.getWidth(); x++) {
                    grid.set(x + y * 3, shaped.ingredientAt(x, y));
                }
            }
        } else if (recipe instanceof AltarShapelessRecipe shapeless) {
            for (int i = 0; i < shapeless.getIngredients().size(); i++) grid.set(i, shapeless.getIngredients().get(i));
        }
        return new MachineJeiRecipe(id, List.copyOf(grid), recipe.getCatalyst(),
                recipe.getResultItem(registries), recipe.getRecipeTime(), recipe.getFuelCost());
    }
}
