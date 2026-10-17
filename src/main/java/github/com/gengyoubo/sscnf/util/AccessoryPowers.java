package github.com.gengyoubo.sscnf.util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.items.accessory.AccessoryItem;
import github.com.gengyoubo.sscnf.util.accessory.AccessoryUtils;
import java.util.*;
public final class AccessoryPowers {
    private AccessoryPowers() {}
    public interface CustomAccessoryPower { void onFormChange(ItemStack stack, AccessoryItem.SlotData slot, Player player); }
    public record Changes(List<ResourceLocation> add, List<ResourceLocation> remove) {
        public Changes { add = List.copyOf(add); remove = List.copyOf(remove); }
    }
    public record AccessoryPowerData(List<ResourceLocation> powers, Changes allForms, Map<ResourceLocation, Changes> forms) {
        public AccessoryPowerData { powers = List.copyOf(powers); forms = Map.copyOf(forms); }
    }
    private static final Map<ResourceLocation, AccessoryPowerData> DATA = new LinkedHashMap<>();
    public static void registerAccessoryPower(ResourceLocation item, AccessoryPowerData data) {
        if (DATA.putIfAbsent(item, data) != null) throw new IllegalStateException("Duplicate accessory powers: " + item);
    }
    public static List<ResourceLocation> effectivePowerIds(Player player, List<ResourceLocation> base) {
        var effective = new LinkedHashSet<>(base);
        var additions = new LinkedHashSet<ResourceLocation>();
        var removals = new LinkedHashSet<ResourceLocation>();
        var form = FormManager.current(player).id();
        for (var slot : getAllAccessory(player)) {
            var data = DATA.get(BuiltInRegistries.ITEM.getKey(slot.getB().getItem()));
            if (data == null) continue;
            additions.addAll(data.powers()); additions.addAll(data.allForms().add()); removals.addAll(data.allForms().remove());
            var changes = data.forms().get(form);
            if (changes != null) { additions.addAll(changes.add()); removals.addAll(changes.remove()); }
        }
        effective.removeAll(removals); effective.addAll(additions); return List.copyOf(effective);
    }
    public static List<net.minecraft.util.Tuple<AccessoryItem.SlotData, ItemStack>> getAllAccessory(Player player) {
        List<net.minecraft.util.Tuple<AccessoryItem.SlotData, ItemStack>> allAccessory = new ArrayList<>();
        for (Map.Entry<String, AccessoryUtils.AccessoryIO> entry : AccessoryUtils.activeAccessoryModInterfaces.entrySet()) {
            String ioName = entry.getKey();
            AccessoryUtils.AccessoryIO io = entry.getValue();
            var allSlots = io.getEntitySlots(player);
            if (allSlots != null) {
                for (Map.Entry<net.minecraft.util.Tuple<String, String>, List<ItemStack>> e : allSlots.entrySet()) {
                    var slotPair = e.getKey();
                    List<ItemStack> stacks = e.getValue();
                    int idx = 0;
                    for (ItemStack stack : stacks) {
                        if (stack.getItem() instanceof AccessoryItem && io != AccessoryUtils.nowAccessoryMod) continue;
                        AccessoryItem.SlotData data;
                        data = new AccessoryItem.SlotData(ResourceLocation.fromNamespaceAndPath(ioName, "%s/%s".formatted(slotPair.getA(), slotPair.getB())), idx);
                        allAccessory.add(new net.minecraft.util.Tuple<>(data, stack));
                        idx++;
                    }
                }
            }
        }
        return allAccessory;
    }

    public static void ReApplyAccessoryPowerOnPlayerFormChange(Player player) {
        List<net.minecraft.util.Tuple<AccessoryItem.SlotData, ItemStack>> allAccessory = getAllAccessory(player);
        for (var pair : allAccessory) {
            ItemStack stack = pair.getB();
            if (stack.getItem() instanceof CustomAccessoryPower cpti) {
                cpti.onFormChange(stack, pair.getA(), player);
            }
        }
    }

}
