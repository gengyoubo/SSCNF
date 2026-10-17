package github.com.gengyoubo.sscnf.mixin;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import github.com.gengyoubo.sscnf.power.FormPowerRegistry;
import github.com.gengyoubo.sscnf.power.FormPowerRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Applies Apoli modify_food before vanilla clamps hunger and saturation. */
@Mixin(Player.class)
public abstract class FoodDataPowerMixin {
    @Redirect(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(Lnet/minecraft/world/food/FoodProperties;)V"))
    private void ssc$modifyFood(FoodData foodData, net.minecraft.world.food.FoodProperties properties,
                               net.minecraft.world.level.Level level, ItemStack stack, net.minecraft.world.food.FoodProperties original) {
        Player player = (Player)(Object)this;
        int food = properties.nutrition();
        float saturation = food == 0 ? 0.0F : properties.saturation() / (food * 2.0F);
        List<JsonObject> foodModifiers = new ArrayList<>();
        List<JsonObject> saturationModifiers = new ArrayList<>();
        FormPowerRegistry.visitActive(player, (id, power) -> {
            if (!"shape_shifter_curse:modify_food".equals(FormPowerRegistry.typeOf(power))
                    || !FormPowerRuntime.test(player, player, power.getAsJsonObject("condition"))
                    || !FormPowerRuntime.matchesItem(stack, power.getAsJsonObject("item_condition"))) return;
            ssc$collect(power, "food_modifier", "food_modifiers", foodModifiers);
            ssc$collect(power, "saturation_modifier", "saturation_modifiers", saturationModifiers);
        });
        if (!foodModifiers.isEmpty()) {
            food = (int) FormPowerRuntime.applyModifierList(player, food, foodModifiers);
        }
        if (!saturationModifiers.isEmpty()) {
            saturation = (float) FormPowerRuntime.applyModifierList(player, saturation, saturationModifiers);
        }
        foodData.eat(food, saturation);
    }

    private static void ssc$collect(JsonObject power, String single, String plural, List<JsonObject> into) {
        if (power.has(single) && power.get(single).isJsonObject()) into.add(power.getAsJsonObject(single));
        if (power.has(plural) && power.get(plural).isJsonArray()) {
            for (var entry : power.getAsJsonArray(plural)) {
                if (entry.isJsonObject()) into.add(entry.getAsJsonObject());
            }
        }
    }
}
