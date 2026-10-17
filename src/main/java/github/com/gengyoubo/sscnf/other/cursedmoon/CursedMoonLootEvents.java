package github.com.gengyoubo.sscnf.other.cursedmoon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.registry.ModItems;

/** Cursed Moon mob drops, matching Fabric's player-kill-only 45% moon-dust roll. */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID)
public final class CursedMoonLootEvents {
    private static final float MOONDUST_DROP_PROBABILITY = 0.45F;

    private CursedMoonLootEvents() {
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Mob mob) || !CursedMoonService.isInCursedMoon(mob.level())) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof TamableAnimal tameable) {
            attacker = tameable.getOwner();
        }
        if (!(attacker instanceof ServerPlayer) || mob.getRandom().nextFloat() >= MOONDUST_DROP_PROBABILITY) {
            return;
        }

        ItemEntity drop = new ItemEntity(mob.level(), mob.getX(), mob.getY(), mob.getZ(),
                new ItemStack(ModItems.UNTREATED_MOONDUST.get()));
        event.getDrops().add(drop);
    }
}
