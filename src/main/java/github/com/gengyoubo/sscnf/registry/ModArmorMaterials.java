package github.com.gengyoubo.sscnf.registry;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.*;
import java.util.*;
public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, "shape-shifter-curse");
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MORPHSCALE = MATERIALS.register("morphscale", () -> new ArmorMaterial(Map.of(ArmorItem.Type.HELMET, 2, ArmorItem.Type.LEGGINGS, 6, ArmorItem.Type.CHESTPLATE, 7, ArmorItem.Type.BOOTS, 2), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND), List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("diamond"))), 1.0F, 0.0F));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NETHERITEMORPHSCALE = MATERIALS.register("netherite_morphscale", () -> new ArmorMaterial(Map.of(ArmorItem.Type.HELMET, 3, ArmorItem.Type.LEGGINGS, 7, ArmorItem.Type.CHESTPLATE, 8, ArmorItem.Type.BOOTS, 3), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(Items.NETHERITE_SCRAP), List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("netherite"))), 2.0F, 0.1F));
    private ModArmorMaterials() {}
}
