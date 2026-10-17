package github.com.gengyoubo.sscnf.registry;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.recipe.altar.AltarShapedRecipe;
import github.com.gengyoubo.sscnf.recipe.altar.AltarShapelessRecipe;

/**
 * Fabric 1.10.0 parity: altar and morph-scale upgrade recipe types.
 */
public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            Registries.RECIPE_SERIALIZER, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(
            Registries.RECIPE_TYPE, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);

    private record DummyRecipeType<T extends Recipe<?>>(String id) implements RecipeType<T> {
        @Override public String toString() { return ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE + ":" + id; }
    }

    public static final java.util.function.Supplier<RecipeType<?>> ALTAR_SHAPELESS_TYPE = TYPES.register("altar_shapeless", () -> new DummyRecipeType<>("altar_shapeless"));
    public static final java.util.function.Supplier<RecipeType<?>> ALTAR_SHAPED_TYPE = TYPES.register("altar_shaped", () -> new DummyRecipeType<>("altar_shaped"));
    public static final java.util.function.Supplier<RecipeType<?>> MORPH_SCALE_UPGRADE_TYPE = TYPES.register("morph_scale_upgrade", () -> new DummyRecipeType<>("morph_scale_upgrade"));

    public static final java.util.function.Supplier<RecipeSerializer<?>> ALTAR_SHAPELESS = SERIALIZERS.register("altar_shapeless", AltarShapelessRecipe.Serializer::new);
    public static final java.util.function.Supplier<RecipeSerializer<?>> ALTAR_SHAPED = SERIALIZERS.register("altar_shaped", AltarShapedRecipe.Serializer::new);
    public static final java.util.function.Supplier<RecipeSerializer<?>> MORPH_SCALE_UPGRADE = SERIALIZERS.register(
            "morph_scale_upgrade", github.com.gengyoubo.sscnf.recipe.MorphScaleUpgradeRecipe.Serializer::new);

    private ModRecipeSerializers() {}
}
