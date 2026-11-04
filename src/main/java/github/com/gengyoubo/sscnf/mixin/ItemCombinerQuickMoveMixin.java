package github.com.gengyoubo.sscnf.mixin;

import github.com.gengyoubo.sscnf.items.MorphScaleItems;
import github.com.gengyoubo.sscnf.other.config.GameplayConfigState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerQuickMoveMixin {
    @Unique private ItemStack ssc$quickMoveOutput = ItemStack.EMPTY;
    @Inject(method = "quickMoveStack", at = @At("HEAD"))
    private void ssc$rememberOutput(Player player, int index, CallbackInfoReturnable<ItemStack> callback) {
        ssc$quickMoveOutput = ItemStack.EMPTY;
        if ((Object) this instanceof SmithingMenu menu && index == menu.getResultSlot() && GameplayConfigState.fullStackUpgrade()) {
            ItemStack output = menu.getSlot(index).getItem();
            var recipe = ((github.com.gengyoubo.sscnf.mixin.accessor.SmithingRecipeAccessor) menu).ssc$getSelectedRecipe();
            if (output.getCount() > 1 && recipe != null
                    && recipe.value() instanceof github.com.gengyoubo.sscnf.recipe.MorphScaleUpgradeRecipe)
                ssc$quickMoveOutput = output.copy();
        }
    }
    @Redirect(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V"))
    private void ssc$reportTransferredCount(Slot slot, Player player, ItemStack remaining) {
        if (!ssc$quickMoveOutput.isEmpty()) {
            int transferred = ssc$quickMoveOutput.getCount() - remaining.getCount();
            slot.onTake(player, ssc$quickMoveOutput.copyWithCount(Math.max(0, transferred)));
            ssc$quickMoveOutput = ItemStack.EMPTY;
        } else slot.onTake(player, remaining);
    }
}
