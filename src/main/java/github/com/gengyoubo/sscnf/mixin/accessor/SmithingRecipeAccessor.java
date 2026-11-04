package github.com.gengyoubo.sscnf.mixin.accessor;

import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingMenu.class)
public interface SmithingRecipeAccessor {
    @Accessor("selectedRecipe") RecipeHolder<SmithingRecipe> ssc$getSelectedRecipe();
}
