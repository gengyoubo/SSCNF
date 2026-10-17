package github.com.gengyoubo.sscnf.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import github.com.gengyoubo.sscnf.registry.ModRecipeSerializers;

import java.util.Objects;

/**
 * Forge equivalent of Fabric's MorphScaleUpgradeRecipe.  The template and
 * addition are data-driven; the middle smithing slot may be any item that has
 * not already received the MorphScaleItem marker.  SSC item conditions use
 * this exact marker when checking morph-scale equipment.
 */
public final class MorphScaleUpgradeRecipe implements SmithingRecipe {
    private static final String MORPH_SCALE_ITEM = "MorphScaleItem";

    private final ResourceLocation id;
    private final Ingredient template;
    private final Ingredient addition;

    public MorphScaleUpgradeRecipe(ResourceLocation id, Ingredient template, Ingredient addition) {
        this.id = id;
        this.template = template;
        this.addition = addition;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return !stack.isEmpty() && !stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getBoolean(MORPH_SCALE_ITEM);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return addition.test(stack);
    }

    @Override
    public boolean matches(net.minecraft.world.item.crafting.SmithingRecipeInput container, Level level) {
        return isTemplateIngredient(container.getItem(0))
                && isBaseIngredient(container.getItem(1))
                && isAdditionIngredient(container.getItem(2));
    }

    @Override
    public ItemStack assemble(net.minecraft.world.item.crafting.SmithingRecipeInput container, net.minecraft.core.HolderLookup.Provider access) {
        ItemStack result = container.getItem(1).copy();
        result.setCount(1);
        net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, result, tag -> tag.putBoolean(MORPH_SCALE_ITEM, true));
        return result;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider access) {
        ItemStack preview = new ItemStack(Items.STONE);
        net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, preview, tag -> tag.putBoolean(MORPH_SCALE_ITEM, true));
        return preview;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MORPH_SCALE_UPGRADE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SmithingRecipe.super.getType();
    }

    public static final class Serializer implements RecipeSerializer<MorphScaleUpgradeRecipe> {
        private static final com.mojang.serialization.MapCodec<MorphScaleUpgradeRecipe> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("template").forGetter(r -> r.template),
            Ingredient.CODEC_NONEMPTY.fieldOf("addition").forGetter(r -> r.addition)
        ).apply(instance, (template, addition) -> new MorphScaleUpgradeRecipe(ResourceLocation.parse("shape-shifter-curse:morph_scale_upgrade"), template, addition)));
        @Override public com.mojang.serialization.MapCodec<MorphScaleUpgradeRecipe> codec() { return CODEC; }
        @Override public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, MorphScaleUpgradeRecipe> streamCodec() { return net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistries(CODEC.codec()); }
    }
}
