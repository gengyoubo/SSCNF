package github.com.gengyoubo.sscnf.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.other.config.SscCommonConfig;
import github.com.gengyoubo.sscnf.registry.ModEntities;

/** Spawn placement rules mirrored from the Fabric transformative entity registrations. */
@net.neoforged.fml.common.EventBusSubscriber(modid = ShapeShifterCurseNeoForge.MOD_ID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD)
public final class TransformativeSpawnPlacementEvents {
    private TransformativeSpawnPlacementEvents() { }

    @SubscribeEvent
    public static void register(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.TRANSFORMATIVE_BAT.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TransformativeSpawnPlacementEvents::canSpawnBat,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.TRANSFORMATIVE_AXOLOTL.get(), net.minecraft.world.entity.SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TransformativeSpawnPlacementEvents::canSpawnAxolotl,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.TRANSFORMATIVE_OCELOT.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING, TransformativeSpawnPlacementEvents::canSpawnOcelot,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.TRANSFORMATIVE_SPIDER.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TransformativeSpawnPlacementEvents::canSpawnSpider,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.TRANSFORMATIVE_WOLF.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TransformativeSpawnPlacementEvents::canSpawnWolf,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static boolean canSpawnBat(EntityType<TransformativeBatEntity> type, LevelAccessor level,
                                       MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (pos.getY() >= level.getSeaLevel()) return false;
        double chance = SscCommonConfig.TRANSFORMATIVE_BAT_SPAWN_CHANCE.get();
        if (chance <= 0.0) return false;
        if (chance >= 1.0) return true;
        return random.nextFloat() <= chance
                && level.getMaxLocalRawBrightness(pos) <= random.nextInt(4)
                && TransformativeBatEntity.checkMobSpawnRules(type, level, reason, pos, random);
    }

    private static boolean canSpawnAxolotl(EntityType<TransformativeAxolotlEntity> type, ServerLevelAccessor level,
                                           MobSpawnType reason, BlockPos pos, RandomSource random) {
        double chance = SscCommonConfig.TRANSFORMATIVE_AXOLOTL_SPAWN_CHANCE.get();
        if (chance <= 0.0) return false;
        if (chance >= 1.0) return true;
        return random.nextFloat() < chance || (level.getFluidState(pos).is(Fluids.WATER)
                && TransformativeAxolotlEntity.checkAxolotlSpawnRules(type, level, reason, pos, random));
    }

    private static boolean canSpawnOcelot(EntityType<TransformativeOcelotEntity> type, LevelAccessor level,
                                          MobSpawnType reason, BlockPos pos, RandomSource random) {
        return random.nextFloat() < SscCommonConfig.TRANSFORMATIVE_OCELOT_SPAWN_CHANCE.get();
    }

    private static boolean canSpawnSpider(EntityType<TransformativeSpiderEntity> type, LevelAccessor level,
                                          MobSpawnType reason, BlockPos pos, RandomSource random) {
        return random.nextFloat() < SscCommonConfig.TRANSFORMATIVE_SPIDER_SPAWN_CHANCE.get();
    }

    private static boolean canSpawnWolf(EntityType<TransformativeWolfEntity> type, LevelAccessor level,
                                        MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockPos check = pos;
        for (int i = 0; i < 5; i++, check = check.below()) {
            if (level.getBlockState(check).is(net.minecraft.world.level.block.Blocks.TNT)) return false;
        }
        double chance = SscCommonConfig.TRANSFORMATIVE_WOLF_SPAWN_CHANCE.get();
        return chance >= 1.0 || chance > 0.0 && random.nextFloat() < chance;
    }
}
