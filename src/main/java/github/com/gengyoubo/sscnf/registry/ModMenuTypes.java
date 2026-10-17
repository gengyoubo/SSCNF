package github.com.gengyoubo.sscnf.registry;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.block.entity.AltarBlockEntity;
import github.com.gengyoubo.sscnf.other.menu.AltarMenu;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);
    public static final java.util.function.Supplier<MenuType<AltarMenu>> ALTAR = MENUS.register("altar", () -> IMenuTypeExtension.create((id, inv, buf) -> {
        var pos = buf.readBlockPos();
        var level = inv.player.level();
        var be = level.getBlockEntity(pos);
        if (be instanceof AltarBlockEntity altar) {
            return new AltarMenu(id, inv, altar, altar.dataAccess);
        }
        // fallback dummy
        return new AltarMenu(id, inv, new net.minecraft.world.SimpleContainer(11), new net.minecraft.world.inventory.SimpleContainerData(3));
    }));
    private ModMenuTypes(){}
}
