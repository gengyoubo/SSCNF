package github.com.gengyoubo.sscnf.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.entity.WebBulletEntity;
import github.com.gengyoubo.sscnf.registry.ModEntities;

@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class WebBulletRenderer {
    private WebBulletRenderer() {
    }

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.WEB_BULLET.get(), WebBulletRenderer::create);
    }

    private static ThrownItemRenderer<WebBulletEntity> create(EntityRendererProvider.Context context) {
        return new ThrownItemRenderer<>(context, 0.75F, true);
    }
}
