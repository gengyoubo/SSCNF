package github.com.gengyoubo.sscnf.client;
public final class EntityPreview {
    private EntityPreview() {}
    public static void render(net.minecraft.client.gui.GuiGraphics graphics, int x, int feetY, int scale, float dx, float dy, net.minecraft.world.entity.LivingEntity entity) {
        net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,
                x - scale, feetY - scale * 2, x + scale, feetY, scale, 0.0625F,
                x - dx, feetY - scale - dy, entity);
    }
}
