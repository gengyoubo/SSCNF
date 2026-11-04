package github.com.gengyoubo.sscnf.recipe.altar;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public interface AltarRecipe extends Recipe<AltarInput> {
    int getRecipeTime();
    int getFuelCost();
    default int getTotalFuelCost() { return -1; }
    default int fuelUsage(int progress) {
        int total = getTotalFuelCost();
        if (total < 0) return getFuelCost();
        return (int) (((long) (progress + 1) * total / getRecipeTime())
                - ((long) progress * total / getRecipeTime()));
    }
    Ingredient getCatalyst();
    boolean canCraft(net.minecraft.world.entity.player.Player player);
}
