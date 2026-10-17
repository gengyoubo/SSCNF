package github.com.gengyoubo.sscnf.items.tools;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Forge port of Fabric's {@code AuxiliaryAxeToolMaterial}. High base damage, no
 * mining level: extra-hand weapon.
 */
public final class AuxiliaryAxeToolMaterial implements Tier {
    public static final AuxiliaryAxeToolMaterial INSTANCE = new AuxiliaryAxeToolMaterial();

    private AuxiliaryAxeToolMaterial() {
    }

    @Override
    public int getUses() {
        return 781;
    }

    @Override
    public float getSpeed() {
        return 1.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 6.0F;
    }

    @Override public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() { return net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL; }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.DIAMOND);
    }
}