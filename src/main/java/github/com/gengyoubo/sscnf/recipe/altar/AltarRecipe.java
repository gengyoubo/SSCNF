package github.com.gengyoubo.sscnf.recipe.altar;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public interface AltarRecipe extends Recipe<AltarInput> {
    int getRecipeTime();
    int getFuelCost();
    Ingredient getCatalyst();
    boolean canCraft(net.minecraft.world.entity.player.Player player);
}
