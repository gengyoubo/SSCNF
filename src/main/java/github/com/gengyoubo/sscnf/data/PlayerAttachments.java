package github.com.gengyoubo.sscnf.data;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import java.util.function.Supplier;
public final class PlayerAttachments {
    private static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ShapeShifterCurseNeoForge.MOD_ID);
    public static final Supplier<AttachmentType<PlayerFormData>> PLAYER_FORM = TYPES.register("player_form", () -> AttachmentType.builder(PlayerFormData::new).serialize(CompoundTag.CODEC.xmap(tag -> { var data = new PlayerFormData(); data.deserializeNBT(tag); return data; }, PlayerFormData::serializeNBT)).build());
    public static final Supplier<AttachmentType<PlayerSkinData>> PLAYER_SKIN = TYPES.register("player_skin", () -> AttachmentType.builder(PlayerSkinData::new).serialize(CompoundTag.CODEC.xmap(tag -> { var data = new PlayerSkinData(); data.deserializeNBT(tag); return data; }, PlayerSkinData::serializeNBT)).build());
    public static void register(IEventBus bus) { TYPES.register(bus); }
    private PlayerAttachments() {}
}
