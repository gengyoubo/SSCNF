package github.com.gengyoubo.sscnf.items.armors;
import net.minecraft.world.item.*;
import net.minecraft.core.Holder;
import java.util.*;
public final class NetheriteMorphscaleArmorMaterial {
    public static final Holder<ArmorMaterial> INSTANCE = github.com.gengyoubo.sscnf.registry.ModArmorMaterials.NETHERITEMORPHSCALE;
    public static int durability(ArmorItem.Type type) { return switch (type) { case BOOTS -> 429; case LEGGINGS -> 495; case CHESTPLATE -> 528; case HELMET -> 363; case BODY -> 528; }; }
    private NetheriteMorphscaleArmorMaterial() {}
}
