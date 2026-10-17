package github.com.gengyoubo.sscnf.items.armors;

import net.minecraft.resources.ResourceLocation;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MorphscaleArmorRenderer extends GeoArmorRenderer<MorphScaleArmor> {
    public MorphscaleArmorRenderer() {
        super(new DefaultedItemGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE, "morphscale_armor")));
    }
}