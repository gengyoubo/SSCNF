package github.com.gengyoubo.sscnf.client.render;

import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.client.FormKeyMappings;
import github.com.gengyoubo.sscnf.other.config.SscClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

/** Six-slot Fabric skill selector and its three visible sprites. */
@EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, value = Dist.CLIENT)
public final class MagicHudRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE, "textures/perk/textures.png");
    private static final int SLOTS = 6;
    private static int selected;
    private static int held = -1;

    private MagicHudRenderer() {
    }

    private static void change(int delta) {
        selected = Math.floorMod(selected + delta, SLOTS);
    }

    /** Whether the selected-skill key currently stands in for active skill {@code index}. */
    public static boolean virtualPressed(int index) {
        return held == index;
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) {
            held = -1;
            return;
        }
        if (minecraft.screen == null) {
            while (FormKeyMappings.PREVIOUS_SKILL.consumeClick()) change(-1);
            while (FormKeyMappings.NEXT_SKILL.consumeClick()) change(1);
        }
        held = minecraft.screen == null && FormKeyMappings.SELECTED_SKILL.isDown() ? selected : -1;
    }

    @SubscribeEvent
    public static void scroll(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        double delta = event.getScrollDeltaY();
        if (minecraft.player == null || minecraft.screen != null
                || !FormKeyMappings.SCROLL_SKILL.isDown() || delta == 0) return;
        change(delta > 0 ? -1 : 1);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) return;
        GuiGraphics graphics = event.getGuiGraphics();
        int anchor = SscClientConfig.MAGIC_POSITION.get();
        int x = HudPosition.x(graphics.guiWidth(), anchor, SscClientConfig.MAGIC_OFFSET_X.get());
        int y = HudPosition.y(graphics.guiHeight(), anchor, SscClientConfig.MAGIC_OFFSET_Y.get());
        int[] slots = {Math.floorMod(selected - 1, SLOTS), selected, (selected + 1) % SLOTS};
        for (int i = 0; i < slots.length; i++) {
            boolean center = i == 1;
            int size = center ? 24 : 16;
            int spriteX = x + (i == 0 ? 0 : center ? 18 : 44);
            int spriteY = y + (center ? 0 : 4);
            // The selected slot uses the lit sprites in columns 1-6; its neighbours the dim ones in 7-12.
            int column = slots[i] + (center ? 1 : 7);
            graphics.pose().pushPose();
            graphics.pose().translate(spriteX, spriteY, 0);
            graphics.pose().scale(size / 16F, size / 16F, 1);
            graphics.blit(TEXTURE, 0, 0, column * 16, 0, 16, 16, 512, 512);
            graphics.pose().popPose();
        }
    }
}
