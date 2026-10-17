package github.com.gengyoubo.sscnf.items.armors;

import net.minecraft.resources.ResourceLocation;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NetheriteMorphscaleArmorRenderer extends GeoArmorRenderer<NetheriteMorphScaleArmor> {
    public NetheriteMorphscaleArmorRenderer() {
        super(new DefaultedItemGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE, "netherite_morphscale_armor")));
    }
}