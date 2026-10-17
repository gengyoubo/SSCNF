package github.com.gengyoubo.sscnf.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import github.com.gengyoubo.sscnf.util.PotionComponents;
import net.minecraft.world.item.alchemy.Potions;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class PotionStackMixin {
    @Shadow @Final public Container container;

    @Inject(method = "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I", at = @At("RETURN"), cancellable = true)
    private void ssc$modifyPotionStackSize(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!(container instanceof Inventory inventory) || !(stack.getItem() instanceof PotionItem)) return;
        boolean water = PotionComponents.getPotion(stack) == Potions.WATER;
        final int[] limit = {cir.getReturnValue()};
        FormPowerRegistry.visitActive(inventory.player, (id, power) -> {
            if (!"shape-shifter-curse:modify_potion_stack".equals(FormPowerRegistry.typeOf(power))
                    || !FormPowerRuntime.test(inventory.player, inventory.player, power.getAsJsonObject("condition"))
                    || FormPowerRuntime.booleanValue(power, "only_water_potion", false) && !water) return;
            limit[0] = Math.max(limit[0], FormPowerRuntime.intValue(power, "count", 1));
        });
        cir.setReturnValue(limit[0]);
    }
}
