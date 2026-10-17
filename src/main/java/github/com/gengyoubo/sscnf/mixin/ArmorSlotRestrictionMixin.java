package github.com.gengyoubo.sscnf.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.sscnf.power.MissingPowerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Applies form equipment restrictions to the native armor slot. */
@Mixin(targets = "net.minecraft.world.inventory.ArmorSlot")
public abstract class ArmorSlotRestrictionMixin extends Slot {
    protected ArmorSlotRestrictionMixin(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void ssc$preventRestrictedArmor(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!(this.container instanceof Inventory inventory)) return;
        Player player = inventory.player;
        EquipmentSlot slot = switch (this.getSlotIndex()) {
            case 39 -> EquipmentSlot.HEAD;
            case 38 -> EquipmentSlot.CHEST;
            case 37 -> EquipmentSlot.LEGS;
            case 36 -> EquipmentSlot.FEET;
            default -> null;
        };
        if (slot != null && MissingPowerEvents.isArmorRestricted(player, slot, stack)) {
            cir.setReturnValue(false);
        }
    }
}
