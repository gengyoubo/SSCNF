package github.com.gengyoubo.sscnf.util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
public final class PotionComponents {
    private PotionComponents() {}
    public static ItemStack setPotion(ItemStack stack, Holder<Potion> potion) { stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion)); return stack; }
    public static ItemStack setPotion(ItemStack stack, Potion potion) { return setPotion(stack, net.minecraft.core.registries.BuiltInRegistries.POTION.wrapAsHolder(potion)); }
    public static Holder<Potion> getPotion(ItemStack stack) { return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElse(net.minecraft.world.item.alchemy.Potions.WATER); }
    public static Iterable<net.minecraft.world.effect.MobEffectInstance> getMobEffects(ItemStack stack) { return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getAllEffects(); }
}
