package github.com.gengyoubo.sscnf.mixin;

import github.com.gengyoubo.sscnf.other.config.SscCommonConfig;
import github.com.gengyoubo.sscnf.recipe.MorphScaleUpgradeRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class SmithingFullStackMixin {
    @Shadow private RecipeHolder<SmithingRecipe> selectedRecipe;
    @Shadow private void shrinkStackInSlot(int slot) { throw new AssertionError(); }
    @Unique private int ssc$takeCount;
    @Inject(method = "onTake", at = @At("HEAD"))
    private void ssc$rememberOutput(Player player, ItemStack output, CallbackInfo callback) {
        ssc$takeCount = selectedRecipe != null && selectedRecipe.value() instanceof MorphScaleUpgradeRecipe
                && SscCommonConfig.FULL_STACK_UPGRADE.get() ? output.getCount() : 1;
    }
    @Redirect(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/SmithingMenu;shrinkStackInSlot(I)V"))
    private void ssc$consumeBase(SmithingMenu menu, int slot) {
        if (slot != 1 || ssc$takeCount <= 1) { shrinkStackInSlot(slot); return; }
        ItemStack stack = menu.getSlot(slot).getItem();
        stack.shrink(Math.min(ssc$takeCount, stack.getCount()));
        menu.getSlot(slot).setChanged();
    }
}
