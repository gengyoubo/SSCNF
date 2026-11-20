package github.com.gengyoubo.sscnf.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;

/** Mod sound events; datapack powers resolve them by id through the sound registry. */
public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE);
    public static final DeferredHolder<SoundEvent, SoundEvent> AVALI_CHATTER = register("avali_chatter");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String path) {
        return SOUNDS.register(path, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE, path)));
    }

    private ModSounds() {}
}
