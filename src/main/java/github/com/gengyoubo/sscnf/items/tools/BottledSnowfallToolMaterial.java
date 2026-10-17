package github.com.gengyoubo.sscnf.items.tools;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Forge port of Fabric's {@code BottledSnowfallToolMaterial}. Low durability,
 * no combat/mine bonus: Snow Fox form tool whose real behavior lives in powers.
 */
public final class BottledSnowfallToolMaterial implements Tier {
    public static final BottledSnowfallToolMaterial INSTANCE = new BottledSnowfallToolMaterial();

    private BottledSnowfallToolMaterial() {
    }

    @Override
    public int getUses() {
        return 300;
    }

    @Override
    public float getSpeed() {
        return 1.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 0.0F;
    }

    @Override public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() { return net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL; }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.POWDER_SNOW_BUCKET);
    }
}