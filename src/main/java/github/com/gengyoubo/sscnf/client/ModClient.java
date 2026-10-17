package github.com.gengyoubo.sscnf.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.client.renderer.entity.OcelotRenderer;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.client.screen.AltarScreen;
import github.com.gengyoubo.sscnf.client.render.FormAttunerBeamRenderer;
import github.com.gengyoubo.sscnf.client.render.AnubisWolfMinionRenderer;
import github.com.gengyoubo.sscnf.registry.ModBlockEntities;
import github.com.gengyoubo.sscnf.registry.ModMenuTypes;
import github.com.gengyoubo.sscnf.registry.ModEntities;
import github.com.gengyoubo.sscnf.registry.ModBlocks;

@SuppressWarnings("removal")
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClient {
    @SubscribeEvent
    public static void registerScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ALTAR.get(), AltarScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TEMP_WEB_BRIDGE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FORM_ATTUNER.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ANUBIS_WOLF_MINION.get(), AnubisWolfMinionRenderer::new);
        event.registerEntityRenderer(ModEntities.TRANSFORMATIVE_BAT.get(), BatRenderer::new);
        event.registerEntityRenderer(ModEntities.TRANSFORMATIVE_AXOLOTL.get(), AxolotlRenderer::new);
        event.registerEntityRenderer(ModEntities.TRANSFORMATIVE_OCELOT.get(), OcelotRenderer::new);
        event.registerEntityRenderer(ModEntities.TRANSFORMATIVE_SPIDER.get(), SpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.TRANSFORMATIVE_WOLF.get(), WolfRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FORM_ATTUNER.get(), FormAttunerBeamRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AnubisWolfMinionRenderer.LAYER,
                AnubisWolfMinionRenderer::createBodyLayer);
    }
}
