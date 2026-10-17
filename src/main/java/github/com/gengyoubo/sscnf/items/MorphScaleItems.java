package github.com.gengyoubo.sscnf.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Shared recognition for built-in morph-scale items and upgraded equipment. */
public final class MorphScaleItems {
    public static final String MARKER_KEY = "MorphScaleItem";
    private static final TagKey<Item> ITEM_TAG = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("shape-shifter-curse", "morph_scale_item"));

    private MorphScaleItems() {
    }

    public static boolean isMorphScaleItem(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(ITEM_TAG) || hasUpgradeMarker(stack));
    }

    public static boolean hasUpgradeMarker(ItemStack stack) {
        if (stack.isEmpty()) return false;
        var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getBoolean(MARKER_KEY) || tag.getBoolean("MorphScale")
                || tag.getBoolean("morphscale") || tag.getBoolean("shape_shifter_curse_morphscale");
    }
}
