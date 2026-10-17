package github.com.gengyoubo.sscnf.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import github.com.gengyoubo.sscnf.util.PotionComponents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.power.TransformativeEffectService;

/** Forge registration and brewing paths corresponding to Fabric RegCustomPotions. */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(
            BuiltInRegistries.POTION, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);

    public static final DeferredHolder<Potion, ? extends Potion> MOONDUST = POTIONS.register("moondust_potion", () -> new Potion(new MobEffectInstance[0]));
    public static final DeferredHolder<Potion, ? extends Potion> TO_BAT = transformativePotion("to_bat_0_potion", ModEffects.TO_BAT);
    public static final DeferredHolder<Potion, ? extends Potion> TO_AXOLOTL = transformativePotion("to_axolotl_0_potion", ModEffects.TO_AXOLOTL);
    public static final DeferredHolder<Potion, ? extends Potion> TO_OCELOT = transformativePotion("to_ocelot_0_potion", ModEffects.TO_OCELOT);
    public static final DeferredHolder<Potion, ? extends Potion> TO_FAMILIAR_FOX = transformativePotion("to_familiar_fox_0_potion", ModEffects.TO_FAMILIAR_FOX);
    public static final DeferredHolder<Potion, ? extends Potion> TO_SNOW_FOX = transformativePotion("to_snow_fox_0_potion", ModEffects.TO_SNOW_FOX);
    public static final DeferredHolder<Potion, ? extends Potion> TO_ANUBIS_WOLF = transformativePotion("to_anubis_wolf_0_potion", ModEffects.TO_ANUBIS_WOLF);
    public static final DeferredHolder<Potion, ? extends Potion> TO_SPIDER = transformativePotion("to_spider_0_potion", ModEffects.TO_SPIDER);
    public static final DeferredHolder<Potion, ? extends Potion> TO_ALLAY = transformativePotion("to_allay_sp_potion", ModEffects.TO_ALLAY);
    public static final DeferredHolder<Potion, ? extends Potion> TO_FERAL_CAT = transformativePotion("to_feral_cat_sp_potion", ModEffects.TO_FERAL_CAT);
    public static final DeferredHolder<Potion, ? extends Potion> FEED = effectPotion("feed_potion", ModEffects.FEED);

    private static DeferredHolder<Potion, ? extends Potion> effectPotion(String id, DeferredHolder<net.minecraft.world.effect.MobEffect, ? extends net.minecraft.world.effect.MobEffect> effect) {
        return POTIONS.register(id, () -> new Potion(new MobEffectInstance(effect, 3600)));
    }

    private static DeferredHolder<Potion, ? extends Potion> transformativePotion(String id, DeferredHolder<net.minecraft.world.effect.MobEffect, ? extends net.minecraft.world.effect.MobEffect> effect) {
        return POTIONS.register(id, () -> new Potion(new MobEffectInstance(
                effect, TransformativeEffectService.DEFAULT_DURATION)));
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void registerBrewing(net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();
        add(builder, Potions.AWKWARD, ModItems.MOONDUST_MATRIX.get(), MOONDUST);
        add(builder, MOONDUST, Items.POINTED_DRIPSTONE, TO_BAT);
        add(builder, MOONDUST, Items.BIG_DRIPLEAF, TO_AXOLOTL);
        add(builder, MOONDUST, Items.CHICKEN, TO_OCELOT);
        add(builder, MOONDUST, ModItems.ECTOPLASM_RAG.get(), TO_ANUBIS_WOLF);
        add(builder, MOONDUST, ModItems.SILK_DEW.get(), TO_SPIDER);
        add(builder, TO_FAMILIAR_FOX, Items.GOLD_NUGGET, TO_SNOW_FOX);
        add(builder, MOONDUST, Items.AMETHYST_SHARD, TO_ALLAY);
        add(builder, MOONDUST, Items.COD_BUCKET, TO_FERAL_CAT);
    }

    private static void add(net.minecraft.world.item.alchemy.PotionBrewing.Builder builder, net.minecraft.core.Holder<Potion> input, net.minecraft.world.item.Item ingredient, net.minecraft.core.Holder<Potion> output) {
        builder.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionComponents.setPotion(new ItemStack(Items.POTION), input)),
                Ingredient.of(ingredient), PotionComponents.setPotion(new ItemStack(Items.POTION), output)));
    }

    private ModPotions() {}
}
